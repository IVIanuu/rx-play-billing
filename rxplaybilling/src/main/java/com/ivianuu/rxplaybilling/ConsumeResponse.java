package com.ivianuu.rxplaybilling;

import android.support.annotation.NonNull;

import com.android.billingclient.api.BillingClient;

/**
 * Represents a consume response
 */
public class ConsumeResponse {

    private String purchaseToken;
    @BillingClient.BillingResponse private int responseCode;

    /**
     * Instantiates a consume response
     */
    public ConsumeResponse(@NonNull String purchaseToken, @BillingClient.BillingResponse int responseCode) {
        this.purchaseToken = purchaseToken;
        this.responseCode = responseCode;
    }

    /**
     * The purchase token from this response
     */
    @NonNull
    public String getPurchaseToken() {
        return purchaseToken;
    }

    /**
     * The response code
     */
    @BillingClient.BillingResponse
    public int getResponseCode() {
        return responseCode;
    }

}
