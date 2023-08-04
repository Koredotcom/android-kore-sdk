package kore.botssdk.view.row

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SimpleListViewHolderProvider<Binding : ViewBinding> {

    fun provide(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = SimpleListViewHolder(inflateBinding(parent, viewType))
        initialize(viewHolder)
        return viewHolder
    }

    abstract fun inflateBinding(parent: ViewGroup, viewType: Int): Binding

    open fun initialize(viewHolder: SimpleListViewHolder<Binding>) {}
}