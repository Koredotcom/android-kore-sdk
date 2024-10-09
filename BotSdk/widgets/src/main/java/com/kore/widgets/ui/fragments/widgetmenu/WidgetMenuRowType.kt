package com.kore.widgets.ui.fragments.widgetmenu

import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider
import com.kore.widgets.ui.fragments.widgetmenu.row.WidgetMenuItemProvider

enum class WidgetMenuRowType(
    override val provider: WidgetSimpleListViewHolderProvider<*>
) : WidgetSimpleListRow.SimpleListRowType {
    Menu(WidgetMenuItemProvider())
}