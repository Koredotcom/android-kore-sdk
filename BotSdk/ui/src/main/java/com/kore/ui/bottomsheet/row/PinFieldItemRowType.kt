package com.kore.ui.bottomsheet.row

import com.kore.ui.bottomsheet.row.pinfielditem.PinFieldItemProvider
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider

enum class PinFieldItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(PinFieldItemProvider())
}