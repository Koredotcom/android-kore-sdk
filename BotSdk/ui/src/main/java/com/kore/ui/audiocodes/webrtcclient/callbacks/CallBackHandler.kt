package com.kore.ui.audiocodes.webrtcclient.callbacks

import android.annotation.SuppressLint
import com.audiocodes.mv.webrtcsdk.im.InstanceMessageStatus
import com.audiocodes.mv.webrtcsdk.session.CallState

object CallBackHandler {
    private val loginStateChangeCallBacks: MutableList<LoginStateChanged> = ArrayList()
    private val callStateChangeCallBacks: MutableList<CallStateChanged> = ArrayList()
    private val chatCallBacks: MutableList<ChatCallback> = ArrayList()
    private val tabChangeCallbacks: MutableList<TabChangeCallback> = ArrayList()

    @JvmStatic
    fun loginStateChange(state: Boolean) {
        for (i in loginStateChangeCallBacks.indices) {
            loginStateChangeCallBacks[i].loginStateChange(state)
        }
    }

    @JvmStatic
    fun unregisterLoginStateChange(cb: LoginStateChanged) {
        loginStateChangeCallBacks.remove(cb)
    }

    @JvmStatic
    fun registerLginStateChange(cb: LoginStateChanged) {
        loginStateChangeCallBacks.add(cb)
    }

    @SuppressLint("UnknownNullness")
    fun callStateChanged(callState: CallState) {
        for (i in callStateChangeCallBacks.indices) {
            callStateChangeCallBacks[i].callStateChanged(callState)
        }
    }

    fun unregisterCallStateChanged(cb: CallStateChanged) {
        callStateChangeCallBacks.remove(cb)
    }

    fun registerCallStateChanged(cb: CallStateChanged) {
        callStateChangeCallBacks.clear()
        callStateChangeCallBacks.add(cb)
    }

    @JvmStatic
    fun onNewMessage(user: String, message: String) {
        for (i in chatCallBacks.indices) {
            chatCallBacks[i].onNewMessage(user, message)
        }
    }

    @JvmStatic
    @SuppressLint("UnknownNullness")
    fun onMessageStatus(instanceMessageStatus: InstanceMessageStatus, ID: Long) {
        for (i in chatCallBacks.indices) {
            chatCallBacks[i].onMessageStatus(instanceMessageStatus, ID)
        }
    }

    @JvmStatic
    @SuppressLint("UnknownNullness")
    fun unregisterChatCallback(chatCallback: ChatCallback) {
        chatCallBacks.remove(chatCallback)
    }

    @JvmStatic
    @SuppressLint("UnknownNullness")
    fun registerChatCallback(chatCallback: ChatCallback) {
        chatCallBacks.clear()
        chatCallBacks.add(chatCallback)
    }

    @JvmStatic
    fun onTabChange(tabIndex: Int) {
        for (i in tabChangeCallbacks.indices) {
            tabChangeCallbacks[i].onTabChange(tabIndex)
        }
    }

    @SuppressLint("UnknownNullness")
    fun unregisterTabChangeCallback(tabChangeCallback: TabChangeCallback): Boolean {
        return chatCallBacks.remove(tabChangeCallback as ChatCallback)
    }

    @SuppressLint("UnknownNullness")
    fun registerTabChangeCallback(tabChangeCallback: TabChangeCallback) {
        tabChangeCallbacks.clear()
        tabChangeCallbacks.add(tabChangeCallback)
    }

    //LoginStateChanged
    fun interface LoginStateChanged {
        fun loginStateChange(state: Boolean)
    }

    //CallStateChanged
    interface CallStateChanged {
        fun callStateChanged(callState: CallState)
    }

    //ChatCallback
    interface ChatCallback {
        fun onNewMessage(user: String, message: String)
        fun onMessageStatus(instanceMessageStatus: InstanceMessageStatus, ID: Long)
    }

    //TabChangeCallback
    fun interface TabChangeCallback {
        fun onTabChange(tabIndex: Int)
    }
}
