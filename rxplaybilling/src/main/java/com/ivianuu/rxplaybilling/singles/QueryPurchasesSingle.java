package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Query purchases single
 */
@RestrictTo(RestrictTo.Scope.GROUP_ID)
public final class QueryPurchasesSingle implements SingleOnSubscribe<Purchase.PurchasesResult> {

    private BillingClient billingClient;
    private String skuType;

    private QueryPurchasesSingle(BillingClient billingClient, String skuType) {
        this.billingClient = billingClient;
        this.skuType = skuType;
    }

    @CheckResult @NonNull
    public static Single<Purchase.PurchasesResult> create(@NonNull BillingClient billingClient, @NonNull String skuType) {
        return Single.create(new QueryPurchasesSingle(billingClient, skuType));
    }

    @Override
    public void subscribe(final SingleEmitter<Purchase.PurchasesResult> e) throws Exception {
        if (!e.isDisposed()) {
            e.onSuccess(billingClient.queryPurchases(skuType));
        }
    }
}
