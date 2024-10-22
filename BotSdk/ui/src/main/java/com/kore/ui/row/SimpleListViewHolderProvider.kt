package com.kore.ui.row

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kore.botclient.databinding.BaseRowBinding

abstract class SimpleListViewHolderProvider<Binding : ViewBinding> {
    fun provide(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflateBinding(parent, viewType).root
        val binding = BaseRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.addView(view)
        val viewHolder = SimpleListViewHolder(binding)
        initialize(viewHolder as SimpleListViewHolder<Binding>)
        return viewHolder
    }

    abstract fun inflateBinding(parent: ViewGroup, viewType: Int): Binding

    open fun initialize(viewHolder: SimpleListViewHolder<Binding>) {}
}