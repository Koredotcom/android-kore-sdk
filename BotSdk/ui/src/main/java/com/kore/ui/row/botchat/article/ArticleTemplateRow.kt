package com.kore.ui.row.botchat.article

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.adapters.ArticleListAdapter
import com.kore.ui.databinding.RowArticleTemplateBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.BotChatRowType

class ArticleTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
    private val isEnabled: Boolean,
    private val actionEvent: (actionEvent: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_ARTICLE_PROVIDER)
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ArticleTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ArticleTemplateRow) return false
        return otherRow.isEnabled == isEnabled
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, "", false, true)
        val childBinding = RowArticleTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val title: String = (payload[BotResponseConstants.KEY_TITLE] ?: "").toString()
            botListViewTitle.visibility = if (title.isNotEmpty()) View.VISIBLE else View.GONE
            botListViewTitle.text = title
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(VerticalSpaceItemDecoration(10))
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        // This function would get called when any attribute of botResponse is changed
        val childBinding = RowArticleTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            commonBind()
        }
    }

    private fun RowArticleTemplateBinding.commonBind() {
        (payload[BotResponseConstants.ELEMENTS] as List<HashMap<String, Any?>>?)?.let {
            recyclerView.adapter = ArticleListAdapter(
                root.context,
                it,
                isEnabled,
                actionEvent
            )
        }
    }
}