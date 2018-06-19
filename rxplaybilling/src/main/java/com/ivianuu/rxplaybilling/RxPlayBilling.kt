package com.ivianuu.rxplaybilling

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.Builder
import com.android.billingclient.api.BillingClient.newBuilder
import com.pixite.android.billingx.DebugBillingClient
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

/**
 * @author Manuel Wrage (IVIanuu)
 */
class RxPlayBilling {

    val isReady get() = billingClient.isReady

    val purchaseUpdates: Observable<PurchasesUpdatedResult>
        get() = _purchaseUpdates
    private val _purchaseUpdates = PublishSubject.create<PurchasesUpdatedResult>()

    private val billingClient: BillingClient

    private val purchaseUpdatesListener =
        PurchasesUpdatedListener { responseCode, purchases ->
            val billingResponse = responseCode.toBillingResponseEnum()
            if (billingResponse == BillingResponse.OK) {
                _purchaseUpdates.onNext(
                    PurchasesUpdatedResult.Success(
                        billingResponse,
                        purchases!!
                    )
                )
            } else {
                _purchaseUpdates.onNext(PurchasesUpdatedResult.Failure(billingResponse))
            }
        }

    private constructor(billingClientBuilder: Builder) {
        billingClient = billingClientBuilder
            .setListener(purchaseUpdatesListener)
            .build()
    }

    private constructor(debugBillingClientBuilder: DebugBillingClient.DebugBillingClientBuilder) {
        billingClient = debugBillingClientBuilder
            .setListener(purchaseUpdatesListener)
            .build()
    }

    fun connect(): Single<ConnectionResult> {
        if (billingClient.isReady) {
            return Single.just(ConnectionResult.Success(BillingResponse.OK))
        }

        return Single.create { e ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(responseCode: Int) {
                    if (e.isDisposed) return
                    val billingResponse = responseCode.toBillingResponseEnum()
                    val result = if (billingResponse == BillingResponse.OK) {
                        ConnectionResult.Success(billingResponse)
                    } else {
                        ConnectionResult.Failure(billingResponse)
                    }

                    e.onSuccess(result)
                }

                override fun onBillingServiceDisconnected() {
                }
            })
        }
    }

    fun disconnect() {
        billingClient.endConnection()
    }

    fun isFeatureSupported(feature: FeatureType): Int =
        billingClient.isFeatureSupported(feature.value)

    fun consume(purchaseToken: String): Single<ConsumeResult> {
        return Single.create { e ->
            billingClient.consumeAsync(purchaseToken) { responseCode, _purchaseToken ->
                if (e.isDisposed) return@consumeAsync
                val billingResponse = responseCode.toBillingResponseEnum()
                val result = if (billingResponse == BillingResponse.OK) {
                    ConsumeResult.Success(billingResponse, _purchaseToken)
                } else {
                    ConsumeResult.Failure(billingResponse)
                }

                e.onSuccess(result)
            }
        }
    }

    fun launchBillingFlow(activity: Activity, params: BillingFlowParams): Single<PurchaseResult> {
        val responseCode = billingClient.launchBillingFlow(activity, params)
        val billingResponse = responseCode.toBillingResponseEnum()
        val result = if (billingResponse == BillingResponse.OK) {
            PurchaseResult.Success(billingResponse)
        } else {
            PurchaseResult.Failure(billingResponse)
        }
        return Single.just(result)
    }

    fun queryPurchases(skuType: SkuType): Single<QueryPurchasesResult> {
        val realResult = billingClient.queryPurchases(skuType.value)

        val billingResponse = realResult.responseCode.toBillingResponseEnum()

        val result = if (billingResponse == BillingResponse.OK) {
            QueryPurchasesResult.Success(billingResponse, realResult.purchasesList)
        } else {
            QueryPurchasesResult.Failure(billingResponse)
        }

        return Single.just(result)
    }

    fun queryPurchasesNetwork(skuType: SkuType): Single<QueryPurchasesResult> {
        return Single.create { e ->
            billingClient.queryPurchaseHistoryAsync(skuType.value) { responseCode, purchasesList ->
                if (e.isDisposed) return@queryPurchaseHistoryAsync

                val billingResponse = responseCode.toBillingResponseEnum()

                val result = if (billingResponse == BillingResponse.OK) {
                    QueryPurchasesResult.Success(billingResponse, purchasesList!!)
                } else {
                    QueryPurchasesResult.Failure(billingResponse)
                }

                e.onSuccess(result)
            }
        }
    }

    fun querySkuDetails(params: SkuDetailsParams): Single<QuerySkuDetailsResult> {
        return Single.create { e ->
            billingClient.querySkuDetailsAsync(params) { responseCode, skuDetailsList ->
                if (e.isDisposed) return@querySkuDetailsAsync

                val billingResponse = responseCode.toBillingResponseEnum()

                val result = if (billingResponse == BillingResponse.OK) {
                    QuerySkuDetailsResult.Success(billingResponse, skuDetailsList!!)
                } else {
                    QuerySkuDetailsResult.Failure(billingResponse)
                }

                e.onSuccess(result)
            }
        }
    }

    companion object {

        fun create(context: Context) =
            create(newBuilder(context))

        fun create(billingClientBuilder: BillingClient.Builder) =
            RxPlayBilling(billingClientBuilder)

        fun create(billingClientBuilder: DebugBillingClient.DebugBillingClientBuilder) =
            RxPlayBilling(billingClientBuilder)

    }

}