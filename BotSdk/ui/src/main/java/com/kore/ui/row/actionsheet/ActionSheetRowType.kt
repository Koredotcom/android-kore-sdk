package com.kore.ui.row.actionsheet

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider

enum class ActionSheetRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Attachments(ActionSheetItemProvider());
}