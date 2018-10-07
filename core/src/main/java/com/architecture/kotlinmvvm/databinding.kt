package com.architecture.kotlinmvvm

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.support.design.button.MaterialButton
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView

@BindingAdapter("bind:tint")
fun setImageViewTint(view: ImageView, color: Int) {
    DrawableCompat.setTint(view.drawable, color)
}


@BindingAdapter("bind:mbBackgroundTint")
fun setMaterialButtonBargroundTint(view: MaterialButton, color: Int) {

    view.backgroundTintList = ColorStateList.valueOf(color)
}
