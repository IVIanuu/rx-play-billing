package com.ivianuu.rxplaybilling

import com.android.billingclient.api.BillingClient

enum class SkuType(val value: String) {
    IN_APP(BillingClient.SkuType.INAPP),
    SUBS(BillingClient.SkuType.SUBS)
}

fun String.toSkuTypeEnum() =
    SkuType.values().firstOrNull { it.value == this }
            ?: throw IllegalArgumentException("unknown sku type $this")