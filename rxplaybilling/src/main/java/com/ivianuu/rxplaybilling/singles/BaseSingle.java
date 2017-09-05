package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.NonNull;

import com.android.billingclient.api.BillingClient;

import io.reactivex.SingleOnSubscribe;

/**
 * Base single
 */
abstract class BaseSingle<T> implements SingleOnSubscribe<T> {

    final BillingClient billingClient;

    BaseSingle(@NonNull BillingClient billingClient) {
        this.billingClient = billingClient;
    }
}
