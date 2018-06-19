package com.ivianuu.rxplaybilling.ext

import com.android.billingclient.api.SkuDetailsParams
import com.ivianuu.rxplaybilling.SkuType

fun SkuDetailsParams.Builder.setType(skuType: SkuType) =
    setType(skuType.value)

fun SkuDetailsParams.Builder.setSkusList(skusList: Collection<String>) =
    setSkusList(skusList.toMutableList())

fun SkuDetailsParams.Builder.setSkusList(vararg skusList: String) =
    setSkusList(skusList.toMutableList())