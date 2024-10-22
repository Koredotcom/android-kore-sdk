package com.kore.ui.audiocodes.webrtcclient.fragments.contactlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.common.utils.LogUtils
import com.kore.common.utils.ToastUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.db.NativeDBManager
import com.kore.ui.audiocodes.webrtcclient.fragments.BaseFragment
import com.kore.ui.audiocodes.webrtcclient.fragments.FragmentLifecycle
import com.kore.ui.audiocodes.webrtcclient.fragments.contactlist.row.ContactListItemRow
import com.kore.ui.audiocodes.webrtcclient.structure.ContactListObject
import com.kore.ui.databinding.MainFragmentContactListBinding

class ContactListFragment : BaseFragment(), FragmentLifecycle {
    companion object {
        private const val LOG_TAG = "ContactListFragment"
        private const val CONTACT_LIST_MAX_SIZE = 3000 //0 for unlimited
    }

    private var binding: MainFragmentContactListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.main_fragment_contact_list, container, false)
        initGui()
        return binding?.root
    }

    private fun initGui() {
        binding?.apply {
            var nativeDBObjectList = NativeDBManager.getContactList(requireContext(), NativeDBManager.QueryType.ALL, null)
            LogUtils.d(LOG_TAG, "original nativeDBObjectList: ${nativeDBObjectList?.size}")
            if (nativeDBObjectList.isNullOrEmpty()) {
                ToastUtils.showToast(requireContext(), "No contacts found!", Toast.LENGTH_LONG)
                return@apply
            }
            val adapter = SimpleListAdapter(ContactListItemRowType.values().asList())
            contactList.layoutManager = LinearLayoutManager(root.context)
            contactList.addItemDecoration(SimpleListRow.VerticalSpaceItemDecoration(2))
            contactList.adapter = adapter
            if (nativeDBObjectList.size > CONTACT_LIST_MAX_SIZE) {
                nativeDBObjectList = nativeDBObjectList.subList(0, CONTACT_LIST_MAX_SIZE - 1)
            }
            val contactList: MutableList<ContactListObject> = ArrayList()
            for (dBObject in nativeDBObjectList) {
                dBObject?.phones?.map { dbPhone ->
                    if (dbPhone != null) {
                        contactList.add(
                            ContactListObject(dBObject.displayName, dbPhone.phoneNumber, dBObject.photoURI, dBObject.photoThumbnailURI)
                        )
                    }
                }
            }

            val rows = contactList.map {
                ContactListItemRow(it, this@ContactListFragment::onItemClick, this@ContactListFragment::onCallButtonClick)
            }
            adapter.submitList(rows)
        }
    }

    private fun onItemClick(contact: ContactListObject) {
        LogUtils.d(LOG_TAG, "list view row selected")
        Toast.makeText(requireContext(), "audio call to: ${contact.name} number: ${contact.number}", Toast.LENGTH_SHORT).show()
        acManager.callNumber(requireContext(), contact.number)
    }

    private fun onCallButtonClick(contact: ContactListObject) {
        LogUtils.d(LOG_TAG, "list view button selected")
        Toast.makeText(requireContext(), "Video call to: ${contact.name} number: ${contact.number}", Toast.LENGTH_SHORT).show()
        acManager.callNumber(requireContext(), contact.number, true)
    }

    override fun onPauseFragment() {}
    override fun onResumeFragment() {}
}