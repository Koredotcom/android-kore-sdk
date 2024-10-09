package com.kore.widgets.ui.fragments.bottompanel.row.widget.row.form

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.widgets.databinding.RowFormWidgetBinding
import com.kore.widgets.row.WidgetSimpleListViewHolderProvider

class FormWidgetProvider : WidgetSimpleListViewHolderProvider<RowFormWidgetBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowFormWidgetBinding =
        RowFormWidgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}