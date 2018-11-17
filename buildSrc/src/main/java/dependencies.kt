@file:Suppress("ClassName", "unused")

object Versions {
    // android
    const val compileSdk = 28
    const val minSdk = 16
    const val minSdkSample = 21
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "1.0"

    const val androidGradlePlugin = "3.2.1"

    const val billingX = "master"
    const val constraintLayout = "1.1.3"
    const val kotlin = "1.3.10"
    const val mavenGradlePlugin = "2.1"
    const val playBilling = "1.1"
    const val rxAndroid = "2.0.2"
    const val rxJava = "2.1.16"
    const val support = "28.0.0"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val billingX = "com.github.pixiteapps:billingx:${Versions.billingX}"

    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val mavenGradlePlugin =
        "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradlePlugin}"

    const val playBilling = "com.android.billingclient:billing:${Versions.playBilling}"

    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"

    const val supportAppCompat = "com.android.support:appcompat-v7:${Versions.support}"
}