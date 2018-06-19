package com.ivianuu.rxplaybilling.sample

import android.content.Context
import android.view.View
import android.widget.Toast
import com.ivianuu.rxplaybilling.PurchasesUpdatedResult
import com.ivianuu.rxplaybilling.QueryPurchasesResult
import com.ivianuu.rxplaybilling.RxPlayBilling
import com.ivianuu.rxplaybilling.SkuType
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(disposables: CompositeDisposable): Disposable {
    disposables.add(this)
    return this
}

fun View.clicks() = Observable.create<Unit> { e ->
    setOnClickListener {
        if (!e.isDisposed) {
            e.onNext(Unit)
        }
    }

    e.setCancellable { setOnClickListener(null) }
}

fun RxPlayBilling.observePurchaseState(skuType: SkuType, sku: String): Observable<Boolean> {
    return purchaseUpdates
        .filter {
            if (it is PurchasesUpdatedResult.Success) {
                it.purchases.any { it.sku == sku }
            } else {
                false
            }
        }
        .map { Unit }
        .startWith(Unit)
        .flatMapSingle {
            queryPurchases(skuType)
                .map {
                    if (it is QueryPurchasesResult.Success) {
                        it.purchases.any { it.sku == sku }
                    } else {
                        false
                    }
                }
                .flatMap {
                    if (!it) {
                        queryPurchasesNetwork(skuType)
                            .map {
                                if (it is QueryPurchasesResult.Success) {
                                    it.purchases.any { it.sku == sku }
                                } else {
                                    false
                                }
                            }
                    } else {
                        Single.just(true)
                    }
                }
        }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
