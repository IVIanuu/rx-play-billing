package com.ivianuu.rxplaybilling;

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

    public PurchaseUpdate(@NonNull List<Purchase> purchases, @BillingClient.BillingResponse int responseCode) {
        this.purchases = purchases;
        this.responseCode = responseCode;
    }

    @NonNull
    public List<Purchase> getPurchases() {
        return purchases;
    }

    @BillingClient.BillingResponse
    public int getResponseCode() {
        return responseCode;
    }
}
