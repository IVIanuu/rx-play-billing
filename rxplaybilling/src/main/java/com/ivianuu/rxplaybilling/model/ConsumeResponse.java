package com.ivianuu.rxplaybilling.model;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;

/**
 * Represents a consume response
 */
public final class ConsumeResponse extends Response {

    private final String purchaseToken;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public ConsumeResponse(@Nullable String purchaseToken,
                           @BillingClient.BillingResponse int responseCode) {
        super(responseCode);
        this.purchaseToken = purchaseToken;
    }

    /**
     * The purchase token of this response
     */
    @Nullable public String purchaseToken() {
        return purchaseToken;
    }
}
