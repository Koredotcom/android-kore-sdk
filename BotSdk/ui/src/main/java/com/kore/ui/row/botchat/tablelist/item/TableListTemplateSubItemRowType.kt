package com.kore.ui.row.botchat.tablelist.item

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.tablelist.item.subitem.TableListTemplateSubItemProvider

enum class TableListTemplateSubItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    SubItem(TableListTemplateSubItemProvider()),
}