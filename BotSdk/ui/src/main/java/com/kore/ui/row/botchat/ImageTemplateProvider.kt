package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.ImageTemplateViewBinding
import com.kore.ui.row.SimpleListViewHolderProvider

class ImageTemplateProvider : SimpleListViewHolderProvider<ImageTemplateViewBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): ImageTemplateViewBinding =
        ImageTemplateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}