package com.kore.ui.audiocodes.webrtcclient.fragments.contactlist

import com.kore.common.row.SimpleListRow
import com.kore.common.row.SimpleListViewHolderProvider
import com.kore.ui.audiocodes.webrtcclient.fragments.contactlist.row.ContactListItemProvider

enum class ContactListItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(ContactListItemProvider())
}