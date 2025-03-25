package com.kore.ui.row.botchat

import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.graphics.toColorInt
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.databinding.RowClockTemplateBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.BotChatRowType.Companion.ROW_CLOCK_PROVIDER

class ClockTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val isLastItem: Boolean = false,
    private val selectedTime: String,
    private val onClockChange: (id: String, selectedTime: String?, key: String) -> Unit,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    companion object {
        const val MERIDIAN_AM = "AM"
        const val MERIDIAN_PM = "PM"
    }

    override val type: SimpleListRowType = BotChatRowType.getRowType(ROW_CLOCK_PROVIDER)
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ClockTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ClockTemplateRow) return false
        return otherRow.isLastItem == isLastItem && otherRow.selectedTime == selectedTime
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowClockTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val sharedPrefs = PreferenceRepositoryImpl()
            val bgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
            val txtColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF").toColorInt()
            confirm.setRoundedCorner(6.dpToPx(root.context).toFloat())
            confirm.setBackgroundColor(bgColor)
            confirm.setTextColor(txtColor)
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowClockTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowClockTemplateBinding.commonBind() {
        if (selectedTime.isNotEmpty()) {
            val currentTime = selectedTime.split(":")
            hoursText.text = currentTime[0]
            minutesText.text = currentTime[1]
            amPm.text = currentTime[2]
            val hours = currentTime[0].toInt()
            seekbarHours.progress = if (hours == 12 && currentTime[2] == MERIDIAN_AM) {
                0
            } else if (currentTime[2] == MERIDIAN_PM && hours < 12) {
                hours + 12
            } else {
                hours
            }
            seekbarMinutes.progress = currentTime[1].toInt()
        }
        setSeekbarListener(seekbarHours, seekbarMinutes, this)
        setSeekbarListener(seekbarMinutes, seekbarHours, this)
        confirm.setOnClickListener {
            actionEvent(BotChatEvent.SendMessage("${hoursText.text}:${minutesText.text} ${amPm.text}"))
        }
        confirm.isClickable = isLastItem
        seekbarHours.isEnabled = isLastItem
        seekbarMinutes.isEnabled = isLastItem
    }

    private fun setSeekbarListener(seekbar: SeekBar, otherSeekBar: SeekBar, binding: RowClockTemplateBinding) {
        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
                if (seekBar?.isSelected == true) {
                    if (seekbar.id == binding.seekbarHours.id) {
                        binding.hoursText.text = if (p1 == 0) {
                            "12"
                        } else if (p1 < 10) {
                            "0$p1"
                        } else if (p1 <= 12) {
                            "$p1"
                        } else if ((p1 - 12) < 10) {
                            "0${(p1 - 12)}"
                        } else {
                            "${p1 - 12}"
                        }
                        binding.amPm.text = if (p1 >= 12) MERIDIAN_PM else MERIDIAN_AM
                    } else {
                        binding.minutesText.text = if (p1 < 10) "0$p1" else "$p1"
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                if (!isLastItem) return
                seekbar.isSelected = true
                otherSeekBar.isSelected = false
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (!isLastItem) return
                onClockChange(
                    id,
                    "${binding.hoursText.text}:${binding.minutesText.text}:${binding.amPm.text}",
                    BotResponseConstants.SELECTED_TIME
                )
            }
        })
    }
}