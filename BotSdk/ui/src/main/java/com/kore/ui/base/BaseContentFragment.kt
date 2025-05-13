package com.kore.ui.base

import android.os.Bundle
import androidx.annotation.ContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.kore.common.event.UserActionEvent
import com.kore.listeners.QuickRepliesClickListener
import com.kore.model.BaseBotMessage
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.END_DATE
import com.kore.model.constants.BotResponseConstants.FORMAT
import com.kore.model.constants.BotResponseConstants.START_DATE
import com.kore.ui.R
import com.kore.ui.botchat.fragment.BotContentView
import com.kore.ui.botchat.fragment.BotContentViewModel
import java.util.Calendar

abstract class BaseContentFragment : Fragment(), BotContentView, QuickRepliesClickListener {
    val contentViewModel: BotContentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentViewModel.setView(this)
        contentViewModel.init(requireContext())
    }

    abstract fun onFileDownloadProgress(msgId: String, progress: Int, downloadBytes: Int)

    abstract fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit)

    abstract fun getAdapterLastItem(): BaseBotMessage?

    abstract fun showTypingIndicator(icon: String?, enable: Boolean)

    abstract fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?, isStacked: Boolean)

    abstract fun hideQuickReplies()

    abstract fun addMessagesToAdapter(messages: List<BaseBotMessage>, isHistory: Boolean, isReconnection: Boolean)

    abstract fun getAdapterCount(): Int

    abstract fun onBrandingDetails()

    abstract fun onLoadHistory(isReconnect: Boolean)

    fun showCalenderTemplate(payload: HashMap<String, Any>) {
        if (BotResponseConstants.TEMPLATE_TYPE_DATE == payload[BotResponseConstants.KEY_TEMPLATE_TYPE]) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(payload[BotResponseConstants.KEY_TITLE] as String)
            builder.setPositiveButtonText(getString(R.string.confirm))
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            builder.setCalendarConstraints(
                contentViewModel.limitRange(
                    (payload[START_DATE] as String?) ?: "", (payload[END_DATE] as String?) ?: "", (payload[FORMAT] as String?) ?: ""
                ).build()
            )
            builder.setTheme(R.style.MyMaterialCalendarTheme)
            try {
                val picker = builder.build()
                picker.show(childFragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { selection -> contentViewModel.onDatePicked(selection) }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        } else {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setTitleText(payload[BotResponseConstants.KEY_TITLE] as String)
            builder.setPositiveButtonText(getString(R.string.confirm))
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            builder.setCalendarConstraints(
                contentViewModel.limitRange(
                    (payload[START_DATE] as String?) ?: "", (payload[END_DATE] as String?) ?: "", (payload[FORMAT] as String?) ?: ""
                ).build()
            )
            builder.setTheme(R.style.MyMaterialCalendarTheme)
            try {
                val picker = builder.build()
                picker.show(childFragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { selection -> contentViewModel.onRangeDatePicked(selection) }
                picker.addOnNegativeButtonClickListener { picker.dismiss() }
            } catch (e: java.lang.IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }
}