package com.olleh.gyme.settings.ui

import android.arch.lifecycle.ViewModelProvider
import com.architecture.kotlinmvvm.base.AppFragment
import com.architecture.kotlinmvvm.utils.LocaleUtils
import com.olleh.gyme.R
import com.olleh.gyme.databinding.FragmentSettingsBinding
import com.olleh.gyme.settings.viewModel.SettingsViewModel
import java.util.Locale
import javax.inject.Inject

class SettingsFragment : AppFragment<FragmentSettingsBinding, SettingsViewModel>() {

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory

    override fun viewModel(): Class<SettingsViewModel> = SettingsViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory = mViewModelProvider

    override fun layout(): Int = R.layout.fragment_settings

    companion object {

        fun newInstance(): SettingsFragment = SettingsFragment()
    }


    override fun afterViews() {

        super.afterViews()
        mBindings.language = LocaleUtils.getLocale(context!!).language

        mBindings
                .mbChinese
                .setOnClickListener {

                    if (LocaleUtils.getLocale(context!!).language != "zh") {

                        mBindings.language = "zh"
                        LocaleUtils.changeLanguage(context!!, Locale.CHINESE)
                        activity?.recreate()
                    }
                }

        mBindings
                .mbEnglish
                .setOnClickListener {

                    if (LocaleUtils.getLocale(context!!).language != "en") {

                        mBindings.language = "en"
                        LocaleUtils.changeLanguage(context!!, Locale.ENGLISH)
                        activity?.recreate()
                    }
                }

        mBindings
                .mbUpdate
                .setOnClickListener {

                    mBindings.updating = true
                    mViewModel.download()
                }

    }

    override fun observe() {

        super.observe()

        mViewModel
                .lastUpdateTime
                .observe(this, resolve {

                    mBindings.updateTime = it
                })

        mViewModel
                .updatedTime
                .observe(this, resolve {

                    mBindings.updateTime = it

                    mBindings.updating = false
                })
    }
}