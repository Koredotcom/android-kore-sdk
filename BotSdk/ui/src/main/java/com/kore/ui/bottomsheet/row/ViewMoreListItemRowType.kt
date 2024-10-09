package com.kore.ui.bottomsheet.row

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.bottomsheet.row.item.ViewMoreListItemProvider

enum class ViewMoreListItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(ViewMoreListItemProvider())
}