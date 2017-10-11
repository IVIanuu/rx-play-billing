package com.ivianuu.rxplaybilling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetailsParams;
import com.ivianuu.rxplaybilling.model.ConsumeResponse;
import com.ivianuu.rxplaybilling.model.PurchasesResponse;
import com.ivianuu.rxplaybilling.model.Response;
import com.ivianuu.rxplaybilling.model.SkuDetailsResponse;
import com.ivianuu.rxplaybilling.singles.ConsumeSingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchasesNetworkSingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchasesSingle;
import com.ivianuu.rxplaybilling.singles.QuerySkuDetailsSingle;
import com.ivianuu.rxplaybilling.singles.StartConnectionSingle;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

import static com.ivianuu.rxplaybilling.Preconditions.checkNotNull;

/**
 * Wraps a BillingClient
 */
public final class RxPlayBilling {

    private final BillingClient billingClient;
    private final PublishSubject<PurchasesResponse> purchasesSubject = PublishSubject.create();

    private RxPlayBilling(Context context) {
        billingClient = BillingClient.newBuilder(context)
                .setListener((responseCode, purchases)
                        -> purchasesSubject.onNext(new PurchasesResponse(purchases, responseCode)))
                .build();
    }

    /**
     * Returns the RxPlayBilling instance
     */
    @NonNull
    public static RxPlayBilling create(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return new RxPlayBilling(context.getApplicationContext());
    }

    /**
     * Emit's purchase updates
     */
    @CheckResult @NonNull
    public Observable<PurchasesResponse> purchaseUpdates() {
        return purchasesSubject;
    }

    /**
     * Releases all resources of this instance
     */
    public void release() {
        billingClient.endConnection();
    }

    /**
     * Check if specified feature or capability is supported by the Play Store.
     */
    @CheckResult @NonNull
    public Single<Response> isFeatureSupported(@BillingClient.FeatureType @NonNull final String feature) {
        checkNotNull(feature, "feature == null");
        return connect()
                .map(response -> {
                    if (response.success()) {
                        return new Response(billingClient.isFeatureSupported(feature));
                    } else {
                        return response;
                    }
                });
    }

    /**
     * Initiate the UI flow for an in-app purchase or subscription.
     */
    @CheckResult @NonNull
    public Single<Response> launchBillingFlow(@NonNull final Activity activity,
                                              @NonNull final BillingFlowParams billingFlowParams) {
        checkNotNull(activity, "activity == null");
        checkNotNull(billingFlowParams, "billingFlowParams == null");
        return connect()
                .map(response -> {
                    if (response.success()) {
                        return new Response(billingClient.launchBillingFlow(activity, billingFlowParams));
                    } else {
                        return response;
                    }
                });
    }

    /**
     * result of consumption, the user will no longer own it.
     * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a
     */
    @CheckResult @NonNull
    public Single<ConsumeResponse> consume(@NonNull final String purchaseToken) {
        checkNotNull(purchaseToken, "purchaseToken == null");
        return connect()
                .flatMap(response -> {
                    if (response.success()) {
                        return ConsumeSingle.create(billingClient, purchaseToken);
                    } else {
                        return Single.just(new ConsumeResponse(null, response.responseCode()));
                    }
                });
    }

    /**
     * Get purchases details for all the items bought within your app. This method uses a cache of
     * Google Play Store app without initiating a network request.
     */
    @CheckResult @NonNull
    public Single<PurchasesResponse> queryPurchases(@BillingClient.SkuType @NonNull final String skuType) {
        checkNotNull(skuType, "skuType == null");
        return connect()
                .flatMap(response -> {
                    if (response.success()) {
                        return QueryPurchasesSingle.create(billingClient, skuType);
                    } else {
                        return Single.just(new PurchasesResponse(null, response.responseCode()));
                    }
                });
    }

    /**
     * Returns the most recent purchase made by the user for each SKU, even if that purchase is
     * expired, canceled, or consumed.
     */
    @CheckResult @NonNull
    public Single<PurchasesResponse> queryPurchasesNetwork(@BillingClient.SkuType @NonNull final String skuType) {
        checkNotNull(skuType, "skuType == null");
        return connect()
                .flatMap(__ -> QueryPurchasesNetworkSingle.create(billingClient, skuType));
    }

    /**
     * Perform a network query to get SKU details and return the result asynchronously.
     */
    @CheckResult @NonNull
    public Single<SkuDetailsResponse> querySkuDetails(@NonNull SkuDetailsParams skuDetailsParams) {
        checkNotNull(skuDetailsParams, "skuDetailsParams == null");
        return connect()
                .flatMap(__ -> QuerySkuDetailsSingle.create(billingClient, skuDetailsParams));
    }

    private Single<Response> connect() {
        if (billingClient.isReady()) {
            // were already connected
            return Single.just(new Response(BillingClient.BillingResponse.OK));
        }

        return StartConnectionSingle.create(billingClient);
    }

}
