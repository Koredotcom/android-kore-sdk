package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.HorizontalBarChartTemplateBinding
import com.kore.common.row.SimpleListViewHolderProvider

class HorizontalBarChartProvider :
    SimpleListViewHolderProvider<HorizontalBarChartTemplateBinding>() {
    override fun inflateBinding(
        parent: ViewGroup, viewType: Int
    ): HorizontalBarChartTemplateBinding = HorizontalBarChartTemplateBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
}