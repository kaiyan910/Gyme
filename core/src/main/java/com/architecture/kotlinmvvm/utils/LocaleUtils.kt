package com.architecture.kotlinmvvm.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import com.architecture.kotlinmvvm.preferences.PrefsKey
import timber.log.Timber
import java.util.Locale

object LocaleUtils {

    fun onAttach(context: Context): Context = changeLanguage(context, getLocale(context))

    public fun changeLanguage(context: Context, locale: Locale): Context {

        Timber.d("<DEBUG> Locale=%s", locale.toString())

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        preferences.edit()
                .putString(PrefsKey.LOCALE, locale.toLanguageTag())
                .apply()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale)
        } else {
            updateResourcesLegacy(context, locale)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, locale: Locale): Context {

        val resources = context.resources
        val configuration = resources.configuration

        configuration.locales = LocaleList(locale)

        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLegacy(context: Context, locale: Locale): Context {

        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }

    public fun getLocale(context: Context): Locale {

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val currentLocale = preferences.getString(PrefsKey.LOCALE, "")

        return when {

            currentLocale.isEmpty() -> { // ok it is the first time app open, we need to check the default locale

                // get default locale
                val locale = Locale.getDefault()
                // if somehow cannot get the tag return the english tag, but should not happen
                val supportedLocale = ensureUserLanguageSupported(locale)
                // save the language
                preferences.edit()
                        .putString(PrefsKey.LOCALE, supportedLocale.toLanguageTag())
                        .apply()

                supportedLocale

            }
            currentLocale == "en" -> Locale.ENGLISH
            else -> Locale.CHINESE
        }
    }

    private fun ensureUserLanguageSupported(default: Locale?): Locale {

        return if (default == null || default.language == "en" || default.language != "zh") {
            Locale.ENGLISH
        } else {
            Locale.TRADITIONAL_CHINESE
        }
    }
}