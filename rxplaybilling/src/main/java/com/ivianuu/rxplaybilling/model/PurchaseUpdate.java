package com.ivianuu.rxplaybilling.model;

import android.support.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import java.util.List;

/**
 * Represents a purchase update
 */
public class PurchaseUpdate {

    private List<Purchase> purchases;
    @BillingClient.BillingResponse private int responseCode;

    /**
     * Instantiates a purchase update
     */
    public PurchaseUpdate(@NonNull List<Purchase> purchases, @BillingClient.BillingResponse int responseCode) {
        this.purchases = purchases;
        this.responseCode = responseCode;
    }

    /**
     * The list of purchases of this response
     */
    @NonNull
    public List<Purchase> getPurchases() {
        return purchases;
    }

    /**
     * The response code of this response
     */
    @BillingClient.BillingResponse
    public int getResponseCode() {
        return responseCode;
    }
}
