package com.kore.ui.row.botchat.multiselect

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.multiselect.item.MultiSelectItemProvider

enum class MultiSelectItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(MultiSelectItemProvider())
}