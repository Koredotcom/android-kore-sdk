package com.kore.widgets.ui.fragments.bottompanel.row.widget

import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

enum class PanelWidgetRowType(
    override val provider: WidgetSimpleListViewHolderProvider<*>
) : WidgetSimpleListRow.SimpleListRowType {
    Widget(PanelWidgetProvider()),
}