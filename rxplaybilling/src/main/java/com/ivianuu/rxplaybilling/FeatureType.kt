package com.ivianuu.rxplaybilling

import com.android.billingclient.api.BillingClient

enum class FeatureType(val value: String) {
    SUBSCRIPTIONS(BillingClient.FeatureType.SUBSCRIPTIONS),
    SUBSCRIPTIONS_UPDATE(BillingClient.FeatureType.SUBSCRIPTIONS_UPDATE),
    IN_APP_ITEMS_ON_VR(BillingClient.FeatureType.IN_APP_ITEMS_ON_VR),
    SUBSCRIPTIONS_ON_VR(BillingClient.FeatureType.SUBSCRIPTIONS_ON_VR)
}

fun String.toFeatureType(): FeatureType =
    FeatureType.values()
        .firstOrNull { it.value == this }
        ?: error("unknown feature type $this")