package com.architecture.kotlinmvvm.preferences

interface PreferencesRepository {

    fun getLastDownloadTime(): Long
    fun setLastDownloadTime(time: Long)

    fun getToken(): String
    fun setToken(token: String)

    fun cleanToken()
}