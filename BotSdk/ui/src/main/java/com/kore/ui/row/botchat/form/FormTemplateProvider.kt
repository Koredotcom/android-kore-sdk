package com.kore.ui.row.botchat.form

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.FormTempleteViewBinding

class FormTemplateProvider : SimpleListViewHolderProvider<FormTempleteViewBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): FormTempleteViewBinding =
        FormTempleteViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}