package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.MiniTableTemplateBinding
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowResultsTemplateBinding

class ResultsTemplateProvider : SimpleListViewHolderProvider<RowResultsTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowResultsTemplateBinding =
        RowResultsTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}