package com.kore.widgets.ui.fragments.bottompanel

import com.kore.widgets.model.ButtonModel
import com.kore.widgets.model.Panel
import com.kore.widgets.row.WidgetSimpleListRow

interface BottomPanelView {
    fun init()
    fun onBottomPanels(rows: List<WidgetSimpleListRow>)
    fun onPanelWidgets(rows: List<WidgetSimpleListRow>)
    fun onBottomPanelError()
    fun onPanelItemClicked(panel: Panel)
    fun showMenus(menus: List<ButtonModel>)
}