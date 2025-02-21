package com.kore.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.getDotMessage
import com.kore.model.constants.BotResponseConstants.ENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.REENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.RESET_BUTTONS
import com.kore.model.constants.BotResponseConstants.WARNING_MESSAGE
import com.kore.ui.R
import com.kore.ui.databinding.PinResetTemplateBottomSheetBinding

class PinResetTemplateBottomSheet : BottomSheetDialogFragment() {
    private var binding: PinResetTemplateBottomSheetBinding? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var payload: Map<String, Any>
    private var isLastItem: Boolean = false
    private lateinit var actionEvent: (event: UserActionEvent) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.pin_reset_template_bottom_sheet, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            template.title.isVisible = payload[KEY_TITLE] != null
            template.title.text = payload[KEY_TITLE] as String?
            template.enterNewPin.text = payload[ENTER_PIN_TITLE] as String?
            template.reenterNewPin.text = payload[REENTER_PIN_TITLE] as String?
            addTextWatcher(null, template.newPinField1, template.newPinField2)
            addTextWatcher(template.newPinField1, template.newPinField2, template.newPinField3)
            addTextWatcher(template.newPinField2, template.newPinField3, template.newPinField4)
            addTextWatcher(template.newPinField3, template.newPinField4, template.reenterPinField1)
            addTextWatcher(null, template.reenterPinField1, template.reenterPinField2)
            addTextWatcher(template.reenterPinField1, template.reenterPinField2, template.reenterPinField3)
            addTextWatcher(template.reenterPinField2, template.reenterPinField3, template.reenterPinField4)
            addTextWatcher(template.reenterPinField3, template.reenterPinField4, null)
            (payload[RESET_BUTTONS] as List<Map<String, String>>?)?.let { list ->
                template.reset.text = list[0][KEY_TITLE]
                template.reset.setOnClickListener {
                    val enteredPin =
                        template.newPinField1.text.toString() + template.newPinField2.text.toString() +
                                template.newPinField3.text.toString() + template.newPinField4.text.toString()
                    val reenteredPin =
                        template.reenterPinField1.text.toString() + template.reenterPinField2.text.toString() +
                                template.reenterPinField3.text.toString() + template.reenterPinField4.text.toString()
                    if (enteredPin.length == 4 && reenteredPin.length == 4 && enteredPin == reenteredPin) {
                        bottomSheetDialog?.dismiss()
                        actionEvent(BotChatEvent.SendMessage(enteredPin.getDotMessage(), enteredPin))
                    } else {
                        Toast.makeText(root.context, payload[WARNING_MESSAGE].toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            template.close.setOnClickListener {
                bottomSheetDialog?.dismiss()
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog?.setOnShowListener { dialogInterface ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                bottomSheet.layoutParams.height = resources.displayMetrics.heightPixels
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.peekHeight = 400.dpToPx(requireContext())
            }
        }

        return bottomSheetDialog!!
    }

    fun showData(
        payload: Map<String, Any>,
        isLastItem: Boolean,
        manager: FragmentManager,
        actionEvent: (event: UserActionEvent) -> Unit
    ) {
        this.payload = payload
        this.isLastItem = isLastItem
        this.actionEvent = actionEvent
        show(manager, PinResetTemplateBottomSheet::class.java.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}