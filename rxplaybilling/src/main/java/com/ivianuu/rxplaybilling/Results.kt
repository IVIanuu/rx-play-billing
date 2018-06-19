package com.ivianuu.rxplaybilling

import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

sealed class BillingResult {
    abstract val billingResponse: BillingResponse
}

sealed class ConnectionResult : BillingResult() {
    data class Success(override val billingResponse: BillingResponse) : ConnectionResult()

    data class Failure(override val billingResponse: BillingResponse) : ConnectionResult()
}

sealed class PurchasesUpdatedResult : BillingResult() {
    data class Success(
        override val billingResponse: BillingResponse,
        val purchases: List<Purchase>
    ) : PurchasesUpdatedResult()

    data class Failure(
        override val billingResponse: BillingResponse
    ) : PurchasesUpdatedResult()
}

sealed class ConsumeResult : BillingResult() {
    data class Success(
        override val billingResponse: BillingResponse,
        val outputToken: String
    ) : ConsumeResult()

    data class Failure(override val billingResponse: BillingResponse) : ConsumeResult()
}

sealed class PurchaseResult : BillingResult() {
    data class Success(
        override val billingResponse: BillingResponse
    ) : PurchaseResult()

    data class Failure(
        override val billingResponse: BillingResponse
    ) : PurchaseResult()
}

sealed class QueryPurchasesResult : BillingResult() {
    data class Success(
        override val billingResponse: BillingResponse,
        val purchases: List<Purchase>
    ) : QueryPurchasesResult()

    data class Failure(
        override val billingResponse: BillingResponse
    ) : QueryPurchasesResult()
}

sealed class QuerySkuDetailsResult : BillingResult() {
    data class Success(
        override val billingResponse: BillingResponse,
        val skuDetails: List<SkuDetails>
    ) : QuerySkuDetailsResult()

    data class Failure(
        override val billingResponse: BillingResponse
    ) : QuerySkuDetailsResult()
}