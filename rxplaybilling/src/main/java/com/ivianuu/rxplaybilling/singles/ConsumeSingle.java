package com.ivianuu.rxplaybilling.singles;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.android.billingclient.api.BillingClient;
import com.ivianuu.rxplaybilling.model.ConsumeResponse;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * Consume single
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class ConsumeSingle extends BaseSingle<ConsumeResponse> {

    private final String purchaseToken;

    private ConsumeSingle(BillingClient billingClient, String purchaseToken) {
        super(billingClient);
        this.purchaseToken = purchaseToken;
    }

    /**
     * Consumes the purchase and emits the response
     */
    @CheckResult @NonNull
    public static Single<ConsumeResponse> create(@NonNull BillingClient billingClient,
                                                 @NonNull String purchaseToken) {
        return Single.create(new ConsumeSingle(billingClient, purchaseToken));
    }

    @Override
    public void subscribe(final SingleEmitter<ConsumeResponse> e) throws Exception {
        billingClient.consumeAsync(purchaseToken, (purchaseToken, resultCode) -> {
            e.onSuccess(new ConsumeResponse(resultCode, purchaseToken));
        });
    }
}
