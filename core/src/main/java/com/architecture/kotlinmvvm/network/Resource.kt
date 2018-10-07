package com.architecture.kotlinmvvm.network

class Resource<T> private constructor(
        val status: Status,
        val data: T?,
        val errorState: ErrorState?) {

    enum class Status {
        LOADING, SUCCESS, ERROR
    }

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: ErrorState): Resource<T> {
            return Resource(Status.ERROR, null, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}