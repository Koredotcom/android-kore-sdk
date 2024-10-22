package com.kore.ui.audiocodes.webrtcclient.fragments.contactlist

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.audiocodes.webrtcclient.fragments.contactlist.row.ContactListItemProvider

enum class ContactListItemRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Item(ContactListItemProvider())
}