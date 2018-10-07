package com.architecture.kotlinmvvm

import android.content.Context

fun Context.getResourceByName(name: String): String {

    return try {
        val resource = resources.getIdentifier(name, "string", packageName)
        getString(resource)
    } catch (e: Exception) {
        getString(R.string.error_undefined)
    }
}