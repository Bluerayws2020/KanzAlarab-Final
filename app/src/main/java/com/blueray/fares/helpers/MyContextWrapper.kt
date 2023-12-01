package com.blueray.fares.helpers

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

class MyContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, newLocale: Locale): ContextWrapper {
            var newContext = context

            val res = context.resources
            val configuration = Configuration(res.configuration)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)

                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
//                configuration.locales = localeList

                newContext = context.createConfigurationContext(configuration)

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(newLocale)
                newContext = context.createConfigurationContext(configuration)

            } else {
                configuration.locale = newLocale
                res.updateConfiguration(configuration, res.displayMetrics)
            }

            return MyContextWrapper(newContext)
        }
    }
}
