package com.kore.ui.row.botchat.advancemultiselect.category.subitem

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowAdvanceMultiSelectCategorySubitemBinding as Binding

class AdvanceMultiSelectCategorySubItemProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}