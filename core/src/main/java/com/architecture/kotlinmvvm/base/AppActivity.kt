package com.architecture.kotlinmvvm.base

import android.databinding.ViewDataBinding
import com.architecture.kotlinmvvm.Constant
import com.architecture.kotlinmvvm.R
import com.architecture.kotlinmvvm.getResourceByName
import com.architecture.kotlinmvvm.manager.DialogManager
import com.architecture.kotlinmvvm.network.ErrorState
import javax.inject.Inject

abstract class AppActivity<T : ViewDataBinding, M : AppViewModel> : BaseActivity<T, M>() {

    @Inject
    lateinit var mDialogManager: DialogManager

    override fun afterViews() {

        super.afterViews()

        lifecycle.addObserver(mDialogManager)
    }

    override fun onError(errorState: ErrorState) {

        mDialogManager.showDoNothingDialog(
                getString(R.string.dialog_error_title),
                getResourceByName("${Constant.ERROR_PREFIX}${errorState.errorCode}"),
                getString(R.string.dialog_button_ok)
        )
    }
}