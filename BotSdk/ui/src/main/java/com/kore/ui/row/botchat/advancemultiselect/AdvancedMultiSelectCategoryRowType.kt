package com.kore.ui.row.botchat.advancemultiselect

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.advancemultiselect.category.AdvanceMultiSelectCategoryProvider

enum class AdvancedMultiSelectCategoryRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {

    Category(AdvanceMultiSelectCategoryProvider()),
}