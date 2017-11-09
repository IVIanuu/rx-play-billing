package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetailsParams;
import com.ivianuu.rxplaybilling.model.SkuDetailsResponse;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * Query sku details single
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class QuerySkuDetailsSingle extends BaseSingle<SkuDetailsResponse> {

    private final SkuDetailsParams skuDetailsParams;

    private QuerySkuDetailsSingle(BillingClient billingClient, SkuDetailsParams skuDetailsParams) {
        super(billingClient);
        this.skuDetailsParams = skuDetailsParams;
    }

    /**
     * Queries the sku details and emits the response
     */
    @CheckResult @NonNull
    public static Single<SkuDetailsResponse> create(@NonNull BillingClient billingClient,
                                                   @NonNull SkuDetailsParams skuDetailsParams) {
        return Single.create(new QuerySkuDetailsSingle(billingClient, skuDetailsParams));
    }

    @Override
    public void subscribe(final SingleEmitter<SkuDetailsResponse> e) throws Exception {
        billingClient.querySkuDetailsAsync(skuDetailsParams, (responseCode, skuDetailsList) -> {
            e.onSuccess(new SkuDetailsResponse(responseCode, skuDetailsList));
        });
    }
}
