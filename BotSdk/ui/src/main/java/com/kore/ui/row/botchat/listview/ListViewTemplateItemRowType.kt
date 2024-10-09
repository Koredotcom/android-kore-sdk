package com.kore.ui.row.botchat.listview

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.listview.item.ListViewTemplateItemProvider

enum class ListViewTemplateItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(ListViewTemplateItemProvider())
}