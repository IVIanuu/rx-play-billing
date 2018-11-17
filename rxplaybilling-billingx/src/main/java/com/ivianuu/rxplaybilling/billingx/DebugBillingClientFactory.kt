package com.ivianuu.rxplaybilling.billingx

import android.content.Context
import com.android.billingclient.api.PurchasesUpdatedListener
import com.ivianuu.rxplaybilling.BillingClientFactory
import com.pixite.android.billingx.DebugBillingClient

/**
 * Debug billing client factory
 */
class DebugBillingClientFactory : BillingClientFactory {
    override fun createBillingClient(
        context: Context,
        listener: PurchasesUpdatedListener
    ) = DebugBillingClient.newBuilder(context)
        .setListener(listener)
        .build()
}