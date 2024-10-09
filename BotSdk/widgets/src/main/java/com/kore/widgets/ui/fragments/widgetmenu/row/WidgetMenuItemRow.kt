package com.kore.widgets.ui.fragments.widgetmenu.row

import androidx.viewbinding.ViewBinding
import com.kore.widgets.constants.Constants
import com.kore.widgets.databinding.RowWidgetMenuItemBinding
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.event.WidgetActionEvent
import com.kore.widgets.model.ButtonModel
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.ui.fragments.widgetmenu.WidgetMenuRowType

class WidgetMenuItemRow(
    private val menu: ButtonModel,
    private val actionEvent: (event: BaseActionEvent) -> Unit
) : WidgetSimpleListRow {
    override val type: WidgetSimpleListRow.SimpleListRowType = WidgetMenuRowType.Menu

    override fun areItemsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is WidgetMenuItemRow) return false
        return otherRow.menu.title == menu.title
    }

    override fun areContentsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is WidgetMenuItemRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowWidgetMenuItemBinding).apply {
            name.text = menu.title
            root.setOnClickListener {
                when (menu.type) {
                    Constants.URL -> menu.url?.let { actionEvent(WidgetActionEvent.UrlClick(it, "")) }
                    Constants.POSTBACK -> menu.payload?.let { actionEvent(WidgetActionEvent.SendMessageFromPanel(it, true)) }
                }
            }
        }
    }
}