package com.kore.ui.row

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class SimpleListAdapter(
    private val types: List<SimpleListRow.SimpleListRowType>
) : ListAdapter<SimpleListRow, RecyclerView.ViewHolder>(SimpleListCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val provider = types.find { it.ordinal == viewType }?.provider ?: throw IllegalArgumentException("viewType is invalid.")
        return provider.provide(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? SimpleListViewHolder<*>)?.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            (holder as? SimpleListViewHolder<*>)?.bindWithPayload(getItem(position), payloads)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    private class SimpleListCallback : DiffUtil.ItemCallback<SimpleListRow>() {

        override fun areItemsTheSame(oldItem: SimpleListRow, newItem: SimpleListRow): Boolean {
            if (oldItem::class != newItem::class) return false
            return oldItem.areItemsTheSame(newItem)
        }

        override fun areContentsTheSame(oldItem: SimpleListRow, newItem: SimpleListRow): Boolean {
            if (oldItem::class != newItem::class) return false
            return oldItem.areContentsTheSame(newItem)
        }

        override fun getChangePayload(oldItem: SimpleListRow, newItem: SimpleListRow): Any? {
            return oldItem.getChangePayload(newItem)
        }
    }
}