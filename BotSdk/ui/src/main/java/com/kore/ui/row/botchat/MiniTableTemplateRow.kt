package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.extensions.dpToPx
import com.kore.common.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.adapters.MiniTablePagerAdapter
import com.kore.ui.databinding.MiniTableTemplateBinding

class MiniTableTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val actionEvent: (event: UserActionEvent) -> Unit,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_MINI_TABLE_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is MiniTableTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is MiniTableTemplateRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = MiniTableTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val miniValues = payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>
            if (miniValues.isNotEmpty()) {
                root.apply {
                    offscreenPageLimit = 1
                    val recyclerView = getChildAt(0) as RecyclerView
                    recyclerView.apply {
                        val padding = (20.dpToPx(binding.root.context))
                        setPadding(0, 0, padding, 0)
                        clipToPadding = false
                    }
                    adapter = MiniTablePagerAdapter(root.context, miniValues, actionEvent)
                }
            }
        }
    }
}