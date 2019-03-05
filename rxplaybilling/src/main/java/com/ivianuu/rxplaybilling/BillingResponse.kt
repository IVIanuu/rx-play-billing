package com.ivianuu.rxplaybilling

import com.android.billingclient.api.BillingClient

enum class BillingResponse(val value: Int) {
    FEATURE_NOT_SUPPORTED(BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED),
    SERVICE_DISCONNECTED(BillingClient.BillingResponse.SERVICE_DISCONNECTED),
    OK(BillingClient.BillingResponse.OK),
    USER_CANCELED(BillingClient.BillingResponse.USER_CANCELED),
    SERVICE_UNAVAILABLE(BillingClient.BillingResponse.SERVICE_UNAVAILABLE),
    BILLING_UNAVAILABLE(BillingClient.BillingResponse.BILLING_UNAVAILABLE),
    ITEM_UNAVAILABLE(BillingClient.BillingResponse.ITEM_UNAVAILABLE),
    DEVELOPER_ERROR(BillingClient.BillingResponse.DEVELOPER_ERROR),
    ERROR(BillingClient.BillingResponse.ERROR),
    ITEM_ALREADY_OWNED(BillingClient.BillingResponse.ITEM_ALREADY_OWNED),
    ITEM_NOT_OWNED(BillingClient.BillingResponse.ITEM_NOT_OWNED)
}

fun Int.toBillingResponse(): BillingResponse =
    BillingResponse.values()
        .firstOrNull { it.value == this }
        ?: error("unknown billing response $this")