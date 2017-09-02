package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.ConsumeResponseListener;
import com.ivianuu.rxplaybilling.model.ConsumeResponse;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Consume single
 */
@RestrictTo(RestrictTo.Scope.GROUP_ID)
public final class ConsumeSingle implements SingleOnSubscribe<ConsumeResponse> {

    private BillingClient billingClient;
    private String purchaseToken;

    private ConsumeSingle(BillingClient billingClient, String purchaseToken) {
        this.billingClient = billingClient;
        this.purchaseToken = purchaseToken;
    }

    @CheckResult @NonNull
    public static Single<ConsumeResponse> create(@NonNull BillingClient billingClient, @NonNull String purchaseToken) {
        return Single.create(new ConsumeSingle(billingClient, purchaseToken));
    }

    @Override
    public void subscribe(final SingleEmitter<ConsumeResponse> e) throws Exception {
        billingClient.consumeAsync(purchaseToken, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(String purchaseToken, int resultCode) {
                if (!e.isDisposed()) {
                    e.onSuccess(new ConsumeResponse(purchaseToken, resultCode));
                }
            }
        });
    }
}
