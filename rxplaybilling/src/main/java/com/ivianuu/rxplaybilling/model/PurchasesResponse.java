package com.ivianuu.rxplaybilling.model;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import java.util.List;

/**
 * Represents a purchase update
 */
public final class PurchasesResponse extends Response {

    private final List<Purchase> purchases;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public PurchasesResponse(@Nullable List<Purchase> purchases,
                             @BillingClient.BillingResponse int responseCode) {
        super(responseCode);
        this.purchases = purchases;
    }

    /**
     * Returns the purchases
     */
    @Nullable public List<Purchase> purchases() {
        return purchases;
    }
}
