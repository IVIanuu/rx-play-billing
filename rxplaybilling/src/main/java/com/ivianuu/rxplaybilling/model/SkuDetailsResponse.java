package com.ivianuu.rxplaybilling.model;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;

import java.util.List;

/**
 * Sku details response
 */
public final class SkuDetailsResponse extends Response {

    private final List<SkuDetails> skuDetailsList;

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public SkuDetailsResponse(@BillingClient.BillingResponse int responseCode,
                              @Nullable List<SkuDetails> skuDetailsList) {
        super(responseCode);
        this.skuDetailsList = skuDetailsList;
    }

    /**
     * Returns the sku details list
     */
    @Nullable public List<SkuDetails> skuDetailsList() {
        return skuDetailsList;
    }
}
