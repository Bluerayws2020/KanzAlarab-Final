package com.blueray.fares

import android.app.Application
import com.blueray.fares.helpers.HelperUtils
import dagger.hilt.android.HiltAndroidApp

const val ONESIGNAL_APP_ID = "14a886e5-d580-4d7a-81ea-61184f75186a"

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        HelperUtils.setDefaultLanguage(this@AppClass, "ar")
        HelperUtils.setLang(this@AppClass, "ar")
//        // Enable verbose OneSignal logging to debug issues if needed.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//
//        // OneSignal Initialization
//
//        OneSignal.initWithContext(this)
//        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}