package com.olleh.gyme.gym.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.architecture.kotlinmvvm.base.BaseListAdapter
import com.architecture.kotlinmvvm.model.Gym
import com.olleh.gyme.databinding.ItemGymDetailsInfoBinding

class GymInfoAdapter : BaseListAdapter<Gym>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return GymInfoViewHolder(ItemGymDetailsInfoBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        (viewHolder as GymInfoViewHolder).bind(data[position])
    }

    inner class GymInfoViewHolder(private val binding: ItemGymDetailsInfoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(gym: Gym?) {

            binding.gym = gym
            binding.executePendingBindings()
        }
    }
}