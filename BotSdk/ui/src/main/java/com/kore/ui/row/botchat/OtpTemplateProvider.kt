package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowOtpTemplateBinding

class OtpTemplateProvider : SimpleListViewHolderProvider<RowOtpTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowOtpTemplateBinding =
        RowOtpTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}