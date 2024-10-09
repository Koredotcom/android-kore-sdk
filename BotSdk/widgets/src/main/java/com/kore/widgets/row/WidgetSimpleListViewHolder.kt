package com.kore.widgets.row

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class WidgetSimpleListViewHolder<Binding : ViewBinding>(
    val binding: Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: WidgetSimpleListRow) {
        row.bind(binding)
    }

    fun bindWithPayload(row: WidgetSimpleListRow, payload: List<Any>) {
        row.bindWithPayload(binding, payload)
    }
}