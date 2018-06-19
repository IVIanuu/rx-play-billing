package com.ivianuu.rxplaybilling.ext

import com.android.billingclient.api.BillingFlowParams
import com.ivianuu.rxplaybilling.SkuType

fun BillingFlowParams.Builder.setType(skuType: SkuType) =
    setType(skuType.value)