package com.ivianuu.rxplaybilling

import com.android.billingclient.api.BillingClient

enum class SkuType(val value: String) {
    IN_APP(BillingClient.SkuType.INAPP),
    SUBS(BillingClient.SkuType.SUBS)
}

fun String.toSkuType(): SkuType =
    SkuType.values().firstOrNull { it.value == this }
        ?: error("unknown sku type $this")