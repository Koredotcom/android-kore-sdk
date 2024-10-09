package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.CardTemplateViewBinding
import com.kore.common.row.SimpleListViewHolderProvider

class CardTemplateProvider : SimpleListViewHolderProvider<CardTemplateViewBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): CardTemplateViewBinding =
        CardTemplateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}