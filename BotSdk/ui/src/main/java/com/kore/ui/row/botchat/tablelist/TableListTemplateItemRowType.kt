package com.kore.ui.row.botchat.tablelist

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.tablelist.item.TableListTemplateItemProvider

enum class TableListTemplateItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(TableListTemplateItemProvider()),
}