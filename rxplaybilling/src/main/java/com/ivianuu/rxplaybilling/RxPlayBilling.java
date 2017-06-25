package com.ivianuu.rxplaybilling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.subjects.PublishSubject;

/**
 * Wraps a BillingClient
 */
public class RxPlayBilling {

    private BillingClient billingClient;
    private PublishSubject<PurchaseUpdate> purchaseUpdatesSubject = PublishSubject.create();

    public RxPlayBilling(@NonNull Context context) {
        billingClient = new BillingClient.Builder(context)
                .setListener(purchasesUpdatedListener)
                .build();
    }

    /**
     * Check if specified feature or capability is supported by the Play Store.
     */
    @BillingClient.BillingResponse
    public int isFeatureSupported(@NonNull @BillingClient.FeatureType String feature) {
        throwIfNotReady();
        return billingClient.isFeatureSupported(feature);
    }

    /**
     * Checks if the client is currently connected to the service, so that requests to other methods
     * will succeed.
     */
    public boolean isReady() {
        return billingClient.isReady();
    }

    /**
     * Connects to the billing service
     * Observable emits on connection changes true means connected false disconnected
     */
    @NonNull
    public Observable<Boolean> connect() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final ObservableEmitter<Boolean> e) throws Exception {
                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(int resultCode) {
                        if (resultCode == BillingClient.BillingResponse.OK) {
                            e.onNext(true);
                        } else {
                            e.onError(new Throwable(String.valueOf(resultCode)));
                        }
                    }

                    @Override
                    public void onBillingServiceDisconnected() {
                        if (!e.isDisposed()) {
                            e.onNext(false);
                        }
                    }
                });
            }
        });
    }

    /**
     * Close the connection and release all held resources such as service connections.
     */
    public void disconnect() {
        billingClient.endConnection();
    }

    /**
     * Initiate the UI flow for an in-app purchase or subscription.
     */
    @BillingClient.BillingResponse
    public int launchBillingFlow(@NonNull Activity activity, @NonNull BillingFlowParams billingFlowParams) {
        throwIfNotReady();
        return billingClient.launchBillingFlow(activity, billingFlowParams);
    }

    /**
     * Perform a network query to get SKU details and return the result asynchronously.
     */
    @NonNull
    public Purchase.PurchasesResult queryPurchases(@NonNull @BillingClient.SkuType String skuType) {
        throwIfNotReady();
        return billingClient.queryPurchases(skuType);
    }

    /**
     * Returns the most recent purchase made by the user for each SKU, even if that purchase is
     * expired, canceled, or consumed.
     */
    @NonNull
    public Single<Purchase.PurchasesResult> queryPurchasesNetwork(@NonNull @BillingClient.SkuType final String skuType) {
        throwIfNotReady();
        return Single.create(new SingleOnSubscribe<Purchase.PurchasesResult>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final SingleEmitter<Purchase.PurchasesResult> e) throws Exception {
                billingClient.queryPurchaseHistoryAsync(skuType, new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(Purchase.PurchasesResult result) {
                        if (!e.isDisposed()) {
                            e.onSuccess(result);
                        }
                    }
                });
            }
        });
    }

    /**
     * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a
     * result of consumption, the user will no longer own it.
     */
    @NonNull
    public Single<ConsumeResponse> consume(@NonNull final String purchaseToken) {
        throwIfNotReady();
        return Single.create(new SingleOnSubscribe<ConsumeResponse>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final SingleEmitter<ConsumeResponse> e) throws Exception {
                billingClient.consumeAsync(purchaseToken, new ConsumeResponseListener() {
                    @Override
                    public void onConsumeResponse(String purchaseToken, int resultCode) {
                        if (!e.isDisposed()) {
                            e.onSuccess(new ConsumeResponse(purchaseToken, resultCode));
                        }
                    }
                });
            }
        });
    }

    /**
     * Returns the most recent purchase made by the user for each SKU, even if that purchase is
     * expired, canceled, or consumed.
     */
    @NonNull
    public Single<Purchase.PurchasesResult> queryPurchaseHistory(@NonNull @BillingClient.SkuType final String skuType) {
        throwIfNotReady();
        return Single.create(new SingleOnSubscribe<Purchase.PurchasesResult>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final SingleEmitter<Purchase.PurchasesResult> e) throws Exception {
                billingClient.queryPurchaseHistoryAsync(skuType, new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(Purchase.PurchasesResult result) {
                        if (!e.isDisposed()) {
                            e.onSuccess(result);
                        }
                    }
                });
            }
        });
    }

    /**
     * Emit's on every purchase update
     */
    @NonNull
    public Observable<PurchaseUpdate> purchaseUpdates() {
        return purchaseUpdatesSubject;
    }

    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
            purchaseUpdatesSubject.onNext(new PurchaseUpdate(purchases, responseCode));
        }
    };

    private void throwIfNotReady() {
        if (!isReady()) {
            throw new IllegalStateException("you need to wait for state connected first");
        }
    }
}
