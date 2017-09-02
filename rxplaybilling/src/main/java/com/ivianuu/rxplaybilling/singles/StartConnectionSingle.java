package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Start connection single
 */
@RestrictTo(RestrictTo.Scope.GROUP_ID)
public final class StartConnectionSingle implements SingleOnSubscribe<Integer> {

    private BillingClient billingClient;

    private StartConnectionSingle(BillingClient billingClient) {
        this.billingClient = billingClient;
    }

    @CheckResult @NonNull
    public static Single<Integer> create(@NonNull BillingClient billingClient) {
        return Single.create(new StartConnectionSingle(billingClient));
    }

    @Override
    public void subscribe(final SingleEmitter<Integer> e) throws Exception {
        // connect
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int resultCode) {
                if (!e.isDisposed()) {
                    e.onSuccess(resultCode);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                if (!e.isDisposed()) {
                    e.onError(new IllegalStateException(String.valueOf(BillingClient.BillingResponse.SERVICE_DISCONNECTED)));
                }
            }
        });
    }
}
