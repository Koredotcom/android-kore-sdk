package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.ListTemplateViewBinding
import com.kore.common.row.SimpleListViewHolderProvider

class BotListTemplateProvider : SimpleListViewHolderProvider<ListTemplateViewBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): ListTemplateViewBinding =
        ListTemplateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}