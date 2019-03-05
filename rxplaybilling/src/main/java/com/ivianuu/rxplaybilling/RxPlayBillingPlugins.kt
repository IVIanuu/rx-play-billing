package com.ivianuu.rxplaybilling

/**
 * Global configurations
 */
object RxPlayBillingPlugins

private var _defaultBillingClientFactory: BillingClientFactory = DefaultBillingClientFactory()

/**
 * The billing client factory to use by default
 */
var RxPlayBillingPlugins.defaultBillingClientFactory: BillingClientFactory
    get() = _defaultBillingClientFactory
    set(value) {
        _defaultBillingClientFactory = value
    }