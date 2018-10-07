package com.architecture.kotlinmvvm.network

object HttpCode {

    const val NO_CONNECTION = -1
    const val APP = 888

    const val OK = 200
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val INTERNAL_SERVER_ERROR = 500
    const val BAD_GATEWAY = 502
    const val GATEWAY_TIMEOUT = 504
}