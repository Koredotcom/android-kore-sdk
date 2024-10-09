package com.kore.widgets.ui.fragments.bottompanel.row.widget.row.piechart

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.widgets.databinding.RowPiechartWidgetBinding
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

class PieChartWidgetProvider : WidgetSimpleListViewHolderProvider<RowPiechartWidgetBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowPiechartWidgetBinding =
        RowPiechartWidgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}