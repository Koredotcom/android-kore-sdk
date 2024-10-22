package com.kore.ui.row.botchat.listview

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.listview.item.ListViewTemplateItemProvider

enum class ListViewTemplateItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(ListViewTemplateItemProvider())
}