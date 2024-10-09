package com.kore.widgets.row

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class WidgetSimpleListAdapter(
    private val types: List<WidgetSimpleListRow.SimpleListRowType>
) : ListAdapter<WidgetSimpleListRow, RecyclerView.ViewHolder>(SimpleListCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val provider = types.find { it.ordinal == viewType }?.provider ?: throw IllegalArgumentException("viewType is invalid.")
        return provider.provide(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? WidgetSimpleListViewHolder<*>)?.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            (holder as? WidgetSimpleListViewHolder<*>)?.bindWithPayload(getItem(position), payloads)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    private class SimpleListCallback : DiffUtil.ItemCallback<WidgetSimpleListRow>() {

        override fun areItemsTheSame(oldItem: WidgetSimpleListRow, newItem: WidgetSimpleListRow): Boolean {
            if (oldItem::class != newItem::class) return false
            return oldItem.areItemsTheSame(newItem)
        }

        override fun areContentsTheSame(oldItem: WidgetSimpleListRow, newItem: WidgetSimpleListRow): Boolean {
            if (oldItem::class != newItem::class) return false
            return oldItem.areContentsTheSame(newItem)
        }

        override fun getChangePayload(oldItem: WidgetSimpleListRow, newItem: WidgetSimpleListRow): Any? {
            return oldItem.getChangePayload(newItem)
        }
    }
}