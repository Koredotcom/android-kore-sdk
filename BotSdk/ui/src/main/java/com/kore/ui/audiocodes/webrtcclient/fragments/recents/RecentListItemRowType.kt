package com.kore.ui.audiocodes.webrtcclient.fragments.recents

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.audiocodes.webrtcclient.fragments.recents.row.RecentListItemProvider

enum class RecentListItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(RecentListItemProvider())
}