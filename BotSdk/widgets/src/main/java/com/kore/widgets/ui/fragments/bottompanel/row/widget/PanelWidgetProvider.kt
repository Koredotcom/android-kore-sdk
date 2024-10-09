package com.kore.widgets.ui.fragments.bottompanel.row.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.widgets.databinding.RowPanelWidgetBinding
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

class PanelWidgetProvider : WidgetSimpleListViewHolderProvider<RowPanelWidgetBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowPanelWidgetBinding =
        RowPanelWidgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}