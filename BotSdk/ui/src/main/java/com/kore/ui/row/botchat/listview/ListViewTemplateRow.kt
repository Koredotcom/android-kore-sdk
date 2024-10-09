package com.kore.ui.row.botchat.listview

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.row.SimpleListAdapter
import com.kore.common.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.ELEMENTS
import com.kore.model.constants.BotResponseConstants.KEY_BUTTONS
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.MORE_COUNT
import com.kore.model.constants.BotResponseConstants.MORE_DATA
import com.kore.model.constants.BotResponseConstants.SEE_MORE
import com.kore.ui.bottomsheet.ShowMoreListBottomSheet
import com.kore.ui.bottomsheet.ViewMoreListBottomSheet
import com.kore.ui.databinding.RowListviewTemplateBinding
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.ui.row.botchat.BotChatRowType.Companion.ROW_LIST_VIEW_PROVIDER
import com.kore.ui.row.botchat.listview.item.ListViewTemplateItemRow

class ListViewTemplateRow(
    private val context: Context,
    private val id: String,
    private val iconUrl: String?,
    private val payload: Map<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(ROW_LIST_VIEW_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ListViewTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ListViewTemplateRow) return false
        return otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl,  isShow = false, isTemplate = true)
        val childBinding = RowListviewTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            showMore.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            list.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            list.adapter = SimpleListAdapter(ListViewTemplateItemRowType.values().asList())
            (payload[KEY_BUTTONS] as List<Map<String, String>>?)?.get(0)?.get(KEY_TITLE)?.let { showMore.text = it }
            payload[SEE_MORE]?.let { showMore.isVisible = it as Boolean }
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowListviewTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowListviewTemplateBinding.commonBind() {
        (list.adapter as SimpleListAdapter).submitList(createRows())
        showMore.setOnClickListener {
            val manager = (this@ListViewTemplateRow.context as FragmentActivity).supportFragmentManager
            if (!(payload[KEY_BUTTONS] as List<Map<String, String>>?).isNullOrEmpty()) {
                ShowMoreListBottomSheet().apply {
                    showData(true, payload[MORE_DATA] as Map<String, Any>, isLastItem, manager, actionEvent)
                }
            } else {
                ViewMoreListBottomSheet().apply {
                    showData(false, payload[ELEMENTS] as List<Map<String, Any>>, manager)
                }
            }
        }
    }

    private fun createRows(): List<SimpleListRow> {
        val elements = payload[ELEMENTS] as List<Map<String, Any>>
        val moreCount = payload[MORE_COUNT] as Double?
        return if (moreCount != null && moreCount > 0) {
            var rows = emptyList<SimpleListRow>()
            for (i in 0 until moreCount.toInt()) {
                rows = rows + ListViewTemplateItemRow(elements[i], isLastItem, actionEvent)
            }
            rows
        } else {
            elements.map { element -> ListViewTemplateItemRow(element, isLastItem, actionEvent) }
        }
    }
}