package com.olleh.gyme.equipment.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.architecture.kotlinmvvm.base.BaseListAdapter
import com.architecture.kotlinmvvm.model.Equipment
import com.olleh.gyme.databinding.ItemEquipmentBinding

class EquipmentListAdapter : BaseListAdapter<Equipment>() {

    val selectedEquipment = mutableListOf<String>()
    var onEquipmentCountChangeListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return EquipmentViewHolder(ItemEquipmentBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        super.onBindViewHolder(viewHolder, position)
        (viewHolder as EquipmentViewHolder).bind(data[position], selectedEquipment.contains(data[position].id)) { check, id ->

            if (check) {
                selectedEquipment.add(id)
            } else {
                selectedEquipment.remove(id)
            }

            onEquipmentCountChangeListener?.invoke(selectedEquipment.size)
        }
    }

    fun reset() {

        selectedEquipment.clear()
        onEquipmentCountChangeListener?.invoke(0)
        notifyDataSetChanged()
    }

    inner class EquipmentViewHolder(private val binding: ItemEquipmentBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Equipment, check: Boolean, onCheckCallback: (Boolean, String) -> Unit) {

            binding
                    .cbSelected
                    .setOnCheckedChangeListener(null)

            binding
                    .cbSelected
                    .isChecked = check

            binding
                    .cbSelected
                    .setOnCheckedChangeListener { _, b ->

                        onCheckCallback.invoke(b, item.id)
                    }

            binding.equipment = item
            binding.executePendingBindings()
        }
    }
}