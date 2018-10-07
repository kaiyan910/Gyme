package com.olleh.gyme.equipment.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.architecture.kotlinmvvm.base.BaseListAdapter
import com.architecture.kotlinmvvm.model.Gym
import com.olleh.gyme.databinding.ItemGymBinding
import com.olleh.gyme.databinding.ItemGymEmptyBinding

class EquipmentSearchResultGymListAdapter : BaseListAdapter<Gym>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if (itemType == 0) {
            EquipmentSearchResultEmptyViewHolder(ItemGymEmptyBinding.inflate(inflater, parent, false))
        } else {
            EquipmentSearchResultGymViewHolder(ItemGymBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        super.onBindViewHolder(viewHolder, position)

        if (viewHolder is EquipmentSearchResultGymViewHolder) {
            viewHolder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {

        return if (data.size == 0) {
            1
        } else {
            data.size
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (data.size == 0) {
            0
        } else {
            1
        }
    }

    inner class EquipmentSearchResultGymViewHolder(private val binding: ItemGymBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Gym) {

            binding.gym = item
            binding.executePendingBindings()
        }
    }

    inner class EquipmentSearchResultEmptyViewHolder(private val binding: ItemGymEmptyBinding)
        : RecyclerView.ViewHolder(binding.root) {

    }
}