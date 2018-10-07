package com.architecture.kotlinmvvm.manager

import io.reactivex.Flowable

interface DataManager {

    fun clean(): Flowable<Boolean>
    fun download(): Flowable<Boolean>
}