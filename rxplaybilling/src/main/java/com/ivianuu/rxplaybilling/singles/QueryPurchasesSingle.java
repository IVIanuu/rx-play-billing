package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.ivianuu.rxplaybilling.model.PurchasesResponse;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * Query purchases single
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class QueryPurchasesSingle extends BaseSingle<PurchasesResponse> {

    private final String skuType;

    private QueryPurchasesSingle(BillingClient billingClient, String skuType) {
        super(billingClient);
        this.skuType = skuType;
    }
    /**
     * Queries the purchases and emits the response
     */
    @CheckResult @NonNull
    public static Single<PurchasesResponse> create(@NonNull BillingClient billingClient,
                                                   @BillingClient.SkuType @NonNull String skuType) {
        return Single.create(new QueryPurchasesSingle(billingClient, skuType));
    }

    @Override
    public void subscribe(final SingleEmitter<PurchasesResponse> e) throws Exception {
        Purchase.PurchasesResult result = billingClient.queryPurchases(skuType);
        e.onSuccess(new PurchasesResponse(result.getPurchasesList(), result.getResponseCode()));
    }
}
