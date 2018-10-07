package com.architecture.kotlinmvvm.base

import android.databinding.ViewDataBinding
import android.support.v4.app.DialogFragment
import android.R.attr.x
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.view.*
import dagger.android.support.AndroidSupportInjection

abstract class BaseDialogFragment<T : ViewDataBinding>(private val widthRatio: Double) : DialogFragment() {

    protected lateinit var mBindings: T

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBindings = DataBindingUtil.inflate(inflater, layout(), container, false)
        return mBindings.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null) {
            dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        afterViews()
    }

    override fun onResume() {

        val window = dialog.window
        val size = Point()

        if (window != null) {
            val display = window.windowManager.defaultDisplay
            display.getSize(size)
            window.setLayout((size.x * widthRatio).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER)
        }

        super.onResume()
    }

    protected fun afterViews() {

    }

    protected abstract fun layout(): Int
}