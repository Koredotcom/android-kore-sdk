package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowCarouselTemplateBinding

class CarouselTemplateProvider : SimpleListViewHolderProvider<RowCarouselTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowCarouselTemplateBinding =
        RowCarouselTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}