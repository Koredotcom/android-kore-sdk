package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.MiniTableTemplateBinding
import com.kore.ui.row.SimpleListViewHolderProvider

class MiniTableTemplateProvider : SimpleListViewHolderProvider<MiniTableTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): MiniTableTemplateBinding =
        MiniTableTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}