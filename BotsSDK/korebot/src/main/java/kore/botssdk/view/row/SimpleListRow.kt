package kore.botssdk.view.row

import androidx.viewbinding.ViewBinding

interface SimpleListRow {

    val type: SimpleListRowType

    fun areItemsTheSame(otherRow: SimpleListRow): Boolean

    fun areContentsTheSame(otherRow: SimpleListRow): Boolean

    fun getChangePayload(otherRow: SimpleListRow): Any? = null

    fun <Binding : ViewBinding> bind(binding: Binding)

    fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {}

    interface SimpleListRowType {

        val ordinal: Int

        val provider: SimpleListViewHolderProvider<*>
    }
}