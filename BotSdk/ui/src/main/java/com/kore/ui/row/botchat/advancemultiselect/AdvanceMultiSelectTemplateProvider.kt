package com.kore.ui.row.botchat.advancemultiselect

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowAdvanceMultiSelectBinding as Binding

class AdvanceMultiSelectTemplateProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}