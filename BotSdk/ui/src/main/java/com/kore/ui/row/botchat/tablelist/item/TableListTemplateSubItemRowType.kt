package com.kore.ui.row.botchat.tablelist.item

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.tablelist.item.subitem.TableListTemplateSubItemProvider

enum class TableListTemplateSubItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    SubItem(TableListTemplateSubItemProvider()),
}