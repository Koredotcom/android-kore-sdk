package com.kore.ui.bottomsheet.row

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.bottomsheet.row.item.ViewMoreListItemProvider

enum class ViewMoreListItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(ViewMoreListItemProvider())
}