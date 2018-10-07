package com.architecture.kotlinmvvm.base

import android.support.v7.widget.RecyclerView

abstract class BaseListAdapter<M> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: MutableList<M> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    var onItemClickListener: ((data: M, position: Int) -> Unit)? = null

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        viewHolder
                .itemView
                .rootView
                .setOnClickListener {

                    onItemClickListener?.invoke(data.get(position), position)
                }
    }
}