package com.kore.interfaces

interface ComposeFooterInterface {
    /**
     * @param message : Title and payload, Both are same
     */
    fun onSendClick(message: String?, isFromUtterance: Boolean)

    /**
     * @param message : Title of the button
     * @param payload : Payload to be send
     */
    fun onSendClick(message: String?, payload: String?, isFromUtterance: Boolean)
    fun onSendClick(
        message: String?,
        attachments: ArrayList<HashMap<String?, String?>?>?,
        isFromUtterance: Boolean
    )

    fun sendWithSomeDelay(message: String?, payload: String?, time: Long, isScrollUpNeeded: Boolean)
    fun copyMessageToComposer(text: String?, isForOnboard: Boolean)

    fun onDeepLinkClicked(url: String?)
}