package com.ivianuu.rxplaybilling.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetailsParams;
import com.ivianuu.rxplaybilling.RxPlayBilling;
import com.ivianuu.rxplaybilling.model.Response;
import com.ivianuu.rxplaybilling.model.SkuDetailsResponse;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private RxPlayBilling rxPlayBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxPlayBilling = RxPlayBilling.create(this);

        rxPlayBilling
                .isFeatureSupported(BillingClient.FeatureType.IN_APP_ITEMS_ON_VR)
                .filter(Response::success)
                .flatMapSingle(__ -> {
                    BillingFlowParams params = BillingFlowParams.newBuilder()
                            .setType(BillingClient.FeatureType.IN_APP_ITEMS_ON_VR)
                            .setSku("")
                            .build();
                    return rxPlayBilling.launchBillingFlow(MainActivity.this, params);
                })
                .subscribe(response -> {
                    if (response.success()) {
                        Log.d("testtt", "success");
                    } else {
                        Log.d("testtt", "no success " + response.responseCode());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxPlayBilling.release();
    }
}
