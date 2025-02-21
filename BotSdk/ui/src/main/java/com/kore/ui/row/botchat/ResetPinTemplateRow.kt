package com.kore.ui.row.botchat

import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.extensions.getDotMessage
import com.kore.model.constants.BotResponseConstants.DESCRIPTION
import com.kore.model.constants.BotResponseConstants.ENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.MOBILE_NUMBER
import com.kore.model.constants.BotResponseConstants.OTP_BUTTONS
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.model.constants.BotResponseConstants.REENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.RESET_BUTTONS
import com.kore.model.constants.BotResponseConstants.WARNING_MESSAGE
import com.kore.ui.databinding.RowOtpTemplateBinding
import com.kore.ui.databinding.RowPinResetTemplateBinding
import com.kore.ui.row.SimpleListRow

class ResetPinTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_RESET_PIN_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ResetPinTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ResetPinTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowPinResetTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            close.isVisible = false
            title.isVisible = payload[KEY_TITLE] != null
            title.text = payload[KEY_TITLE] as String?
            enterNewPin.text = payload[ENTER_PIN_TITLE] as String?
            reenterNewPin.text = payload[REENTER_PIN_TITLE] as String?
            addTextWatcher(null, newPinField1, newPinField2)
            addTextWatcher(newPinField1, newPinField2, newPinField3)
            addTextWatcher(newPinField2, newPinField3, newPinField4)
            addTextWatcher(newPinField3, newPinField4, reenterPinField1)
            addTextWatcher(null, reenterPinField1, reenterPinField2)
            addTextWatcher(reenterPinField1, reenterPinField2, reenterPinField3)
            addTextWatcher(reenterPinField2, reenterPinField3, reenterPinField4)
            addTextWatcher(reenterPinField3, reenterPinField4, null)
            (payload[RESET_BUTTONS] as List<Map<String, String>>?)?.let { list ->
                reset.text = list[0][KEY_TITLE]
                reset.setOnClickListener {
                    val enteredPin =
                        newPinField1.text.toString() + newPinField2.text.toString() +
                                newPinField3.text.toString() + newPinField4.text.toString()
                    val reenteredPin =
                        reenterPinField1.text.toString() + reenterPinField2.text.toString() +
                                reenterPinField3.text.toString() + reenterPinField4.text.toString()
                    if (enteredPin.length == 4 && reenteredPin.length == 4 && enteredPin == reenteredPin) {
                        actionEvent(BotChatEvent.SendMessage(enteredPin.getDotMessage(), enteredPin))
                    } else {
                        Toast.makeText(root.context, payload[WARNING_MESSAGE].toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        val childBinding = RowPinResetTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            commonBind()
        }
    }

    private fun RowPinResetTemplateBinding.commonBind() {
        (payload[RESET_BUTTONS] as List<Map<String, String>>?)?.let { list ->
            reset.setOnClickListener {
                if (!isLastItem) return@setOnClickListener
                val enteredPin =
                    newPinField1.text.toString() + newPinField2.text.toString() +
                            newPinField3.text.toString() + newPinField4.text.toString()
                val reenteredPin =
                    reenterPinField1.text.toString() + reenterPinField2.text.toString() +
                            reenterPinField3.text.toString() + reenterPinField4.text.toString()
                if (enteredPin.length == 4 && reenteredPin.length == 4 && enteredPin == reenteredPin) {
                    actionEvent(BotChatEvent.SendMessage(enteredPin.getDotMessage(), enteredPin))
                } else {
                    Toast.makeText(root.context, payload[WARNING_MESSAGE].toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addTextWatcher(previousView: EditText?, editText: EditText, nextView: EditText?) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (editText.text.isNotEmpty()) {
                    nextView?.requestFocus()
                } else {
                    previousView?.requestFocus()
                }
            }
        })
    }
}