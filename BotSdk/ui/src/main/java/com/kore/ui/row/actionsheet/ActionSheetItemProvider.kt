package com.kore.ui.row.actionsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.RowActionSheetItemBinding
import com.kore.common.row.SimpleListViewHolderProvider

class ActionSheetItemProvider : SimpleListViewHolderProvider<RowActionSheetItemBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowActionSheetItemBinding =
        RowActionSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}