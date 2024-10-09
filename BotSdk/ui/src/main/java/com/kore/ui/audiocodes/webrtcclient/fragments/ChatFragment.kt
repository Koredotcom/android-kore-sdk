package com.kore.ui.audiocodes.webrtcclient.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.audiocodes.mv.webrtcsdk.im.InstanceMessageStatus
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.ChatCallback
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.registerChatCallback
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.unregisterChatCallback
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils
import com.kore.ui.databinding.MainFragmentChatBinding

@SuppressLint("UnknownNullness")
class ChatFragment : BaseFragment(), FragmentLifecycle {
    private var binding: MainFragmentChatBinding? = null

    private val chatCallback: ChatCallback = object : ChatCallback {
        override fun onNewMessage(user: String, message: String) {
            handler.post {
                binding?.apply {
                    chatLastMessageContactTextview.text = user
                    chatLastMessageTextview.text = message
                }
            }
        }

        override fun onMessageStatus(instanceMessageStatus: InstanceMessageStatus, ID: Long) {
            handler.post {
                Toast.makeText(requireContext(), getString(R.string.chat_message_sent) + ": " + instanceMessageStatus, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.main_fragment_chat, container, false)
        registerChatCallback(chatCallback)
        initGui()
        return binding?.root
    }

    private fun initGui() {
        binding?.apply {
            chatSendButton.setOnClickListener {
                val user = chatContactEdittext.text.toString()
                val text = chatMessageEdittext.text.toString()
                if (user.isNotEmpty() && text.isNotEmpty()) {
                    if (!acManager.isRegisterState) {
                        Toast.makeText(context, R.string.no_registration, Toast.LENGTH_SHORT).show()
                    } else {
                        acManager.sendInstantMessage(user, text)
                    }
                }
            }
            if (AppUtils.getStringBoolean(requireContext(), R.string.enable_debug_mode)) {
                chatContactEdittext.setText(getString(R.string.sip_default_chat_contact))
                chatMessageEdittext.setText(getString(R.string.sip_default_chat_message))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterChatCallback(chatCallback)
        binding = null
    }

    override fun onPauseFragment() {}
    override fun onResumeFragment() {}
}