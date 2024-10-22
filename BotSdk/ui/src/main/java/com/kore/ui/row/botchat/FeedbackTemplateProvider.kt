package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowFeedbackTemplateBinding as Binding

class FeedbackTemplateProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}