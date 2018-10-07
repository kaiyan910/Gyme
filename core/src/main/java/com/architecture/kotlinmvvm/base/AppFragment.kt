package com.architecture.kotlinmvvm.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle

abstract class AppFragment<T : ViewDataBinding, M : AppViewModel> : BaseFragment<T>() {

    protected lateinit var mViewModel: M

    private var mRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, viewModelFactory()).get(viewModel())
    }

    override fun afterViews() {

        super.afterViews()
        observe()
        checkIfRequestNeeded()
    }

    override fun onDestroyView() {

        super.onDestroyView()
        mRequested = false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)
        checkIfRequestNeeded()
    }

    protected open fun observe() {

    }

    protected open fun request() {

    }

    private fun checkIfRequestNeeded() {

        if (userVisibleHint && !mRequested && mViewCreated) {
            mRequested = true
            request()
        }
    }

    protected abstract fun viewModel(): Class<M>
    protected abstract fun viewModelFactory(): ViewModelProvider.Factory
}
