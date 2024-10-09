package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.BarChartTemplateBinding
import com.kore.common.row.SimpleListViewHolderProvider

class BarChartTemplateProvider : SimpleListViewHolderProvider<BarChartTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): BarChartTemplateBinding =
        BarChartTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}