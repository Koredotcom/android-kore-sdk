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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.getDotMessage
import com.kore.model.constants.BotResponseConstants.ENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.PIN_LENGTH
import com.kore.model.constants.BotResponseConstants.REENTER_PIN_TITLE
import com.kore.model.constants.BotResponseConstants.RESET_BUTTONS
import com.kore.model.constants.BotResponseConstants.WARNING_MESSAGE
import com.kore.ui.R
import com.kore.ui.bottomsheet.row.PinFieldItemRowType
import com.kore.ui.bottomsheet.row.pinfielditem.PinFieldItemRow.Companion.createRows
import com.kore.ui.databinding.ResetPinTemplateBottomSheetBinding
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow.HorizontalSpaceItemDecoration

class ResetPinTemplateBottomSheet : BottomSheetDialogFragment() {
    private var binding: ResetPinTemplateBottomSheetBinding? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var payload: Map<String, Any>
    private var isLastItem: Boolean = false
    private lateinit var actionEvent: (event: UserActionEvent) -> Unit
    private val itemSpace = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.reset_pin_template_bottom_sheet, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            template.title.isVisible = payload[KEY_TITLE] != null
            template.title.text = payload[KEY_TITLE] as String?
            template.enterNewPin.text = payload[ENTER_PIN_TITLE] as String?
            template.reenterNewPin.text = payload[REENTER_PIN_TITLE] as String?

            val pinLength = (payload[PIN_LENGTH] as Double).toInt()
            template.newPinRecycler.addItemDecoration(HorizontalSpaceItemDecoration(itemSpace))
            template.newPinRecycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            template.newPinRecycler.post {
                template.newPinRecycler.adapter = SimpleListAdapter(PinFieldItemRowType.values().asList()).apply {
                    submitList(createRows(pinLength, template.newPinRecycler.width, itemSpace))
                    template.newPinRecycler.post {
                        val childCount = template.newPinRecycler.childCount
                        for (index in 0 until childCount) {
                            val childView: EditText = template.newPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                            val prevChildView: EditText? =
                                if (index - 1 >= 0) template.newPinRecycler.getChildAt(index - 1).findViewById(R.id.otp_field) else null
                            val nextChildView: EditText? =
                                if (index + 1 < childCount) template.newPinRecycler.getChildAt(index + 1).findViewById(R.id.otp_field) else null
                            addTextWatcher(prevChildView, childView, nextChildView)
                        }
                    }
                }
            }

            template.reenterPinRecycler.addItemDecoration(HorizontalSpaceItemDecoration(itemSpace))
            template.reenterPinRecycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            template.reenterPinRecycler.post {
                template.reenterPinRecycler.adapter = SimpleListAdapter(PinFieldItemRowType.values().asList()).apply {
                    submitList(createRows(pinLength, template.reenterPinRecycler.width, itemSpace))
                    template.reenterPinRecycler.post {
                        val childCount = template.reenterPinRecycler.childCount
                        for (index in 0 until childCount) {
                            val childView: EditText = template.reenterPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                            val prevChildView: EditText? =
                                if (index - 1 >= 0) template.reenterPinRecycler.getChildAt(index - 1).findViewById(R.id.otp_field) else null
                            val nextChildView: EditText? =
                                if (index + 1 < childCount) template.reenterPinRecycler.getChildAt(index + 1).findViewById(R.id.otp_field) else null
                            addTextWatcher(prevChildView, childView, nextChildView)
                        }
                    }
                }
            }

            (payload[RESET_BUTTONS] as List<Map<String, String>>?)?.let { list ->
                template.reset.text = list[0][KEY_TITLE]
                template.reset.setOnClickListener {
                    var enteredPin = ""
                    var childCount = template.newPinRecycler.childCount
                    for (index in 0 until childCount) {
                        val childView: EditText = template.newPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                        enteredPin += childView.text.toString()
                    }
                    var reenteredPin = ""
                    childCount = template.reenterPinRecycler.childCount
                    for (index in 0 until childCount) {
                        val childView: EditText = template.reenterPinRecycler.getChildAt(index).findViewById(R.id.otp_field)
                        reenteredPin += childView.text.toString()
                    }
                    if (enteredPin.length == pinLength && reenteredPin.length == pinLength && enteredPin == reenteredPin) {
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
        show(manager, ResetPinTemplateBottomSheet::class.java.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}