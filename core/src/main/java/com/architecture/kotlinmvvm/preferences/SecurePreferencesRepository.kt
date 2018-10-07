package com.architecture.kotlinmvvm.preferences

import android.content.SharedPreferences

class SecurePreferencesRepository(private val sharedPreferences: SharedPreferences) : PreferencesRepository {

    override fun getLastDownloadTime(): Long = sharedPreferences.getLong(PrefsKey.LAST_DOWNLOAD_TIME, 0L)

    override fun setLastDownloadTime(time: Long) {

        sharedPreferences.edit()
                .putLong(PrefsKey.LAST_DOWNLOAD_TIME, time)
                .apply()
    }

    override fun setToken(token: String) {

        sharedPreferences.edit()
                .putString(PrefsKey.TOKEN, token)
                .apply()
    }

    override fun getToken(): String = sharedPreferences.getString(PrefsKey.TOKEN, "")

    override fun cleanToken() {

        sharedPreferences.edit()
                .putString(PrefsKey.TOKEN, "")
                .putString(PrefsKey.REFRESH_TOKEN, "")
                .apply()
    }
}