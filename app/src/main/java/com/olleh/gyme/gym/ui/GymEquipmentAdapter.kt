package com.olleh.gyme.gym.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.architecture.kotlinmvvm.base.BaseListAdapter
import com.architecture.kotlinmvvm.model.Equipment
import com.olleh.gyme.R
import com.olleh.gyme.databinding.ItemGymDetailsEquipmentBinding
import com.tooltip.Tooltip


class GymEquipmentAdapter : BaseListAdapter<Equipment>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return GymEquipmentViewHolder(ItemGymDetailsEquipmentBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        (viewHolder as GymEquipmentViewHolder).bind(data[position])
    }

    inner class GymEquipmentViewHolder(private val binding: ItemGymDetailsEquipmentBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(equipment: Equipment) {

            binding.equipment = equipment
            binding.executePendingBindings()

            binding.ivAccessible.setOnClickListener {

                val context = binding.root.context

                val message = if (equipment.shared) {
                    context.getString(R.string.gym_details_shared_with_disable)
                } else {
                    context.getString(R.string.gym_details_not_shared_with_disable)
                }

                Tooltip
                        .Builder(binding.ivAccessible)
                        .setText(message)
                        .setGravity(Gravity.START)
                        .setCornerRadius(R.dimen.spacing_small)
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
                        .setCancelable(true)
                        .show()
            }
        }
    }
}