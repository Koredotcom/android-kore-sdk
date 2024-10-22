package com.kore.ui.row.botchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.databinding.RowCarouselStackedTemplateBinding

class CarouselStackedTemplateProvider: SimpleListViewHolderProvider<RowCarouselStackedTemplateBinding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): RowCarouselStackedTemplateBinding =
        RowCarouselStackedTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
}