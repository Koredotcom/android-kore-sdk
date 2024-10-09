package com.kore.ui.row.actionsheet

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider

enum class ActionSheetRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Attachments(ActionSheetItemProvider());
}