package com.ivianuu.rxplaybilling;

import com.android.billingclient.api.BillingClient;

/**
 * @author Manuel Wrage (IVIanuu)
 */
public class BillingException extends RuntimeException {

    private int resultCode;

    public BillingException(@BillingClient.BillingResponse int resultCode) {
        this.resultCode = resultCode;
        printStackTrace();
    }

    public int getResultCode() {
        return resultCode;
    }
}
