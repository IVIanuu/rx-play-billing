package com.ivianuu.rxplaybilling.model;

import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;

/**
 * Wraps a response code
 */
public class Response {

    @BillingClient.BillingResponse
    private final int responseCode;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Response(@BillingClient.BillingResponse int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Returns the response code
     */
    @BillingClient.BillingResponse
    public int responseCode() {
        return responseCode;
    }

    /**
     * Returns whether the response code is success
     */
    public boolean success() {
        return responseCode == BillingClient.BillingResponse.OK;
    }
}
