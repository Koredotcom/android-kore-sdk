package com.kore.widgets.ui.fragments.bottompanel.row.widget.row.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.widgets.adapters.WidgetsListWidgetAdapter
import com.kore.widgets.databinding.RowListWidgetBinding
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.extensions.dpToPx
import com.kore.widgets.model.WidgetInfoModel
import com.kore.widgets.model.WidgetsModel
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.WidgetInfoRowType

class ListWidgetRow(
    private val widgetsModel: WidgetsModel,
    private val widgetInfoModel: WidgetInfoModel?,
    private val actionEvent: (event: BaseActionEvent) -> Unit
) : WidgetSimpleListRow {
    override val type: WidgetSimpleListRow.SimpleListRowType = WidgetInfoRowType.List

    override fun areItemsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is ListWidgetRow) return false
        return otherRow.widgetsModel.id == widgetsModel.id
    }

    override fun areContentsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is ListWidgetRow) return false
        return otherRow.widgetsModel == widgetsModel && otherRow.widgetInfoModel == widgetInfoModel
    }

    override fun getChangePayload(otherRow: WidgetSimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowListWidgetBinding).commonBind()
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        (binding as RowListWidgetBinding).commonBind()
    }

    private fun RowListWidgetBinding.commonBind() {
        upcomingMeetingRootRecycler.layoutManager = LinearLayoutManager(root.context)
        upcomingMeetingRootRecycler.addItemDecoration(WidgetSimpleListRow.VerticalSpaceItemDecoration(4.dpToPx(root.context)))
        widgetInfoModel?.let { infoModel ->
            val listWidgetAdapter = WidgetsListWidgetAdapter(root.context, infoModel.elements ?: emptyList(), actionEvent)
            upcomingMeetingRootRecycler.adapter = listWidgetAdapter
        }
    }
}