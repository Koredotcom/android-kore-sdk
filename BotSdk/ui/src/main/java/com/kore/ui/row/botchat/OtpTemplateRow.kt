package com.kore.ui.row.botchat

import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.extensions.getDotMessage
import com.kore.model.constants.BotResponseConstants.DESCRIPTION
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.MOBILE_NUMBER
import com.kore.model.constants.BotResponseConstants.OTP_BUTTONS
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.ui.databinding.RowOtpTemplateBinding
import com.kore.ui.row.SimpleListRow

class OtpTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_OTP_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is OtpTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is OtpTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowOtpTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            close.isVisible = false
            title.isVisible = payload[KEY_TITLE] != null
            description.isVisible = payload[DESCRIPTION] != null
            title.text = payload[KEY_TITLE] as String?
            description.text = payload[DESCRIPTION] as String?
            phoneNumber.text = payload[MOBILE_NUMBER] as String?
            addTextWatcher(null, otpField1, otpField2)
            addTextWatcher(otpField1, otpField2, otpField3)
            addTextWatcher(otpField2, otpField3, otpField4)
            addTextWatcher(otpField3, otpField4, null)
            (payload[OTP_BUTTONS] as List<Map<String, String>>?)?.let { list ->
                submit.text = list[0][KEY_TITLE]
                val content = SpannableString(list[1][KEY_TITLE])
                content.setSpan(UnderlineSpan(), 0, content.length, 0)
                resendOtp.text = content
                commonBind()
            }
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        val childBinding = RowOtpTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            commonBind()
        }
    }

    private fun RowOtpTemplateBinding.commonBind() {
        (payload[OTP_BUTTONS] as List<Map<String, String>>?)?.let { list ->
            submit.setOnClickListener {
                if (!isLastItem) return@setOnClickListener
                val otp = otpField1.text.toString() + otpField2.text.toString() +
                        otpField3.text.toString() + otpField4.text.toString()
                if (otp.length == 4) {
                    actionEvent(BotChatEvent.SendMessage(otp.getDotMessage(), otp))
                }
            }
            resendOtp.setOnClickListener {
                if (!isLastItem) return@setOnClickListener
                actionEvent(BotChatEvent.SendMessage(list[1][PAYLOAD].toString(), list[1][PAYLOAD]))
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