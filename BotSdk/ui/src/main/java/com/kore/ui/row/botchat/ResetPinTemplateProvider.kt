package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowOtpTemplateBinding
import com.kore.ui.databinding.RowPinResetTemplateBinding

class ResetPinTemplateProvider : SimpleListViewHolderProvider<RowPinResetTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowPinResetTemplateBinding =
        RowPinResetTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}