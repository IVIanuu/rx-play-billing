package com.ivianuu.rxplaybilling

import com.android.billingclient.api.BillingFlowParams

fun BillingFlowParams.Builder.setType(skuType: SkuType): BillingFlowParams.Builder =
    setType(skuType.value)