package com.kore.common.row

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class SimpleListViewHolder<Binding : ViewBinding>(
    val binding: Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(row: SimpleListRow) {
        row.bind(binding)
    }

    fun bindWithPayload(row: SimpleListRow, payload: List<Any>) {
        row.bindWithPayload(binding, payload)
    }
}