package com.kore.widgets.row

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewbinding.ViewBinding
import com.kore.widgets.event.BaseActionEvent

interface WidgetSimpleListRow {

    val type: SimpleListRowType

    fun areItemsTheSame(otherRow: WidgetSimpleListRow): Boolean

    fun areContentsTheSame(otherRow: WidgetSimpleListRow): Boolean

    fun getChangePayload(otherRow: WidgetSimpleListRow): Any? = null

    fun <Binding : ViewBinding> bind(binding: Binding)

    fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {}

    fun populateData(
        messageId: String, isLastItem: Boolean, botResponse: String, actionEvent: (actionEvent: BaseActionEvent) -> Unit = {}
    ) {
    }

    class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }


    interface SimpleListRowType {

        val ordinal: Int

        val provider: WidgetSimpleListViewHolderProvider<*>
    }
}