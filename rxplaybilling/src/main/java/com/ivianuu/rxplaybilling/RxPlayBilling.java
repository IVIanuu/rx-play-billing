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
import com.ivianuu.rxplaybilling.model.PurchaseUpdate;
import com.ivianuu.rxplaybilling.singles.ConsumeSingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchaseHistorySingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchasesNetworkSingle;
import com.ivianuu.rxplaybilling.singles.QueryPurchasesSingle;
import com.ivianuu.rxplaybilling.singles.StartConnectionSingle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Wraps a BillingClient
 */
public final class RxPlayBilling {

    private static RxPlayBilling instance;

    private BillingClient billingClient;
    private PublishSubject<PurchaseUpdate> purchaseUpdatesSubject = PublishSubject.create();

    private RxPlayBilling(@NonNull Context context) {
        billingClient = new BillingClient.Builder(context)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
                        purchaseUpdatesSubject.onNext(new PurchaseUpdate(purchases, responseCode));
                    }
                })
                .build();
    }

    /**
     * Returns the RxPlayBilling instance
     */
    @NonNull
    public static RxPlayBilling getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new RxPlayBilling(context.getApplicationContext());
        }

        return instance;
    }

    /**
     * Check if specified feature or capability is supported by the Play Store.
     */
    @BillingClient.BillingResponse
    @CheckResult @NonNull
    public Single<Integer> isFeatureSupported(@NonNull @BillingClient.FeatureType final String feature) {
        return connect()
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer resultCode) throws Exception {
                        if (resultCode == BillingClient.BillingResponse.OK) {
                            return billingClient.isFeatureSupported(feature);
                        }

                        throw new BillingException(resultCode);
                    };
                });
    }

    /**
     * Initiate the UI flow for an in-app purchase or subscription.
     */
    @BillingClient.BillingResponse
    @CheckResult @NonNull
    public Single<Integer> launchBillingFlow(@NonNull final Activity activity, @NonNull final BillingFlowParams billingFlowParams) {
        return connect()
                .doOnSuccess(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer resultCode) throws Exception {
                        if (resultCode == BillingClient.BillingResponse.OK) {
                            billingClient.launchBillingFlow(activity, billingFlowParams);
                        }

                        throw new BillingException(resultCode);
                    }
                });
    }

    /**
     * Perform a network query to get SKU details and return the result asynchronously.
     */
    @CheckResult @NonNull
    public Single<Purchase.PurchasesResult> queryPurchases(@NonNull @BillingClient.SkuType final String skuType) {
        return connect()
                .doOnSuccess(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer resultCode) throws Exception {
                        if (resultCode != BillingClient.BillingResponse.OK) {
                            throw new BillingException(resultCode);
                        }
                    }
                })
                .flatMap(new Function<Integer, SingleSource<Purchase.PurchasesResult>>() {
                    @Override
                    public SingleSource<Purchase.PurchasesResult> apply(Integer __) throws Exception {
                        return QueryPurchasesSingle.create(billingClient, skuType);
                    }
                });
    }

    /**
     * Returns the most recent purchase made by the user for each SKU, even if that purchase is
     * expired, canceled, or consumed.
     */
    @CheckResult @NonNull
    public Single<Purchase.PurchasesResult> queryPurchasesNetwork(@NonNull @BillingClient.SkuType final String skuType) {
        return connect()
                .doOnSuccess(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer resultCode) throws Exception {
                        if (resultCode != BillingClient.BillingResponse.OK) {
                            throw new BillingException(resultCode);
                        }
                    }
                })
                .flatMap(new Function<Integer, SingleSource<Purchase.PurchasesResult>>() {
                    @Override
                    public SingleSource<Purchase.PurchasesResult> apply(Integer __) throws Exception {
                        return QueryPurchasesNetworkSingle.create(billingClient, skuType);
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
                .doOnSuccess(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer resultCode) throws Exception {
                        if (resultCode != BillingClient.BillingResponse.OK) {
                            throw new BillingException(resultCode);
                        }
                    }
                })
                .flatMap(new Function<Integer, SingleSource<ConsumeResponse>>() {
                    @Override
                    public SingleSource<ConsumeResponse> apply(Integer __) throws Exception {
                        return ConsumeSingle.create(billingClient, purchaseToken);
                    }
                });
    }

    /**
     * Returns the most recent purchase made by the user for each SKU, even if that purchase is
     * expired, canceled, or consumed.
     */
    @CheckResult @NonNull
    public Single<Purchase.PurchasesResult> queryPurchaseHistory(@NonNull @BillingClient.SkuType final String skuType) {
        return connect()
                .doOnSuccess(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer resultCode) throws Exception {
                        if (resultCode != BillingClient.BillingResponse.OK) {
                            throw new BillingException(resultCode);
                        }
                    }
                })
                .flatMap(new Function<Integer, SingleSource<Purchase.PurchasesResult>>() {
                    @Override
                    public SingleSource<Purchase.PurchasesResult> apply(Integer __) throws Exception {
                        return QueryPurchaseHistorySingle.create(billingClient, skuType);
                    }
                });
    }

    /**
     * Emit's on every purchase update
     */
    @CheckResult @NonNull
    public Observable<PurchaseUpdate> purchaseUpdates() {
        return purchaseUpdatesSubject;
    }

    private Single<Integer> connect() {
        if (billingClient.isReady()) {
            // were already connected
            return Single.just(BillingClient.BillingResponse.OK);
        }

        return StartConnectionSingle.create(billingClient)
                .doOnSuccess(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer resultCode) throws Exception {
                        if (resultCode != BillingClient.BillingResponse.OK) {
                            throw new IllegalStateException(String.valueOf(resultCode));
                        }
                    }
                });
    }

}
