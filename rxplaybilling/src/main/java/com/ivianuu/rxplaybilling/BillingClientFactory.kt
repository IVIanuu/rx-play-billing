package com.ivianuu.rxplaybilling

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener

/**
 * Factory for billing clients
 */
interface BillingClientFactory {

    /**
     * Returns a new [BillingClient] instance
     */
    fun createBillingClient(
        context: Context,
        listener: PurchasesUpdatedListener
    ): BillingClient

}

/**
 * Default [BillingClientFactory]
 */
class DefaultBillingClientFactory : BillingClientFactory {

    override fun createBillingClient(
        context: Context,
        listener: PurchasesUpdatedListener
    ): BillingClient {
        return BillingClient.newBuilder(context)
            .setListener(listener)
            .build()
    }

}