package com.kore.ui.audiocodes.webrtcclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.activities.BaseAppCompatActivity
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.audiocodes.webrtcclient.structure.ImageViewWithText
import com.kore.ui.databinding.MainFragmentDialerBinding

class DialerFragment : BaseFragment(), FragmentLifecycle, OnClickListener, OnLongClickListener {
    private var binding: MainFragmentDialerBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.main_fragment_dialer, container, false)
        initGui()
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initGui() {
        binding?.apply {
            dialerButtonKeypad0.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad1.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad2.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad3.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad4.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad5.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad6.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad7.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad8.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad9.setOnClickListener(this@DialerFragment)
            dialerButtonKeypadAsterisk.setOnClickListener(this@DialerFragment)
            dialerButtonKeypadHash.setOnClickListener(this@DialerFragment)
            dialerButtonKeypadBack.setOnClickListener(this@DialerFragment)
            dialerButtonVideoCall.setOnClickListener(this@DialerFragment)
            dialerButtonCall.setOnClickListener(this@DialerFragment)
            dialerButtonKeypad0.setOnLongClickListener(this@DialerFragment)
            dialerButtonKeypadBack.setOnLongClickListener(this@DialerFragment)

            val debugMode = AppUtils.getStringBoolean(requireContext(), R.string.enable_debug_mode)
            if (debugMode) {
                dialerEditTextCallNumber.setText(getString(R.string.default_dialer_number))
            }
            dialerEditTextCallNumber.setSelection(dialerEditTextCallNumber.text.length)
        }
    }

    private fun updateCallNumber(callNumberEditText: EditText?, newString: String?) {
        if (callNumberEditText != null && newString != null) {
            val focusLocation = callNumberEditText.selectionStart
            callNumberEditText.text = callNumberEditText.text.append(newString)
            callNumberEditText.setSelection(focusLocation + newString.length)
        }
    }

    override fun onPauseFragment() {}
    override fun onResumeFragment() {}
    override fun onClick(clickedView: View?) {
        var currentNumber = ""
        if (clickedView == null) {
            return
        }
        val dialerEditTextCallNumber = binding?.dialerEditTextCallNumber ?: return
        when (clickedView.id) {
            R.id.dialer_button_keypad_back -> {
                var text = dialerEditTextCallNumber.text.toString()
                if (text.isNotEmpty()) {
                    text = text.substring(0, text.length - 1)
                    dialerEditTextCallNumber.setText(text)
                    dialerEditTextCallNumber.setSelection(text.length)
                }
            }

            R.id.dialer_button_call,
            R.id.dialer_button_video_call -> {
                if (dialerEditTextCallNumber.text.toString().isEmpty()) return
                val videoCall = clickedView.id == R.id.dialer_button_video_call
                dialerEditTextCallNumber.setText(dialerEditTextCallNumber.text.toString().replace("#", ""))
                if (dialerEditTextCallNumber.text.isEmpty()) {
                    val callEntryList = (requireActivity() as BaseAppCompatActivity).getDataBase().getEntries(1)
                    if (callEntryList.isNotEmpty()) currentNumber = callEntryList[0].contactNumber
                } else {
                    if (!acManager.isRegisterState && Prefs.getAutoLogin(requireContext())) {
                        Toast.makeText(context, R.string.no_registration, Toast.LENGTH_SHORT).show()
                    } else {
                        acManager.callNumber(requireContext(), dialerEditTextCallNumber.text.toString(), videoCall)
                    }
                }
            }

            else -> currentNumber = (clickedView as ImageViewWithText).valueTextView?.text.toString().split("\n")[0]
        }
        if (currentNumber.isNotEmpty()) {
            updateCallNumber(dialerEditTextCallNumber, currentNumber)
        }
    }

    override fun onLongClick(clickedView: View): Boolean {
        val dialerEditTextCallNumber = binding?.dialerEditTextCallNumber ?: return false
        var tempNumber = ""
        var result = false
        val id = clickedView.id
        if (id == R.id.dialer_button_keypad_0) {
            tempNumber = "+"
            result = true
        } else if (id == R.id.dialer_button_keypad_back) {
            tempNumber = ""
            dialerEditTextCallNumber.setText(tempNumber)
            result = true
        }
        if (result) {
            updateCallNumber(dialerEditTextCallNumber, tempNumber)
        }
        return result
    }
}