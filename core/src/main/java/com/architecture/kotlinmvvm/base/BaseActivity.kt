package com.architecture.kotlinmvvm.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.architecture.kotlinmvvm.network.ErrorState
import com.architecture.kotlinmvvm.network.Resource
import com.architecture.kotlinmvvm.utils.LocaleUtils
import dagger.android.AndroidInjection

abstract class BaseActivity<T : ViewDataBinding, M : BaseViewModel> : AppCompatActivity() {

    protected lateinit var mBindings: T
    protected lateinit var mViewModel: M

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mBindings = DataBindingUtil.setContentView(this, layout())
        mBindings.setLifecycleOwner(this)
        mViewModel = ViewModelProviders.of(this, viewModelFactory()).get(viewModel())
        afterViews()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase!!))
    }

    public fun <U> resolve(callback: (data: U) -> Unit): Observer<Resource<U>> {

        return Observer {

            it?.apply {

                when (status) {
                    Resource.Status.ERROR -> onError(errorState!!)
                    Resource.Status.SUCCESS -> callback(data!!)
                    Resource.Status.LOADING -> onLoadingResources()
                }
            }
        }
    }

    protected open fun afterViews() {
        observe()
    }

    protected open fun observe() {
        // observe something...
    }

    protected open fun onLoadingResources() {
        // do something when querying
    }

    protected open fun onError(errorState: ErrorState) {
        // do something when error occur
    }

    protected abstract fun layout(): Int
    protected abstract fun viewModel(): Class<M>
    protected abstract fun viewModelFactory(): ViewModelProvider.Factory
}