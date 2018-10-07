package com.architecture.kotlinmvvm.network

import com.blankj.utilcode.util.NetworkUtils
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.sql.SQLException

class BasicErrorHandler : ErrorHandler {

    var errorHook: ((ErrorState) -> Unit)? = null

    override fun extractErrorState(throwable: Throwable): ErrorState = when {

        throwable is HttpException -> { // common error from server
            val responseBody = throwable.response().errorBody()
            extractFromBody(throwable.response().code(), responseBody)
        }
        throwable is SQLException -> { // database related error
            createState(HttpCode.APP, ErrorCode.SQL_ERROR)
        }
        throwable is SocketTimeoutException -> { // connection error
            createState(HttpCode.GATEWAY_TIMEOUT, ErrorCode.SERVER_DOWN)
        }
        throwable is IOException && !NetworkUtils.isConnected() -> { // no network
            createState(HttpCode.NO_CONNECTION, ErrorCode.NO_NETWORK)
        }
        throwable is IOException && NetworkUtils.isConnected() -> { // json parse error
            createState(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.RESPONSE_JSON_ERROR)
        }
        else -> { // unexpected error
            createState(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_SERVER_ERROR)
        }
    }

    private fun extractFromBody(httpCode: Int, responseBody: ResponseBody?): ErrorState {

        return try {

            // if no specific error and common error found, handle it according to the http code
            when (httpCode) {
                HttpCode.BAD_REQUEST -> createState(HttpCode.BAD_REQUEST, ErrorCode.INVALID_PARAMETERS)
                HttpCode.UNAUTHORIZED -> createState(HttpCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED)
                HttpCode.NOT_FOUND -> createState(HttpCode.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND)
                HttpCode.FORBIDDEN -> createState(HttpCode.FORBIDDEN, ErrorCode.FORBIDDEN)
                HttpCode.GATEWAY_TIMEOUT -> createState(HttpCode.GATEWAY_TIMEOUT, ErrorCode.SERVER_DOWN)
                else -> createState(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_SERVER_ERROR)
            }

        } catch (e: Exception) {

            ErrorState(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.RESPONSE_JSON_ERROR)
        }
    }

    private fun createState(httpCode: Int, errorCode: String): ErrorState {

        val state = ErrorState(httpCode, errorCode)
        errorHook?.invoke(state)

        return state
    }
}