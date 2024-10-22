package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.ui.adapters.CarouselStackedTemplateAdapter
import com.kore.ui.databinding.RowCarouselStackedTemplateBinding

class CarouselStackedTemplateRow(
    private val id: String,
    private val items: List<Map<String, *>>,
    private val iconUrl: String?,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_CAROUSEL_STACKED_PROVIDER)
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is CarouselStackedTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is CarouselStackedTemplateRow) return false
        return otherRow.items == items && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        RowCarouselStackedTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowCarouselStackedTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowCarouselStackedTemplateBinding.commonBind() {
        rcfCarousel.adapter = CarouselStackedTemplateAdapter(root.context, items, isLastItem, actionEvent)
    }
}