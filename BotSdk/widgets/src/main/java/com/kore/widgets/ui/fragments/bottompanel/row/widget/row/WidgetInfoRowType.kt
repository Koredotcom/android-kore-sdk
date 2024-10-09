package com.kore.widgets.ui.fragments.bottompanel.row.widget.row

import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.form.FormWidgetProvider
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.list.ListWidgetProvider
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.piechart.PieChartWidgetProvider

enum class WidgetInfoRowType(
    override val provider: WidgetSimpleListViewHolderProvider<*>
) : WidgetSimpleListRow.SimpleListRowType {
    List(ListWidgetProvider()),
    Form(FormWidgetProvider()),
    PieChart(PieChartWidgetProvider())
}