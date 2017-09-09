package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.ivianuu.rxplaybilling.model.PurchasesResponse;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * Query purchase history single
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class QueryPurchaseHistorySingle extends BaseSingle<PurchasesResponse> {

    private final String skuType;

    private QueryPurchaseHistorySingle(BillingClient billingClient, String skuType) {
        super(billingClient);
        this.skuType = skuType;
    }

    @CheckResult @NonNull
    public static Single<PurchasesResponse> create(@NonNull BillingClient billingClient, @NonNull String skuType) {
        return Single.create(new QueryPurchaseHistorySingle(billingClient, skuType));
    }

    @Override
    public void subscribe(final SingleEmitter<PurchasesResponse> e) throws Exception {
        billingClient.queryPurchaseHistoryAsync(skuType, result -> {
            if (!e.isDisposed()) {
                e.onSuccess(new PurchasesResponse(result.getPurchasesList(), result.getResponseCode()));
            }
        });
    }
}
