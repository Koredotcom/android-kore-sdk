package com.kore.widgets.ui.fragments.bottompanel.row

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.widgets.databinding.RowBottomPanelItemBinding
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

class BottomPanelItemProvider : WidgetSimpleListViewHolderProvider<RowBottomPanelItemBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowBottomPanelItemBinding =
        RowBottomPanelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}