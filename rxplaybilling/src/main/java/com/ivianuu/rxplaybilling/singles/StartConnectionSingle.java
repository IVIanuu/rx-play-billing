package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.ivianuu.rxplaybilling.model.Response;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * Start connection single
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class StartConnectionSingle extends BaseSingle<Response> {

    private StartConnectionSingle(BillingClient billingClient) {
        super(billingClient);
    }

    @CheckResult @NonNull
    public static Single<Response> create(@NonNull BillingClient billingClient) {
        return Single.create(new StartConnectionSingle(billingClient));
    }

    @Override
    public void subscribe(final SingleEmitter<Response> e) throws Exception {
        // connect
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (!e.isDisposed()) {
                    e.onSuccess(new Response(responseCode));
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // ignore
            }
        });
    }
}
