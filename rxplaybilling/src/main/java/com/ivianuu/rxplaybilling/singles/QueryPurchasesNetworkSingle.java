package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.ivianuu.rxplaybilling.model.PurchasesResponse;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * Query purchases network single
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class QueryPurchasesNetworkSingle extends BaseSingle<PurchasesResponse> {

    private final String skuType;

    private QueryPurchasesNetworkSingle(BillingClient billingClient, String skuType) {
        super(billingClient);
        this.skuType = skuType;
    }

    @CheckResult @NonNull
    public static Single<PurchasesResponse> create(@NonNull BillingClient billingClient, @NonNull String skuType) {
        return Single.create(new QueryPurchasesNetworkSingle(billingClient, skuType));
    }

    @Override
    public void subscribe(final SingleEmitter<PurchasesResponse> e) throws Exception {
        billingClient.queryPurchaseHistoryAsync(skuType, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(Purchase.PurchasesResult result) {
                if (!e.isDisposed()) {
                    e.onSuccess(new PurchasesResponse(result.getPurchasesList(), result.getResponseCode()));
                }
            }
        });
    }
}