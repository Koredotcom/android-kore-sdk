package com.kore.ui.row.botchat.listview.item

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.row.SimpleListRow
import com.kore.event.BotChatEvent
import com.kore.event.BotChatEvent.SendMessage
import com.kore.model.constants.BotResponseConstants.BUTTON_TYPE_WEB_URL
import com.kore.model.constants.BotResponseConstants.COLOR
import com.kore.model.constants.BotResponseConstants.DEFAULT_ACTION
import com.kore.model.constants.BotResponseConstants.IMAGE_URL
import com.kore.model.constants.BotResponseConstants.KEY_SUB_TITLE
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.model.constants.BotResponseConstants.POSTBACK
import com.kore.model.constants.BotResponseConstants.TYPE
import com.kore.model.constants.BotResponseConstants.URL
import com.kore.model.constants.BotResponseConstants.VALUE
import com.kore.ui.databinding.RowListviewTemplateItemBinding
import com.kore.ui.row.botchat.listview.ListViewTemplateItemRowType
import com.kore.ui.transforms.RoundedCornersTransform
import com.squareup.picasso.Picasso

class ListViewTemplateItemRow(
    private val item: Map<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = ListViewTemplateItemRowType.Item

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ListViewTemplateItemRow) return false
        return otherRow.item == item
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ListViewTemplateItemRow) return false
        return otherRow.isLastItem == isLastItem
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowListviewTemplateItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            title.text = item[KEY_TITLE].toString()

            val subTitle = item[KEY_SUB_TITLE]
            tvSubTitle.text = subTitle.toString()
            tvSubTitle.isVisible = subTitle != null && subTitle.toString().isNotEmpty()

            itemCost.text = item[VALUE].toString()
            item[COLOR]?.let { itemCost.setTextColor(Color.parseColor(it.toString())) }

            val imageUrl = item[IMAGE_URL]
            imageView.isVisible = imageUrl != null && imageUrl.toString().isNotEmpty()
            item[IMAGE_URL]?.let {
                if (it.toString().isNotEmpty()) {
                    Picasso.get().load(it.toString()).transform(RoundedCornersTransform()).into(imageView)
                }
            }

            root.setOnClickListener {
                if (!isLastItem) return@setOnClickListener
                val defaultAction = item[DEFAULT_ACTION] as Map<String, String>?
                when (defaultAction?.get(TYPE)) {
                    BUTTON_TYPE_WEB_URL -> actionEvent(BotChatEvent.UrlClick(defaultAction[URL].toString()))
                    POSTBACK -> {
                        defaultAction[PAYLOAD]?.let { actionEvent(SendMessage(it)) }
                        defaultAction[KEY_TITLE]?.let { actionEvent(SendMessage(it)) }
                    }
                }
            }
        }
    }
}