package com.olleh.gyme.splashing.ui

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import com.architecture.kotlinmvvm.base.AppActivity
import com.olleh.gyme.R
import com.olleh.gyme.databinding.ActivitySplashingBinding
import com.olleh.gyme.main.ui.MainActivity
import com.olleh.gyme.splashing.viewModel.SplashingViewModel
import timber.log.Timber
import javax.inject.Inject

class SplashingActivity : AppActivity<ActivitySplashingBinding, SplashingViewModel>() {

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory

    override fun layout(): Int = R.layout.activity_splashing

    override fun viewModel(): Class<SplashingViewModel> = SplashingViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory = mViewModelProvider

    override fun afterViews() {

        super.afterViews()

        mViewModel.download()
    }

    override fun observe() {

        super.observe()

        mViewModel
            .downloadState
            .observe(this, resolve {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            })
    }
}