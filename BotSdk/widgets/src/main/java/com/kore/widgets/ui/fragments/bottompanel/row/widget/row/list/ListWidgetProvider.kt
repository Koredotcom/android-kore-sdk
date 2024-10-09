package com.kore.widgets.ui.fragments.bottompanel.row.widget.row.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.widgets.databinding.RowListWidgetBinding
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

class ListWidgetProvider : WidgetSimpleListViewHolderProvider<RowListWidgetBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowListWidgetBinding =
        RowListWidgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}