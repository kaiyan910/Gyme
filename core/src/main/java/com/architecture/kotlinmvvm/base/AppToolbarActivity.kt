package com.architecture.kotlinmvvm.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.Toolbar

abstract class AppToolbarActivity<T : ViewDataBinding, M : AppViewModel> : AppActivity<T, M>() {

    override fun afterViews() {

        super.afterViews()
        setupToolbar()
        setupNavigation()
    }

    private fun setupToolbar() {

        setSupportActionBar(toolbar())

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = title()
            subtitle = subTitle()
        }
    }

    protected open fun setupNavigation() {
        toolbar().setNavigationOnClickListener { _ -> onBackPressed() }
    }

    protected open fun subTitle(): String {
        return ""
    }

    protected abstract fun title(): String
    protected abstract fun toolbar(): Toolbar
}