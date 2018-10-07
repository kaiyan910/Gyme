package com.architecture.kotlinmvvm.manager

import android.arch.lifecycle.LifecycleObserver

interface DialogManager: LifecycleObserver {

    fun showDoubleButtonsDialog(title: String, content: String, positive: String, negative: String,
                                onPositive: () -> Unit, onNegative: () -> Unit)

    fun showDoubleButtonsDialog(title: Int, content: Int, positive: Int, negative: Int, onPositive: () -> Unit,
                                onNegative: () -> Unit)

    fun showSingleButtonDialog(title: String, content: String, positive: String, onPositive: () -> Unit)
    fun showSingleButtonDialog(title: Int, content: Int, positive: Int, onPositive: () -> Unit)

    fun showDoNothingDialog(title: String, content: String, positive: String)
    fun showDoNothingDialog(title: Int, content: Int, positive: Int)
}
