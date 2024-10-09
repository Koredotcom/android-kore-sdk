package com.kore.ui.row.botchat.multiselect

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.multiselect.item.MultiSelectItemProvider

enum class MultiSelectItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(MultiSelectItemProvider())
}