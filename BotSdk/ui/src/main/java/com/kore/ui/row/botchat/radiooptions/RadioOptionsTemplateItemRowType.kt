package com.kore.ui.row.botchat.radiooptions

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.radiooptions.item.RadioOptionsTemplateItemProvider

enum class RadioOptionsTemplateItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(RadioOptionsTemplateItemProvider())
}