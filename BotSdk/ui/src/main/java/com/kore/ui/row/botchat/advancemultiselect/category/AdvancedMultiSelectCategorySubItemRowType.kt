package com.kore.ui.row.botchat.advancemultiselect.category

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.advancemultiselect.category.subitem.AdvanceMultiSelectCategorySubItemProvider

enum class AdvancedMultiSelectCategorySubItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {

    SubItem(AdvanceMultiSelectCategorySubItemProvider()),
}