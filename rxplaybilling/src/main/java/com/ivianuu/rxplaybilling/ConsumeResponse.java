package com.ivianuu.rxplaybilling;

import android.support.annotation.NonNull;

import com.android.billingclient.api.BillingClient;

/**
 * Represents a consume response
 */
public class ConsumeResponse {

    private String purchaseToken;
    @BillingClient.BillingResponse private int responseCode;

    public ConsumeResponse(@NonNull String purchaseToken, @BillingClient.BillingResponse int responseCode) {
        this.purchaseToken = purchaseToken;
        this.responseCode = responseCode;
    }

    @NonNull
    public String getPurchaseToken() {
        return purchaseToken;
    }

    @BillingClient.BillingResponse
    public int getResponseCode() {
        return responseCode;
    }

}
