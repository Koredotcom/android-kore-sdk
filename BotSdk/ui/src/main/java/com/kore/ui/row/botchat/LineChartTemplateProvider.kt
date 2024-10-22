package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.LineChartTemplateBinding
import com.kore.ui.row.SimpleListViewHolderProvider

class LineChartTemplateProvider : SimpleListViewHolderProvider<LineChartTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): LineChartTemplateBinding =
        LineChartTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}