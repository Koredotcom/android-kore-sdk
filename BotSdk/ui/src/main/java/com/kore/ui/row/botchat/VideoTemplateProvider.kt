package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.databinding.VideoTemplateViewBinding
import com.kore.ui.row.SimpleListViewHolderProvider

class VideoTemplateProvider : SimpleListViewHolderProvider<VideoTemplateViewBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): VideoTemplateViewBinding =
        VideoTemplateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}