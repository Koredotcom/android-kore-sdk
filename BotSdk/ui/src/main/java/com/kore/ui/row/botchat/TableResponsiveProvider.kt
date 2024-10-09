package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.TableResponsiveLayoutBinding
import com.kore.common.row.SimpleListViewHolderProvider

class TableResponsiveProvider : SimpleListViewHolderProvider<TableResponsiveLayoutBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): TableResponsiveLayoutBinding =
        TableResponsiveLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}