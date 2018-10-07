package com.olleh.gyme.utils

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object MapUtils {

    fun createIconFromVector(context: Context, @DrawableRes resId: Int): BitmapDescriptor {

        val vectorDrawable = android.support.v4.content.ContextCompat.getDrawable(context, resId)

        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)

        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                                         vectorDrawable.intrinsicHeight,
                                         android.graphics.Bitmap.Config.ARGB_8888)

        val canvas = android.graphics.Canvas(bitmap)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}