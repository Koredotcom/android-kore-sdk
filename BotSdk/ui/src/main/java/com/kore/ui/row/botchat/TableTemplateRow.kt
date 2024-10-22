package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.adapters.TableTemplateAdapter
import com.kore.ui.adapters.TableTemplateHeaderAdapter
import com.kore.ui.databinding.TableTemplateBinding

class TableTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
) : SimpleListRow() {
    override val type: SimpleListRowType =
        BotChatRowType.getRowType(BotChatRowType.ROW_TABLE_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableTemplateRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = TableTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val cols = payload[BotResponseConstants.COLUMNS] as List<List<String>>

            rvTableView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

            rvTableView.addItemDecoration(DividerItemDecoration(root.context, LinearLayoutManager.VERTICAL))

            rvTableView.adapter =
                TableTemplateAdapter(root.context, payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>, cols)
            rvTableViewHeader.layoutManager =
                GridLayoutManager(root.context, (payload[BotResponseConstants.COLUMNS] as List<List<String>>).size)
//            rvTableViewHeader.layoutManager = LinearLayoutManager(
//                root.context, LinearLayoutManager.HORIZONTAL, false
//            )

            rvTableViewHeader.adapter = TableTemplateHeaderAdapter(root.context, cols)
        }
    }
}