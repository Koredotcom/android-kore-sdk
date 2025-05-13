package com.kore.ui.botchat.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.kore.SDKConfig
import com.kore.botclient.BotClient
import com.kore.common.Result
import com.kore.common.SDKConfiguration
import com.kore.common.SDKConfiguration.OverrideKoreConfig.historyBatchSize
import com.kore.common.SDKConfiguration.getBotConfigModel
import com.kore.common.utils.DateUtils
import com.kore.common.utils.LogUtils
import com.kore.common.utils.NetworkUtils
import com.kore.constants.SharedPrefConstants.HISTORY_COUNT
import com.kore.data.repository.chathistory.ChatHistoryRepository
import com.kore.data.repository.chathistory.ChatHistoryRepositoryImpl
import com.kore.data.repository.preference.PreferenceRepository
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.base.BaseViewModel
import com.kore.validator.RangeValidator
import kotlinx.coroutines.launch
import java.util.Calendar

@SuppressLint("StaticFieldLeak")
class BotContentViewModel : BaseViewModel<BotContentView>() {
    companion object {
        private val LOG_TAG = BotContentViewModel::class.java.simpleName
    }

    private var historyOffset = 0
    private var moreHistory = true
    private lateinit var context: Context

    private val chatHistoryRepository: ChatHistoryRepository = ChatHistoryRepositoryImpl()
    private val preferenceRepository: PreferenceRepository = PreferenceRepositoryImpl()

    fun init(context: Context) {
        this.context = context
    }

    fun fetchChatHistory(isReconnect: Boolean) {
        if (!moreHistory) {
            getView()?.onChatHistory(emptyList(), isReconnect)
            return
        }
        viewModelScope.launch {
            val botConfigModel = getBotConfigModel()

            if (!NetworkUtils.isNetworkAvailable(context)) {
                getView()?.onChatHistory(emptyList(), isReconnect)
                Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (botConfigModel == null) {
                getView()?.onChatHistory(emptyList(), isReconnect)
                Toast.makeText(context, context.getString(R.string.error_config_bot), Toast.LENGTH_SHORT).show()
                return@launch
            }
            val historyCount = preferenceRepository.getIntValue(context, THEME_NAME, HISTORY_COUNT, historyBatchSize)
            when (val response =
                chatHistoryRepository.getChatHistory(
                    context,
                    if (botConfigModel.isWebHook) BotClient.getJwtToken() else BotClient.getAccessToken(),
                    botConfigModel.botId,
                    botConfigModel.botName,
                    historyOffset,
                    if (historyCount > 10 || historyCount == 0) historyBatchSize else historyCount,
                    botConfigModel.isWebHook
                )) {
                is Result.Success -> {
                    val historyMessages = response.data.first
                    getView()?.onChatHistory(historyMessages, isReconnect)
                    historyOffset += response.data.first.size
                    moreHistory = response.data.second
//                    SDKConfig.setIsMinimized(false)
                    if (historyCount > 0)
                        preferenceRepository.putIntValue(context, THEME_NAME, HISTORY_COUNT, 0)
                }

                else -> {
                    getView()?.onChatHistory(emptyList(), isReconnect)
                    LogUtils.e(LOG_TAG, "RtmUrlResponse error: $response")
//                    SDKConfig.setIsMinimized(false)
                }
            }
        }
    }

    fun loadChatHistory(isReconnect: Boolean) {
        if ((isReconnect && SDKConfig.getHistoryOnNetworkResume()) || (!isReconnect && SDKConfiguration.OverrideKoreConfig.historyInitialCall && SDKConfiguration.OverrideKoreConfig.historyEnable)) {
            historyOffset = 0
            moreHistory = true
            getView()?.onLoadingHistory()
            fetchChatHistory(true)
        }
    }

    fun setHistoryOffset(historyOffset: Int) {
        this.historyOffset = historyOffset
    }

    fun limitRange(startDate: String, endDate: String, format: String): CalendarConstraints.Builder {
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val dateFormat = format.replace("DD", "dd").replace("YY", "yy")
        if (dateFormat.isEmpty()) {
            constraintsBuilderRange.setValidator(DateValidatorPointForward.now())
            return constraintsBuilderRange
        }
        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            val startDateMillis: Long = DateUtils.getDateFromFormat(startDate, dateFormat, 0)
            val endDateMillis: Long = DateUtils.getDateFromFormat(endDate, dateFormat, 1)
            constraintsBuilderRange.setStart(startDateMillis)
            constraintsBuilderRange.setEnd(endDateMillis)
            val minValidator: DateValidator = DateValidatorPointForward.from(startDateMillis)
            val maxValidator: DateValidator = DateValidatorPointBackward.before(endDateMillis)
            val validators = listOf(minValidator, maxValidator)
            val compositeValidator = CompositeDateValidator.allOf(validators)
            constraintsBuilderRange.setValidator(compositeValidator)
        } else if (startDate.isNotEmpty()) {
            val dateValidatorMin: DateValidator = DateValidatorPointForward.from(DateUtils.getDateFromFormat(startDate, dateFormat, 0))

            val listValidators = java.util.ArrayList<DateValidator>()
            listValidators.add(dateValidatorMin)

            val validators = CompositeDateValidator.allOf(listValidators)
            constraintsBuilderRange.setValidator(validators)
        } else if (endDate.isNotEmpty()) {
            val dateValidatorMax: DateValidator = DateValidatorPointBackward.before(DateUtils.getDateFromFormat(endDate, dateFormat, 1))

            val listValidators = java.util.ArrayList<DateValidator>()
            listValidators.add(dateValidatorMax)

            val validators = CompositeDateValidator.allOf(listValidators)
            constraintsBuilderRange.setValidator(validators)
        } else {
            constraintsBuilderRange.setValidator(RangeValidator(null, null))
        }
        return constraintsBuilderRange
    }

    fun onRangeDatePicked(selection: Pair<Long, Long>) {
        val startDate = selection.first
        val endDate = selection.second
        val cal = Calendar.getInstance()
        cal.timeInMillis = startDate
        val strYear = cal[Calendar.YEAR]
        val strMonth = cal[Calendar.MONTH] + 1
        val strDay = cal[Calendar.DAY_OF_MONTH]
        cal.timeInMillis = endDate
        val endYear = cal[Calendar.YEAR]
        val endMonth = cal[Calendar.MONTH] + 1
        val endDay = cal[Calendar.DAY_OF_MONTH]
        val formattedDate: StringBuilder = java.lang.StringBuilder()
        formattedDate.append((if ((strDay + 1) > 9) (strDay + 1) else "0" + (strDay + 1)).toString()).append("-")
            .append(if ((strMonth + 1) > 9) (strMonth + 1) else "0" + (strMonth + 1)).append("-").append(strYear).append(" to ")
            .append(if ((endDay + 1) > 9) (endDay + 1) else "0" + (endDay + 1)).append("-")
            .append(if ((endMonth + 1) > 9) (endMonth + 1) else "0" + (endMonth + 1)).append("-").append(endYear)

        BotClient.getInstance().sendMessage(formattedDate.toString(), formattedDate.toString())
    }

    fun onDatePicked(selection: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selection
        val stYear = calendar[Calendar.YEAR]
        val stMonth = calendar[Calendar.MONTH] + 1
        val stDay = calendar[Calendar.DAY_OF_MONTH]
        val formattedDate =
            (if ((stMonth + 1) > 9) (stMonth + 1) else "0" + (stMonth + 1)).toString() + "/" + (if (stDay > 9) stDay else "0$stDay") + "/" + stYear
        BotClient.getInstance().sendMessage(formattedDate, formattedDate)
    }
}