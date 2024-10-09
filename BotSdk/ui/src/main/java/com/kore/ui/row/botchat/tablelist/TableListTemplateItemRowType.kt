package com.kore.ui.row.botchat.tablelist

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.tablelist.item.TableListTemplateItemProvider

enum class TableListTemplateItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(TableListTemplateItemProvider()),
}