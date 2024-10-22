package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.TableTemplateBinding
import com.kore.ui.row.SimpleListViewHolderProvider

class TableTemplateProvider : SimpleListViewHolderProvider<TableTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): TableTemplateBinding =
        TableTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}