package com.kore.ui.audiocodes.webrtcclient.fragments.recents.row

import android.content.Context
import android.net.Uri
import android.text.format.DateUtils
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.common.row.SimpleListRow
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.db.NativeDBManager
import com.kore.ui.audiocodes.webrtcclient.fragments.contactlist.ContactListItemRowType
import com.kore.ui.audiocodes.webrtcclient.general.ImageUtils
import com.kore.ui.audiocodes.webrtcclient.structure.CallEntry
import com.kore.ui.audiocodes.webrtcclient.structure.ContactListObject
import com.kore.ui.databinding.RowRecentsListBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RecentListItemRow(
    private val callEntry: CallEntry,
    private val onItemClick: (contact: CallEntry) -> Unit,
    private val onCallButtonClick: (contact: CallEntry) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = ContactListItemRowType.Item

    companion object {
        private const val TAG = "RecentListItemRow"
    }

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is RecentListItemRow) return false
        return otherRow.callEntry.id == callEntry.id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is RecentListItemRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowRecentsListBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            recentListRowEditTextName.text = callEntry.contactName
            recentListRowEditTextPhonenumber.text = callEntry.contactNumber
            val callTypeId: Int = when (callEntry.callType) {
                CallEntry.CallType.INCOMING -> R.mipmap.recent_incoming
                CallEntry.CallType.OUTGOING -> R.mipmap.recent_outgoing
                CallEntry.CallType.MISSED -> R.mipmap.recent_missed
                else -> {
                    R.mipmap.recent_missed
                }
            }
            recentListRowImageviewCallType.setImageResource(callTypeId)
            recentListRowEditTextCallTime.text = buildCallDate(root.context, callEntry.startTime)
            recentListRowEditTextDuration.text = buildCallDuration(root.context, callEntry.duration)
            val nativeDBObjectList = NativeDBManager.getContactList(
                root.context, NativeDBManager.QueryType.BY_PHONE_AND_SIP, callEntry.contactNumber
            )
            if (!nativeDBObjectList.isNullOrEmpty()) {
                val nativeDBObject = nativeDBObjectList[0]
                val contactListObject = nativeDBObject?.let {
                    ContactListObject(it.displayName, callEntry.contactNumber, nativeDBObject.photoURI, nativeDBObject.photoThumbnailURI)
                }
                recentListRowImageviewContact.setImageResource(R.mipmap.default_contact_list_picture)
                contactListObject?.photoThumbnailURI?.let {
                    val photoBitmap = ImageUtils.getContactBitmapFromURI(root.context, Uri.parse(contactListObject.photoThumbnailURI))
                    if (photoBitmap != null) {
                        val roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(
                            photoBitmap,
                            recentListRowImageviewContact.drawable.intrinsicHeight
                        )
                        recentListRowImageviewContact.setImageBitmap(roundPhotoBitmap)
                    }
                }
                if (contactListObject != null) {
                    recentListRowEditTextName.text = contactListObject.name
                }
            }

            recentListRowImageviewVideo.setOnClickListener { view ->
                (view.parent as ViewGroup).isSelected = true
                onCallButtonClick(callEntry)
            }
            root.setOnClickListener { view ->
                (view.parent as ViewGroup).isSelected = true
                onItemClick(callEntry)
            }
        }
    }

    private fun buildCallDate(context: Context, callDuration: Long): String {
        val resCallDate: String = if (DateUtils.isToday(callDuration)) {
            context.getString(R.string.gen_today)
        } else {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            simpleDateFormat.format(Date(callDuration))
        }
        return resCallDate
    }

    private fun buildCallDuration(context: Context, callDuration: Long): String {
        val callDurationBuilder = StringBuilder()
        val calendar = Calendar.getInstance()
        calendar.time = Date(callDuration)
        val hoursInt = calendar[Calendar.HOUR_OF_DAY] - 2
        val minutesInt = calendar[Calendar.MINUTE]
        val secondsInt = calendar[Calendar.SECOND]
        if (hoursInt - 2 > 0) {
            callDurationBuilder.append(" " + (hoursInt - 2).toString() + context.getString(R.string.gen_hours_short))
        }
        if (minutesInt > 0) {
            callDurationBuilder.append(" " + minutesInt.toString() + context.getString(R.string.gen_minutes_short))
        }
        if (secondsInt > 0) {
            callDurationBuilder.append(" " + secondsInt.toString() + context.getString(R.string.gen_seconds_short))
        }
        return callDurationBuilder.toString()
    }
}