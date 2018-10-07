package com.architecture.kotlinmvvm.preferences

object PrefsKey {

    private const val PREFIX = "v1"

    const val TOKEN = "${PREFIX}_token"
    const val REFRESH_TOKEN = "${PREFIX}_refresh_token"
    const val LOCALE = "${PREFIX}_locale"
    const val LAST_DOWNLOAD_TIME = "${PREFIX}_last_download_time"
}