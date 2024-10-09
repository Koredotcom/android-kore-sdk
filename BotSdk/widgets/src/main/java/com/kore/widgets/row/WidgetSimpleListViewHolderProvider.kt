package com.kore.widgets.row

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class WidgetSimpleListViewHolderProvider<Binding : ViewBinding> {
    fun provide(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = inflateBinding(parent, viewType)
        val viewHolder = WidgetSimpleListViewHolder(binding)
        initialize(viewHolder)
        return viewHolder
    }

    abstract fun inflateBinding(parent: ViewGroup, viewType: Int): Binding

    open fun initialize(viewHolder: WidgetSimpleListViewHolder<Binding>) {}
}