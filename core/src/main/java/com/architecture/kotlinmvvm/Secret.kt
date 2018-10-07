package com.architecture.kotlinmvvm

object Secret {

    init {
        System.loadLibrary("secret_lib")
    }

    external fun lock(): String
}