package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Query purchases network single
 */
@RestrictTo(RestrictTo.Scope.GROUP_ID)
public final class QueryPurchasesNetworkSingle implements SingleOnSubscribe<Purchase.PurchasesResult> {

    private BillingClient billingClient;
    private String skuType;

    private QueryPurchasesNetworkSingle(BillingClient billingClient, String skuType) {
        this.billingClient = billingClient;
        this.skuType = skuType;
    }

    @CheckResult @NonNull
    public static Single<Purchase.PurchasesResult> create(@NonNull BillingClient billingClient, @NonNull String skuType) {
        return Single.create(new QueryPurchasesNetworkSingle(billingClient, skuType));
    }

    @Override
    public void subscribe(final SingleEmitter<Purchase.PurchasesResult> e) throws Exception {
        billingClient.queryPurchaseHistoryAsync(skuType, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(Purchase.PurchasesResult result) {
                if (!e.isDisposed()) {
                    e.onSuccess(result);
                }
            }
        });
    }
}
