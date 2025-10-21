package com.kore.ui.base

import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
                picker.addOnPositiveButtonClickListener { selection ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = selection
                    val formattedDate: String = if (!(payload[FORMAT] as String?).isNullOrEmpty()) {
                        val dateFormat: String = payload[FORMAT].toString().replace("DD", "dd").replace("YY", "yy")
                        val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
                        formatter.format(calendar.time)
                    } else {
                        val stYear = calendar[Calendar.YEAR]
                        val stMonth = calendar[Calendar.MONTH] + 1
                        val stDay = calendar[Calendar.DAY_OF_MONTH]
                        (if ((stMonth + 1) > 9) (stMonth + 1) else "0" + (stMonth + 1)).toString() + "/" + (if (stDay > 9) stDay else "0$stDay") + "/" + stYear
                    }
                    contentViewModel.onDatePicked(formattedDate)
                }
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
                picker.addOnPositiveButtonClickListener { selection ->
                    val formatedStartDate: String
                    val formatedEndDate: String
                    val cal = Calendar.getInstance()
                    val startDate = selection.first
                    val endDate = selection.second
                    val formattedDate = if (!(payload[FORMAT] as String?).isNullOrEmpty()) {
                        val dateFormat: String = payload[FORMAT].toString().replace("DD", "dd").replace("YY", "yy")
                        val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
                        cal.setTimeInMillis(startDate)
                        formatedStartDate = formatter.format(cal.time)
                        cal.setTimeInMillis(endDate)
                        formatedEndDate = formatter.format(cal.time)
                        "$formatedStartDate to $formatedEndDate"
                    } else {
                        cal.timeInMillis = startDate
                        val strYear = cal[Calendar.YEAR]
                        val strMonth = cal[Calendar.MONTH] + 1
                        val strDay = cal[Calendar.DAY_OF_MONTH]
                        cal.timeInMillis = endDate
                        val endYear = cal[Calendar.YEAR]
                        val endMonth = cal[Calendar.MONTH] + 1
                        val endDay = cal[Calendar.DAY_OF_MONTH]
                        formatedStartDate =
                            (if ((strDay + 1) > 9) (strDay + 1) else "0" + (strDay + 1)).toString() + "-" + (if ((strMonth + 1) > 9) (strMonth + 1) else "0" + (strMonth + 1)) + "-" + strYear
                        formatedEndDate =
                            (if ((endDay + 1) > 9) (endDay + 1) else "0" + (endDay + 1)).toString() + "-" + (if ((endMonth + 1) > 9) (endMonth + 1) else "0" + (endMonth + 1)) + "-" + endYear
                        "$formatedStartDate to $formatedEndDate"
                    }
                    contentViewModel.onDatePicked(formattedDate)
                }
                picker.addOnNegativeButtonClickListener { picker.dismiss() }
            } catch (e: java.lang.IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }
}