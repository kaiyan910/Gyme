package com.architecture.kotlinmvvm.base

import android.arch.lifecycle.ViewModel
import android.os.Looper
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class BaseViewModel : ViewModel() {

    protected val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {

        super.onCleared()
        mCompositeDisposable.dispose()
    }

    protected fun <T> applySchedulers(): FlowableTransformer<T, T> {
        return FlowableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.from(Looper.getMainLooper(), true)) }
    }
}
