package com.kore.ui.row.botchat.radiooptions

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowRadioOptionsTemplateBinding as Binding

class RadioOptionsTemplateProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}