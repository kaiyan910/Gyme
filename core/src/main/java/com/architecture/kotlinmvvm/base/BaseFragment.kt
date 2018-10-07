package com.architecture.kotlinmvvm.base

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.architecture.kotlinmvvm.network.Resource
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var mBindings: T
    protected var mViewCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        Timber.d("<DEBUG> onCreate()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBindings = DataBindingUtil.inflate(inflater, layout(), container, false)
        mBindings.setLifecycleOwner(this)
        mViewCreated = true

        afterViews()

        Timber.d("<DEBUG> onCreateView()")

        return mBindings.root
    }

    override fun onDestroyView() {

        super.onDestroyView()

        Timber.d("<DEBUG> onDestroyView()")
        mViewCreated = false
    }

    override fun onPause() {

        super.onPause()
        Timber.d("<DEBUG> onPause()")
    }

    override fun onDestroy() {

        super.onDestroy()
        Timber.d("<DEBUG> onDestroy()")
    }

    protected open fun afterViews() {
        // do something after inflate view
    }

    protected fun <U> resolve(callback: (data: U) -> Unit): Observer<Resource<U>> = getParent().resolve(callback)

    protected abstract fun layout(): Int

    protected fun getParent() = activity as BaseActivity<*, *>
}