package com.kore.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
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
import com.kore.model.constants.BotResponseConstants.DESCRIPTION
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.MOBILE_NUMBER
import com.kore.model.constants.BotResponseConstants.OTP_BUTTONS
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.model.constants.BotResponseConstants.PIN_LENGTH
import com.kore.ui.R
import com.kore.ui.bottomsheet.row.PinFieldItemRowType
import com.kore.ui.bottomsheet.row.pinfielditem.PinFieldItemRow.Companion.createRows
import com.kore.ui.databinding.OtpTemplateBottomSheetBinding
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow.HorizontalSpaceItemDecoration

class OtpTemplateBottomSheet : BottomSheetDialogFragment() {
    private var binding: OtpTemplateBottomSheetBinding? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var payload: Map<String, Any>
    private var isLastItem: Boolean = false
    private lateinit var actionEvent: (event: UserActionEvent) -> Unit
    private val itemSpace = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.otp_template_bottom_sheet, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            otpTemplate.title.isVisible = payload[KEY_TITLE] != null
            otpTemplate.description.isVisible = payload[DESCRIPTION] != null
            otpTemplate.title.text = payload[KEY_TITLE] as String?
            otpTemplate.description.text = payload[DESCRIPTION] as String?
            otpTemplate.phoneNumber.text = payload[MOBILE_NUMBER] as String?
            val pinLength = (payload[PIN_LENGTH] as Double).toInt()
            otpTemplate.otpRecycler.addItemDecoration(HorizontalSpaceItemDecoration(itemSpace))
            otpTemplate.otpRecycler.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
            otpTemplate.otpRecycler.post {
                otpTemplate.otpRecycler.adapter = SimpleListAdapter(PinFieldItemRowType.values().asList()).apply {
                    submitList(createRows(pinLength, otpTemplate.otpRecycler.width, itemSpace))
                    otpTemplate.otpRecycler.post {
                        val childCount = otpTemplate.otpRecycler.childCount
                        for (index in 0 until childCount) {
                            val childView: EditText = otpTemplate.otpRecycler.getChildAt(index).findViewById(R.id.otp_field)
                            val prevChildView: EditText? =
                                if (index - 1 >= 0) otpTemplate.otpRecycler.getChildAt(index - 1).findViewById(R.id.otp_field) else null
                            val nextChildView: EditText? =
                                if (index + 1 < childCount) otpTemplate.otpRecycler.getChildAt(index + 1).findViewById(R.id.otp_field) else null
                            addTextWatcher(prevChildView, childView, nextChildView)
                        }
                    }
                }
            }
            (payload[OTP_BUTTONS] as List<Map<String, String>>?)?.let { list ->
                otpTemplate.submit.text = list[0][KEY_TITLE]
                val content = SpannableString(list[1][KEY_TITLE])
                content.setSpan(UnderlineSpan(), 0, content.length, 0)
                otpTemplate.resendOtp.text = content
                otpTemplate.submit.setOnClickListener {
                    var otp = ""
                    val childCount = otpTemplate.otpRecycler.childCount
                    for (index in 0 until childCount) {
                        val childView: EditText = otpTemplate.otpRecycler.getChildAt(index).findViewById(R.id.otp_field)
                        otp += childView.text.toString()
                    }
                    if (otp.length == pinLength) {
                        bottomSheetDialog?.dismiss()
                        actionEvent(BotChatEvent.SendMessage(otp.getDotMessage(), otp))
                    }
                }
                otpTemplate.resendOtp.setOnClickListener {
                    actionEvent(BotChatEvent.SendMessage(list[1][PAYLOAD].toString(), list[1][PAYLOAD]))
                }
            }
            otpTemplate.close.setOnClickListener {
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
        show(manager, OtpTemplateBottomSheet::class.java.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}