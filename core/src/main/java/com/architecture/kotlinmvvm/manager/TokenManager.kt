package com.architecture.kotlinmvvm.manager

import com.architecture.kotlinmvvm.model.Token

interface TokenManager<T : Token> {

    fun save(token: T)
    fun get(): T

    fun clean()
}