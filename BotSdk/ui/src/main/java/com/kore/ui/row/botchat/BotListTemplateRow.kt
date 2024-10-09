package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.extensions.dpToPx
import com.kore.common.row.SimpleListRow
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.ui.adapters.BotListTemplateAdapter
import com.kore.ui.databinding.ListTemplateViewBinding

class BotListTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
    private val iconUrl: String?,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType =
        BotChatRowType.getRowType(BotChatRowType.ROW_LIST_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    @Suppress("UNCHECKED_CAST")
    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = ListTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            botCustomListView.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            botCustomListView.adapter = BotListTemplateAdapter(
                root.context,
                payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>,
                actionEvent
            )
            botCustomListView.addItemDecoration(SimpleListRow.VerticalSpaceItemDecoration((10.dpToPx(root.context))))
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        ListTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun ListTemplateViewBinding.commonBind() {
        val items = (payload[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>)
        items.isNotEmpty().let {
            val btn: Map<*, *> = items[0]
            botCustomListViewButton.text = btn[BotResponseConstants.KEY_TITLE] as String
            botCustomListViewButton.setOnClickListener {
                if (!isLastItem) return@setOnClickListener
                when (btn[BotResponseConstants.TYPE]) {
                    BotResponseConstants.USER_INTENT,
                    BotResponseConstants.URL,
                    BotResponseConstants.WEB_URL -> actionEvent(BotChatEvent.UrlClick(btn[BotResponseConstants.URL] as String))

                    BotResponseConstants.POSTBACK -> {
                        actionEvent(BotChatEvent.SendMessage(botCustomListViewButton.text.toString(), btn[PAYLOAD] as String))
                    }
                }
            }
        }
    }
}