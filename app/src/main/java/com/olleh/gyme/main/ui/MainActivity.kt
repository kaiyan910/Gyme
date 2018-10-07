package com.olleh.gyme.main.ui

import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import com.architecture.kotlinmvvm.base.AppActivity
import com.olleh.gyme.R
import com.olleh.gyme.databinding.ActivityMainBinding
import com.olleh.gyme.equipment.ui.EquipmentListFragment
import com.olleh.gyme.main.viewModel.MainViewModel
import com.olleh.gyme.map.ui.MapFragment
import com.olleh.gyme.settings.ui.SettingsFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import android.os.Bundle



class MainActivity : AppActivity<ActivityMainBinding, MainViewModel>(), HasSupportFragmentInjector {

    companion object {

        const val STATE_FRAGMENT = "state_fragment"
    }

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory
    @Inject
    lateinit var mSupportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = mSupportFragmentInjector

    override fun layout(): Int = R.layout.activity_main

    override fun viewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory = mViewModelProvider

    override fun afterViews() {

        super.afterViews()

        switchFragment(mViewModel.currentFragmentId)

        mBindings
                .bottomNavigationView
                .setOnNavigationItemSelectedListener {

                    mViewModel.currentFragmentId = it.itemId
                    switchFragment(it.itemId)

                    true
                }
    }

    override fun onBackPressed() {

        val stackCount = supportFragmentManager.backStackEntryCount

        if (stackCount > 0) {

            supportFragmentManager.popBackStack()

        } else {

            super.onBackPressed()
        }
    }

    private fun switchFragment(id: Int) {

        when (id) {

            R.id.menu_map -> setupFragment(MapFragment.newInstance())
            R.id.menu_equipments -> setupFragment(EquipmentListFragment.newInstance())
            R.id.menu_settings -> setupFragment(SettingsFragment.newInstance())
            else -> {
            }
        }
    }

    private fun setupFragment(fragment: Fragment) {

        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, fragment)
                .commit()
    }
}
