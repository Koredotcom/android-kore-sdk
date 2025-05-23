package com.kore.ui.row.botchat.tablelist.item.subitem

import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.event.BotChatEvent.SendMessage
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.COLOR
import com.kore.model.constants.BotResponseConstants.IMAGE
import com.kore.model.constants.BotResponseConstants.IMAGE_SRC
import com.kore.model.constants.BotResponseConstants.KEY_BG_COLOR
import com.kore.model.constants.BotResponseConstants.KEY_SUB_TITLE
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.LAYOUT
import com.kore.model.constants.BotResponseConstants.LINK
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.model.constants.BotResponseConstants.POSTBACK
import com.kore.model.constants.BotResponseConstants.RADIUS
import com.kore.model.constants.BotResponseConstants.ROW_COLOR
import com.kore.model.constants.BotResponseConstants.TYPE
import com.kore.model.constants.BotResponseConstants.URL
import com.kore.model.constants.BotResponseConstants.VALUE
import com.kore.ui.R
import com.kore.ui.databinding.RowTableListTemplateSubItemBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.tablelist.item.TableListTemplateSubItemRowType

class TableListTemplateSubItemRow(
    private val item: Map<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = TableListTemplateSubItemRowType.SubItem

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableListTemplateSubItemRow) return false
        return otherRow.item == item
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableListTemplateSubItemRow) return false
        return otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowTableListTemplateSubItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val titleMap = item[KEY_TITLE] as Map<String, Any>?
            val titleUrlMap = titleMap?.get(URL) as Map<String, String>?
            val textMap = titleMap?.get(KEY_TEXT) as Map<String, String>?
            val imageMap = titleMap?.get(IMAGE) as Map<String, Any>?
            val valueMap = item[VALUE] as Map<String, Any>?
            val valueUrlMap = valueMap?.get(URL) as Map<String, String>?
            val layoutMap = valueMap?.get(LAYOUT) as Map<String, String>?

            image.isVisible = imageMap?.containsKey(IMAGE_SRC) == true
            imageMap?.get(IMAGE_SRC).let {
                val radiusPx = ((imageMap?.get(RADIUS) as? Double)?.toInt()?.dpToPx(binding.root.context) ?: 10.dpToPx(binding.root.context)).toFloat()
                image.setRoundedCorner(radiusPx)
                Glide.with(binding.root.context).load(it).into<DrawableImageViewTarget>(DrawableImageViewTarget(image))
            }

            (textMap?.get(KEY_TITLE) ?: titleUrlMap?.get(KEY_TITLE))?.let {
                title.text = HtmlCompat.fromHtml(
                    if (!textMap?.get(KEY_TITLE).isNullOrEmpty()) it else "<u>$it</u>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            (textMap?.get(KEY_SUB_TITLE) ?: titleUrlMap?.get(KEY_SUB_TITLE))?.let { subTitle.text = it }

            itemCost.setTextColor(ResourcesCompat.getColor(root.context.resources, R.color.black, root.context.theme))

            titleMap?.get(ROW_COLOR)?.let {
                title.setTextColor(it.toString().toColorInt())
                subTitle.setTextColor(it.toString().toColorInt())
                itemCost.setTextColor(it.toString().toColorInt())
            }

            val type = valueMap?.get(TYPE).toString()
            itemCost.isVisible = (type == KEY_TEXT && valueMap?.get(KEY_TEXT) != null) ||
                    (type == URL && valueUrlMap?.get(KEY_TITLE) != null)

            when (type) {
                KEY_TEXT -> {
                    valueMap?.get(KEY_TEXT)?.let { itemCost.text = it.toString() }
                    layoutMap?.get(COLOR)?.let { itemCost.setTextColor(it.toColorInt()) }
                }

                URL -> valueUrlMap?.get(KEY_TITLE)?.let {
                    itemCost.text = HtmlCompat.fromHtml("<u>$it</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
            }

            item[KEY_BG_COLOR]?.let { root.setBackgroundColor(it.toString().toColorInt()) }
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowTableListTemplateSubItemBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowTableListTemplateSubItemBinding.commonBind() {
        root.setOnClickListener {
            val titleMap = item[KEY_TITLE] as Map<String, Any>?
            val titleUrlMap = titleMap?.get(URL) as Map<String, String>?
            val defaultActionMap = item[BotResponseConstants.DEFAULT_ACTION] as Map<String, String>?
            when (defaultActionMap?.get(TYPE)) {
                POSTBACK -> if (isLastItem) actionEvent(SendMessage(defaultActionMap[KEY_TITLE].toString(), defaultActionMap[PAYLOAD].toString()))
                URL -> actionEvent(BotChatEvent.UrlClick(defaultActionMap[URL].toString()))
                else -> actionEvent(BotChatEvent.UrlClick(titleUrlMap?.get(LINK).toString()))
            }
        }
    }
}