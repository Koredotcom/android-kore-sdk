package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.BotListWidgetTemplateViewBinding

class ListWidgetTemplateProvider : SimpleListViewHolderProvider<BotListWidgetTemplateViewBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): BotListWidgetTemplateViewBinding =
        BotListWidgetTemplateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}