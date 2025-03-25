package com.kore.ui.row.botchat

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
import com.kore.model.constants.BotResponseConstants.ENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.PIN_LENGTH
import com.kore.model.constants.BotResponseConstants.REENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.RESET_BUTTONS
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.model.constants.BotResponseConstants.WARNING_MESSAGE
import com.kore.ui.R
import com.kore.ui.bottomsheet.row.PinFieldItemRowType
import com.kore.ui.bottomsheet.row.pinfielditem.PinFieldItemRow.Companion.createRows
import com.kore.ui.databinding.RowPinResetTemplateBinding
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow

class ResetPinTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_RESET_PIN_PROVIDER)
    private val itemSpace = 10
    private var pinLength = 0

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
            (payload[RESET_BUTTONS] as List<Map<String, String>>?)?.let { list ->
                reset.text = list[0][KEY_TITLE]
                val sharedPrefs = PreferenceRepositoryImpl()
                val bgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
                val txtColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF").toColorInt()
                reset.setRoundedCorner(6.dpToPx(root.context).toFloat())
                reset.setBackgroundColor(bgColor)
                reset.setTextColor(txtColor)
                pinLength = (payload[PIN_LENGTH] as Double).toInt()
                newPinRecycler.addItemDecoration(HorizontalSpaceItemDecoration(itemSpace))
                newPinRecycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                newPinRecycler.post {
                    newPinRecycler.adapter = SimpleListAdapter(PinFieldItemRowType.entries).apply {
                        submitList(createRows(pinLength, newPinRecycler.width, itemSpace))
                        newPinRecycler.post {
                            val childCount = newPinRecycler.childCount
                            for (index in 0 until childCount) {
                                val childView: EditText = newPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                                val prevChildView: EditText? =
                                    if (index - 1 >= 0) newPinRecycler.getChildAt(index - 1).findViewById(R.id.otp_field) else null
                                val nextChildView: EditText? =
                                    if (index + 1 < childCount) newPinRecycler.getChildAt(index + 1).findViewById(R.id.otp_field) else null
                                addTextWatcher(prevChildView, childView, nextChildView)
                            }
                        }
                    }
                }

                reenterPinRecycler.addItemDecoration(HorizontalSpaceItemDecoration(itemSpace))
                reenterPinRecycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                reenterPinRecycler.post {
                    reenterPinRecycler.adapter = SimpleListAdapter(PinFieldItemRowType.entries).apply {
                        submitList(createRows(pinLength, reenterPinRecycler.width, itemSpace))
                        reenterPinRecycler.post {
                            val childCount = reenterPinRecycler.childCount
                            for (index in 0 until childCount) {
                                val childView: EditText = reenterPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                                val prevChildView: EditText? =
                                    if (index - 1 >= 0) reenterPinRecycler.getChildAt(index - 1).findViewById(R.id.otp_field) else null
                                val nextChildView: EditText? =
                                    if (index + 1 < childCount) reenterPinRecycler.getChildAt(index + 1).findViewById(R.id.otp_field) else null
                                addTextWatcher(prevChildView, childView, nextChildView)
                            }
                        }
                    }
                }
                commonBind()
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
                var enteredPin = ""
                var childCount = newPinRecycler.childCount
                for (index in 0 until childCount) {
                    val childView: EditText = newPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                    enteredPin += childView.text.toString()
                }
                var reenteredPin = ""
                childCount = reenterPinRecycler.childCount
                for (index in 0 until childCount) {
                    val childView: EditText = reenterPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                    reenteredPin += childView.text.toString()
                }
                if (enteredPin.length == pinLength && reenteredPin.length == pinLength && enteredPin == reenteredPin) {
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