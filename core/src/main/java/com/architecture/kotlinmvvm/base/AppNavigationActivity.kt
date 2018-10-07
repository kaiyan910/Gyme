package com.architecture.kotlinmvvm.base

import android.databinding.ViewDataBinding
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.architecture.kotlinmvvm.R

abstract class AppNavigationActivity<T : ViewDataBinding, M : AppViewModel> : AppToolbarActivity<T, M>() {

    lateinit var mActionBarDrawerToggle: ActionBarDrawerToggle

    override fun setupNavigation() {

        mActionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout(), toolbar(), R.string.drawer_open,
                                                       R.string.drawer_hide)
        mActionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            if (mActionBarDrawerToggle.isDrawerIndicatorEnabled) {
                drawerLayout().openDrawer(navigationView())
            } else {
                onBackPressed()
            }
        }
        mActionBarDrawerToggle.isDrawerIndicatorEnabled = true
        mActionBarDrawerToggle.syncState()

        drawerLayout().addDrawerListener(mActionBarDrawerToggle)
    }

    // get navigation related UIs
    protected abstract fun drawerLayout(): DrawerLayout

    protected abstract fun navigationView(): NavigationView
}