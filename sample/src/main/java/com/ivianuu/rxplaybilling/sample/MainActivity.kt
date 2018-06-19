package com.ivianuu.rxplaybilling.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.ivianuu.rxplaybilling.*
import com.ivianuu.rxplaybilling.ext.filterSuccess
import com.ivianuu.rxplaybilling.ext.setType
import com.pixite.android.billingx.BillingStore
import com.pixite.android.billingx.DebugBillingClient
import com.pixite.android.billingx.SkuDetailsBuilder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var rxPlayBilling: RxPlayBilling

    private val disposables = CompositeDisposable()

    private val consumed = PublishSubject.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rxPlayBilling = if (BuildConfig.DEBUG) {
            RxPlayBilling.create(DebugBillingClient.newBuilder(this))
        } else {
            RxPlayBilling.create(BillingClient.newBuilder(this))
        }

        setupSkus()

        rxPlayBilling.connect()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                if (it is ConnectionResult.Success) {
                    onConnected()
                } else {
                    toast("Failed to connect")
                }
            })
            .addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        rxPlayBilling.disconnect()
        disposables.clear()
    }

    private fun onConnected() {
        consumed
            .startWith(Unit)
            .switchMap { rxPlayBilling.observePurchaseState(SkuType.IN_APP, SKU_TEST) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                consume.isEnabled = it
                purchase.isEnabled = !it
            }
            .addTo(disposables)

        consume.clicks()
            .flatMapMaybe {
                rxPlayBilling.queryPurchases(SkuType.IN_APP)
                    .filterSuccess()
                    .map { it.purchases.firstOrNull { it.sku == SKU_TEST }?.purchaseToken ?: "" }
                    .filter(String::isNotEmpty)
            }
            .flatMapSingle {
                if (BuildConfig.DEBUG) {
                    // todo remove this when billingx implements this function
                    BillingStore.defaultStore(this).removePurchase(SKU_TEST)
                    Single.just(ConsumeResult.Success(BillingResponse.OK, "???"))
                        .doOnSuccess { consumed.onNext(Unit) }
                } else {
                    rxPlayBilling.consume(it)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it is ConsumeResult.Success) {
                    toast("Consumed purchase")
                } else {
                    toast("Failed to consume purchase ${it.billingResponse}")
                }
            }
            .addTo(disposables)

        purchase.clicks()
            .flatMapSingle {
                val params = BillingFlowParams.newBuilder()
                    .setType(SkuType.IN_APP)
                    .setSku(SKU_TEST)
                    .build()
                rxPlayBilling.launchBillingFlow(this, params)
            }
            .flatMapSingle {
                if (it is PurchaseResult.Success) {
                    rxPlayBilling.purchaseUpdates
                        .take(1)
                        .singleOrError()
                        .map {
                            if (it.billingResponse == BillingResponse.OK) {
                                PurchaseResult.Success(it.billingResponse)
                            } else {
                                PurchaseResult.Failure(it.billingResponse)
                            }
                        }
                } else {
                    Single.just(it)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it is PurchaseResult.Success) {
                    toast("Purchased")
                } else {
                    toast("Failed to purchase ${it.billingResponse}")
                }
            }
            .addTo(disposables)
    }

    private fun setupSkus() {
        if (!BuildConfig.DEBUG) return
        val billingStore = BillingStore.defaultStore(this)

        billingStore.addProduct(
            SkuDetailsBuilder(
                sku = SKU_TEST, type = BillingClient.SkuType.INAPP,
                price = "$3.99", priceAmountMicros = 3990000, priceCurrencyCode = "USD",
                title = "Test", description = "This is a test"
            ).build()
        )
    }

    private companion object {
        private const val SKU_TEST = "test"
    }
}
