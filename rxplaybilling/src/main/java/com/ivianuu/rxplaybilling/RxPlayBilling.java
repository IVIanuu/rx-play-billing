package com.ivianuu.rxplaybilling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.ivianuu.rxplaybilling.model.ConsumeResponse;
import com.ivianuu.rxplaybilling.model.PurchasesResponse;
import com.ivianuu.rxplaybilling.model.Response;
import com.ivianuu.rxplaybilling.singles.ConsumeSingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchaseHistorySingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchasesNetworkSingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchasesSingle;
import com.ivianuu.rxplaybilling.singles.StartConnectionSingle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Wraps a BillingClient
 */
public final class RxPlayBilling {

    private static RxPlayBilling instance;

    private final BillingClient billingClient;
    private final PublishSubject<PurchasesResponse> purchasesSubject = PublishSubject.create();

    private RxPlayBilling(@NonNull Context context) {
        billingClient = new BillingClient.Builder(context)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
                        purchasesSubject.onNext(new PurchasesResponse(purchases, responseCode));
                    }
                })
                .build();
    }

    /**
     * Returns the RxPlayBilling instance
     */
    @NonNull
    public static RxPlayBilling get(@NonNull Context context) {
        if (instance == null) {
            instance = new RxPlayBilling(context.getApplicationContext());
        }

        return instance;
    }

    /**
     * Check if specified feature or capability is supported by the Play Store.
     */
    @CheckResult @NonNull
    public Single<Response> isFeatureSupported(@NonNull @BillingClient.FeatureType final String feature) {
        return connect()
                .map(new Function<Response, Response>() {
                    @Override
                    public Response apply(Response response) throws Exception {
                        if (response.success()) {
                            return new Response(billingClient.isFeatureSupported(feature));
                        } else {
                            return response;
                        }
                    }
                });
    }

    /**
     * Initiate the UI flow for an in-app purchase or subscription.
     */
    @CheckResult @NonNull
    public Single<Response> launchBillingFlow(@NonNull final Activity activity, @NonNull final BillingFlowParams billingFlowParams) {
        return connect()
                .map(new Function<Response, Response>() {
                    @Override
                    public Response apply(Response response) throws Exception {
                        if (response.success()) {
                            return new Response(billingClient.launchBillingFlow(activity, billingFlowParams));
                        } else {
                            return response;
                        }
                    }
                });
    }

    /**
     * result of consumption, the user will no longer own it.
     * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a
     */
    @CheckResult @NonNull
    public Single<ConsumeResponse> consume(@NonNull final String purchaseToken) {
        return connect()
                .flatMap(new Function<Response, SingleSource<? extends ConsumeResponse>>() {
                    @Override
                    public SingleSource<? extends ConsumeResponse> apply(Response response) throws Exception {
                        if (response.success()) {
                            return ConsumeSingle.create(billingClient, purchaseToken);
                        } else {
                            return Single.just(new ConsumeResponse(null, response.responseCode()));
                        }
                    }
                });
    }

    /**
     * Perform a network query to get SKU details and return the result asynchronously.
     */
    @CheckResult @NonNull
    public Single<PurchasesResponse> queryPurchases(@NonNull @BillingClient.SkuType final String skuType) {
        return connect()
                .flatMap(new Function<Response, SingleSource<? extends PurchasesResponse>>() {
                    @Override
                    public SingleSource<? extends PurchasesResponse> apply(Response response) throws Exception {
                        if (response.success()) {
                            return QueryPurchasesSingle.create(billingClient, skuType);
                        } else {
                            return Single.just(new PurchasesResponse(null, response.responseCode()));
                        }
                    }
                });
    }

    /**
     * Returns the most recent purchase made by the user for each SKU, even if that purchase is
     * expired, canceled, or consumed.
     */
    @CheckResult @NonNull
    public Single<PurchasesResponse> queryPurchasesNetwork(@NonNull @BillingClient.SkuType final String skuType) {
        return connect()
                .flatMap(new Function<Response, SingleSource<PurchasesResponse>>() {
                    @Override
                    public SingleSource<PurchasesResponse> apply(Response __) throws Exception {
                        return QueryPurchasesNetworkSingle.create(billingClient, skuType);
                    }
                });
    }

    /**
     * Returns the most recent purchase made by the user for each SKU, even if that purchase is
     * expired, canceled, or consumed.
     */
    @CheckResult @NonNull
    public Single<PurchasesResponse> queryPurchaseHistory(@NonNull @BillingClient.SkuType final String skuType) {
        return connect()
                .flatMap(new Function<Response, SingleSource<PurchasesResponse>>() {
                    @Override
                    public SingleSource<PurchasesResponse> apply(Response response) throws Exception {
                        return QueryPurchaseHistorySingle.create(billingClient, skuType);
                    }
                });
    }

    /**
     * Emit's on every purchase update
     */
    @CheckResult @NonNull
    public Observable<PurchasesResponse> purchaseUpdates() {
        return purchasesSubject;
    }

    private Single<Response> connect() {
        if (billingClient.isReady()) {
            // were already connected
            return Single.just(new Response(BillingClient.BillingResponse.OK));
        }

        return StartConnectionSingle.create(billingClient);
    }

}
