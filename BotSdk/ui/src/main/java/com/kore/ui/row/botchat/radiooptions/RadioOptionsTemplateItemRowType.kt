package com.kore.ui.row.botchat.radiooptions

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.radiooptions.item.RadioOptionsTemplateItemProvider

enum class RadioOptionsTemplateItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(RadioOptionsTemplateItemProvider())
}