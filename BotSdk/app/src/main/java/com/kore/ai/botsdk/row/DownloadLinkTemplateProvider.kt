package com.kore.ai.botsdk.row

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ai.botsdk.databinding.RowDownloadLinkTemplateBinding as Binding

class DownloadLinkTemplateProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}