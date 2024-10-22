package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowAttachmentTemplateBinding

class AttachmentTemplateProvider : SimpleListViewHolderProvider<RowAttachmentTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowAttachmentTemplateBinding =
        RowAttachmentTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}