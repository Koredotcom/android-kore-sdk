package com.kore.ui.audiocodes.webrtcclient.fragments.recents

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.audiocodes.webrtcclient.fragments.recents.row.RecentListItemProvider

enum class RecentListItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(RecentListItemProvider())
}