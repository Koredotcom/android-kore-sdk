package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.AdvancelistViewBinding
import com.kore.ui.row.SimpleListViewHolderProvider

class AdvancedListTemplateProvider : SimpleListViewHolderProvider<AdvancelistViewBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): AdvancelistViewBinding =
        AdvancelistViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}