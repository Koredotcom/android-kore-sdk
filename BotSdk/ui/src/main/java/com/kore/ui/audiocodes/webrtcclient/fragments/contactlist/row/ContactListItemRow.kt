package com.kore.ui.audiocodes.webrtcclient.fragments.contactlist.row

import android.net.Uri
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.ui.row.SimpleListRow
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.fragments.contactlist.ContactListItemRowType
import com.kore.ui.audiocodes.webrtcclient.general.ImageUtils
import com.kore.ui.audiocodes.webrtcclient.permissions.PermissionManager
import com.kore.ui.audiocodes.webrtcclient.permissions.PermissionManagerType
import com.kore.ui.audiocodes.webrtcclient.structure.ContactListObject
import com.kore.ui.databinding.RowContactListItemBinding

class ContactListItemRow(
    private val contact: ContactListObject,
    private val onItemClick: (contact: ContactListObject) -> Unit,
    private val onCallButtonClick: (contact: ContactListObject) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = ContactListItemRowType.Item

    companion object {
        private const val TAG = "ContactListItemRow"
    }

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ContactListItemRow) return false
        return otherRow.contact.name == contact.name
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ContactListItemRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowContactListItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            name.text = contact.name
            phoneNumber.text = contact.number
            thumbnail.setImageResource(R.mipmap.default_contact_list_picture)
            contact.photoThumbnailURI?.let { uri ->
                val photoBitmap = ImageUtils.getContactBitmapFromURI(root.context, Uri.parse(uri))
                if (photoBitmap != null) {
                    val roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(photoBitmap, thumbnail.drawable.intrinsicHeight)
                    thumbnail.setImageBitmap(roundPhotoBitmap)
                }
            }
            callButton.setOnClickListener { onCallButtonClick(contact) }
            root.setOnClickListener {
                if (PermissionManager.instance.checkPermission(root.context, PermissionManagerType.CONTACTS)) {
                    onItemClick(contact)
                } else {
                    LogUtils.e(TAG, "Contact permission disabled or null")
                }
            }
        }
    }
}