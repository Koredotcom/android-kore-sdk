package com.kore.widgets.ui.fragments.bottompanel.row

import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

enum class BottomPanelRowType(
    override val provider: WidgetSimpleListViewHolderProvider<*>
) : WidgetSimpleListRow.SimpleListRowType {
    Panel(BottomPanelItemProvider())
}