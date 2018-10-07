package com.olleh.gyme.main.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.architecture.kotlinmvvm.model.Gym
import com.olleh.gyme.databinding.ViewInfoWindowBinding

class MapInfoWindow(context: Context) : LinearLayout(context) {

    private val mBinding: ViewInfoWindowBinding

    init {

        val inflater = LayoutInflater.from(context)
        mBinding = ViewInfoWindowBinding.inflate(inflater, this, true)

    }

    fun bind(room: Gym) {

        mBinding.gym = room
        mBinding.executePendingBindings()
    }
}