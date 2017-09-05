package com.ivianuu.rxplaybilling.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.ivianuu.rxplaybilling.RxPlayBilling;
import com.ivianuu.rxplaybilling.model.Response;

import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity {

    private RxPlayBilling rxPlayBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxPlayBilling = RxPlayBilling.get(this);

        rxPlayBilling
                .isFeatureSupported(BillingClient.FeatureType.IN_APP_ITEMS_ON_VR)
                .filter(new Predicate<Response>() {
                    @Override
                    public boolean test(Response response) throws Exception {
                        return response.success();
                    }
                })
                .flatMapSingle(new Function<Response, SingleSource<Response>>() {
                    @Override
                    public SingleSource<Response> apply(Response __) throws Exception {
                        BillingFlowParams params = new BillingFlowParams.Builder()
                                .setType(BillingClient.FeatureType.IN_APP_ITEMS_ON_VR)
                                .setSku("")
                                .build();
                        return rxPlayBilling.launchBillingFlow(MainActivity.this, params);
                    }
                })
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        if (response.success()) {
                            Log.d("testtt", "success");
                        } else {
                            Log.d("testtt", "no success " + response.responseCode());
                        }
                    }
                });
    }

}
