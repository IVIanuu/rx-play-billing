package com.ivianuu.rxplaybilling.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.ivianuu.rxplaybilling.BillingResponse
import com.ivianuu.rxplaybilling.ConnectionResult
import com.ivianuu.rxplaybilling.ConsumeResult
import com.ivianuu.rxplaybilling.DefaultBillingClientFactory
import com.ivianuu.rxplaybilling.PurchaseResult
import com.ivianuu.rxplaybilling.QueryPurchasesResult
import com.ivianuu.rxplaybilling.RxPlayBilling
import com.ivianuu.rxplaybilling.RxPlayBillingPlugins
import com.ivianuu.rxplaybilling.SkuType
import com.ivianuu.rxplaybilling.defaultBillingClientFactory
import com.ivianuu.rxplaybilling.setType
import com.pixite.android.billingx.BillingStore
import com.pixite.android.billingx.SkuDetailsBuilder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.consume
import kotlinx.android.synthetic.main.activity_main.purchase

class MainActivity : AppCompatActivity() {

    private lateinit var rxPlayBilling: RxPlayBilling

    private val disposables = CompositeDisposable()

    private val consumed = PublishSubject.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPlayBillingPlugins.defaultBillingClientFactory = DefaultBillingClientFactory()

        rxPlayBilling = RxPlayBilling(this)

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
                    .filter { it is QueryPurchasesResult.Success }
                    .cast(QueryPurchasesResult.Success::class.java)
                    .map { it.purchases.firstOrNull { it.sku == SKU_TEST }?.purchaseToken ?: "" }
                    .filter(String::isNotEmpty)
            }
            .flatMapSingle {
                BillingStore.defaultStore(this).removePurchase(SKU_TEST)
                Single.just(ConsumeResult.Success(BillingResponse.OK, "???"))
                    .doOnSuccess { consumed.onNext(Unit) }
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
