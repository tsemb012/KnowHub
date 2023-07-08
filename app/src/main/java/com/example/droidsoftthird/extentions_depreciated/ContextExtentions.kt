package com.example.droidsoftthird.extentions_depreciated

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

fun Context.createLocalizedContext(locale: Locale): Context {
    val res = resources
    val config = Configuration(res.configuration)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.setLocales(LocaleList(locale))
    } else {
        config.setLocale(locale)
    }
    return createConfigurationContext(config)
}

fun getEnglishContext(baseContext: Context): Context {
    val currentLocale = Locale.ENGLISH
    Locale.setDefault(currentLocale)
    val config = getLocalizedConfiguration(currentLocale)
    baseContext.createConfigurationContext(config)
    return baseContext
}

fun getLocalizedConfiguration(locale: Locale): Configuration {
    val config = Configuration()
    return config.apply {
        config.setLayoutDirection(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
        } else {
            config.setLocale(locale)
        }
    }
}
