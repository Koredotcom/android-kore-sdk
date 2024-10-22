package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.PieChartTemplateBinding
import com.kore.ui.row.SimpleListViewHolderProvider

class PieChartTemplateProvider : SimpleListViewHolderProvider<PieChartTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): PieChartTemplateBinding =
        PieChartTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}