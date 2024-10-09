package com.kore.widgets.ui.fragments.widgetmenu.row

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.widgets.databinding.RowWidgetMenuItemBinding
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

class WidgetMenuItemProvider : WidgetSimpleListViewHolderProvider<RowWidgetMenuItemBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowWidgetMenuItemBinding =
        RowWidgetMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}