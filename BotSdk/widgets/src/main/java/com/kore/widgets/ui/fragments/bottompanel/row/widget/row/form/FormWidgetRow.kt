package com.kore.widgets.ui.fragments.bottompanel.row.widget.row.form

import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.kore.widgets.databinding.RowFormWidgetBinding
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.event.WidgetActionEvent
import com.kore.widgets.model.WidgetInfoModel
import com.kore.widgets.model.WidgetsModel
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.WidgetInfoRowType

class FormWidgetRow(
    private val widgetsModel: WidgetsModel,
    private val widgetInfoModel: WidgetInfoModel?,
    private val actionEvent: (event: BaseActionEvent) -> Unit
) : WidgetSimpleListRow {
    override val type: WidgetSimpleListRow.SimpleListRowType = WidgetInfoRowType.Form

    override fun areItemsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is FormWidgetRow) return false
        return otherRow.widgetsModel.id == widgetsModel.id
    }

    override fun areContentsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is FormWidgetRow) return false
        return otherRow.widgetInfoModel == widgetInfoModel
    }

    override fun getChangePayload(otherRow: WidgetSimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowFormWidgetBinding).commonBind()
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        (binding as RowFormWidgetBinding).commonBind()
    }

    private fun RowFormWidgetBinding.commonBind() {
        tvFillForm.isVisible = !widgetInfoModel?.formLink.isNullOrEmpty()
        tvFillForm.setOnClickListener {
            if (!widgetInfoModel?.formLink.isNullOrEmpty()) {
                actionEvent(WidgetActionEvent.UrlClick(widgetInfoModel?.formLink!!, widgetsModel.title!!))
            }
        }
    }
}