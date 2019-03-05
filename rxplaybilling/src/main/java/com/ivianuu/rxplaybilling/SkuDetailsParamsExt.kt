package com.ivianuu.rxplaybilling

import com.android.billingclient.api.SkuDetailsParams

fun SkuDetailsParams.Builder.setType(skuType: SkuType) =
    setType(skuType.value)

fun SkuDetailsParams.Builder.setSkusList(skusList: Iterable<String>) =
    setSkusList(skusList.toMutableList())

fun SkuDetailsParams.Builder.setSkusList(vararg skusList: String) =
    setSkusList(skusList.toMutableList())