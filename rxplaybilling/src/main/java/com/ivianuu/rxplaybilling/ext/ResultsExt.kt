@file:JvmName("ResultsKt")

package com.ivianuu.rxplaybilling.ext

import com.ivianuu.rxplaybilling.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@JvmName("filterConnectionResultSuccess")
fun Flowable<ConnectionResult>.filterSuccess() =
    ofType(ConnectionResult.Success::class.java)

@JvmName("filterConnectionResultSuccess")
fun Maybe<ConnectionResult>.filterSuccess() =
    ofType(ConnectionResult.Success::class.java)

@JvmName("filterConnectionResultSuccess")
fun Observable<ConnectionResult>.filterSuccess() =
    ofType(ConnectionResult.Success::class.java)

@JvmName("filterConnectionResultSuccess")
fun Single<ConnectionResult>.filterSuccess() =
    toMaybe().filterSuccess()

@JvmName("filterPurchaseResultSuccess")
fun Flowable<PurchaseResult>.filterSuccess() =
    ofType(PurchaseResult.Success::class.java)

@JvmName("filterPurchaseResultSuccess")
fun Maybe<PurchaseResult>.filterSuccess() =
    ofType(PurchaseResult.Success::class.java)

@JvmName("filterPurchaseResultSuccess")
fun Observable<PurchaseResult>.filterSuccess() =
    ofType(PurchaseResult.Success::class.java)

@JvmName("filterPurchaseResultSuccess")
fun Single<PurchaseResult>.filterSuccess() =
    toMaybe().filterSuccess()

@JvmName("filterConsumeResultSuccess")
fun Flowable<ConsumeResult>.filterSuccess() =
    ofType(ConsumeResult.Success::class.java)

@JvmName("filterConsumeResultSuccess")
fun Maybe<ConsumeResult>.filterSuccess() =
    ofType(ConsumeResult.Success::class.java)

@JvmName("filterConsumeResultSuccess")
fun Observable<ConsumeResult>.filterSuccess() =
    ofType(ConsumeResult.Success::class.java)

@JvmName("filterConsumeResultSuccess")
fun Single<ConsumeResult>.filterSuccess() =
    toMaybe().filterSuccess()

@JvmName("filterPurchasesUpdatedResultSuccess")
fun Flowable<PurchasesUpdatedResult>.filterSuccess() =
    ofType(PurchasesUpdatedResult.Success::class.java)

@JvmName("filterPurchasesUpdatedResultSuccess")
fun Maybe<PurchasesUpdatedResult>.filterSuccess() =
    ofType(PurchasesUpdatedResult.Success::class.java)

@JvmName("filterPurchasesUpdatedResultSuccess")
fun Observable<PurchasesUpdatedResult>.filterSuccess() =
    ofType(PurchasesUpdatedResult.Success::class.java)

@JvmName("filterPurchasesUpdatedResultSuccess")
fun Single<PurchasesUpdatedResult>.filterSuccess() =
    toMaybe().filterSuccess()

@JvmName("filterQueryPurchasesResultSuccess")
fun Flowable<QueryPurchasesResult>.filterSuccess() =
    ofType(QueryPurchasesResult.Success::class.java)

@JvmName("filterQueryPurchasesResultSuccess")
fun Maybe<QueryPurchasesResult>.filterSuccess() =
    ofType(QueryPurchasesResult.Success::class.java)

@JvmName("filterQueryPurchasesResultSuccess")
fun Observable<QueryPurchasesResult>.filterSuccess() =
    ofType(QueryPurchasesResult.Success::class.java)

@JvmName("filterQueryPurchasesResultSuccess")
fun Single<QueryPurchasesResult>.filterSuccess() =
    toMaybe().filterSuccess()

@JvmName("filterQuerySkuDetailsResultSuccess")
fun Flowable<QuerySkuDetailsResult>.filterSuccess() =
    ofType(QuerySkuDetailsResult.Success::class.java)

@JvmName("filterQuerySkuDetailsResultSuccess")
fun Maybe<QuerySkuDetailsResult>.filterSuccess() =
    ofType(QuerySkuDetailsResult.Success::class.java)

@JvmName("filterQuerySkuDetailsResultSuccess")
fun Observable<QuerySkuDetailsResult>.filterSuccess() =
    ofType(QuerySkuDetailsResult.Success::class.java)

@JvmName("filterQuerySkuDetailsResultSuccess")
fun Single<QuerySkuDetailsResult>.filterSuccess() =
    toMaybe().filterSuccess()
