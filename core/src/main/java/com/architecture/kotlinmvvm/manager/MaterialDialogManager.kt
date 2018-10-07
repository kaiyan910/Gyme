package com.architecture.kotlinmvvm.manager

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import java.lang.ref.WeakReference

class MaterialDialogManager(context: Context) : DialogManager {

    private var mShowing = false
    private val mContext: WeakReference<Context> = WeakReference(context)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {

        mContext.clear()
    }

    override fun showDoubleButtonsDialog(title: String, content: String, positive: String, negative: String,
                                         onPositive: () -> Unit, onNegative: () -> Unit) {

        if (mShowing) return

        mShowing = true

        MaterialDialog
                .Builder(mContext.get()!!)
                .apply {
                    title(title)
                    content(content)
                    positiveText(positive)
                    negativeText(negative)
                    cancelable(false)
                    onPositive { dialog, _ ->
                        onPositive.invoke()
                        dialog.dismiss()
                        mShowing = false
                    }
                    onNegative { dialog, _ ->
                        onNegative.invoke()
                        dialog.dismiss()
                        mShowing = false
                    }
                }
                .show()
    }

    override fun showDoubleButtonsDialog(title: Int, content: Int, positive: Int, negative: Int, onPositive: () -> Unit,
                                         onNegative: () -> Unit) {

        showDoubleButtonsDialog(
                mContext.get()!!.getString(title),
                mContext.get()!!.getString(content),
                mContext.get()!!.getString(positive),
                mContext.get()!!.getString(negative),
                onPositive,
                onNegative)
    }

    override fun showSingleButtonDialog(title: String, content: String, positive: String, onPositive: () -> Unit) {

        if (mShowing) return

        mShowing = true

        MaterialDialog
                .Builder(mContext.get()!!)
                .apply {
                    title(title)
                    content(content)
                    positiveText(positive)
                    cancelable(false)
                    onPositive { dialog, _ ->
                        onPositive.invoke()
                        dialog.dismiss()
                        mShowing = false
                    }
                }
                .show()
    }

    override fun showSingleButtonDialog(title: Int, content: Int, positive: Int, onPositive: () -> Unit) {

        showSingleButtonDialog(
                mContext.get()!!.getString(title),
                mContext.get()!!.getString(content),
                mContext.get()!!.getString(positive),
                onPositive)
    }

    override fun showDoNothingDialog(title: String, content: String, positive: String) {

        if (mShowing) return

        mShowing = true

        MaterialDialog
                .Builder(mContext.get()!!)
                .apply {
                    title(title)
                    content(content)
                    positiveText(positive)
                    cancelable(false)
                    onPositive { dialog, _ ->
                        mShowing = false
                        dialog.dismiss()
                    }
                }
                .show()
    }

    override fun showDoNothingDialog(title: Int, content: Int, positive: Int) {

        showDoNothingDialog(
                mContext.get()!!.getString(title),
                mContext.get()!!.getString(content),
                mContext.get()!!.getString(positive)
        )
    }
}