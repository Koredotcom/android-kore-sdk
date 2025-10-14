package com.kore.ui.row.botchat

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.getDotMessage
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR
import com.kore.model.constants.BotResponseConstants.DESCRIPTION
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.MOBILE_NUMBER
import com.kore.model.constants.BotResponseConstants.OTP_BUTTONS
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.model.constants.BotResponseConstants.PIN_LENGTH
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.bottomsheet.row.PinFieldItemRowType
import com.kore.ui.bottomsheet.row.pinfielditem.PinFieldItemRow.Companion.createRows
import com.kore.ui.databinding.RowOtpTemplateBinding
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow

class OtpTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_OTP_PROVIDER)
    private val itemSpace = 10
    private var pinLength = 0

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
            pinLength = (payload[PIN_LENGTH] as Double).toInt()
            if (bottomSheetDialog != null) {
                llCloseBottomSheet.setPadding(llCloseBottomSheet.paddingLeft, 0, llCloseBottomSheet.paddingRight, llCloseBottomSheet.paddingBottom)
            }
            val sharedPrefs = PreferenceRepositoryImpl()
            val itemBgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
            val gradientDrawable = root.background.mutate() as GradientDrawable
            gradientDrawable.setStroke(1.dpToPx(root.context), if (bottomSheetDialog == null) itemBgColor else Color.TRANSPARENT)
            otpRecycler.addItemDecoration(HorizontalSpaceItemDecoration(itemSpace))
            otpRecycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            otpRecycler.post {
                otpRecycler.adapter = SimpleListAdapter(PinFieldItemRowType.entries).apply {
                    submitList(createRows(pinLength, otpRecycler.width, itemSpace))
                    otpRecycler.post {
                        val childCount = otpRecycler.childCount
                        for (index in 0 until childCount) {
                            val childView: EditText = otpRecycler.getChildAt(index).findViewById(R.id.otp_field)
                            val prevChildView: EditText? =
                                if (index - 1 >= 0) otpRecycler.getChildAt(index - 1).findViewById(R.id.otp_field) else null
                            val nextChildView: EditText? =
                                if (index + 1 < childCount) otpRecycler.getChildAt(index + 1).findViewById(R.id.otp_field) else null
                            addTextWatcher(prevChildView, childView, nextChildView)
                        }
                    }
                }
            }
            (payload[OTP_BUTTONS] as List<Map<String, String>>?)?.let { list ->
                submit.text = list[0][KEY_TITLE]
                val btnBgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
                val txtColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF").toColorInt()
                submit.setRoundedCorner(6.dpToPx(root.context).toFloat())
                submit.setBackgroundColor(btnBgColor)
                submit.setTextColor(txtColor)
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
                var otp = ""
                val childCount = otpRecycler.childCount
                for (index in 0 until childCount) {
                    val childView: EditText = otpRecycler.getChildAt(index).findViewById(R.id.otp_field)
                    otp += childView.text.toString()
                }
                if (otp.length == pinLength) {
                    actionEvent(BotChatEvent.SendMessage(otp.getDotMessage(), otp))
                }
                bottomSheetDialog?.dismiss()
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