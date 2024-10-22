package com.kore.ui.row.botchat.advancemultiselect.category

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.advancemultiselect.category.subitem.AdvanceMultiSelectCategorySubItemProvider

enum class AdvancedMultiSelectCategorySubItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {

    SubItem(AdvanceMultiSelectCategorySubItemProvider()),
}