package com.kore.uploadfile.listeners

import java.util.Hashtable

interface FileTokenListener {
    /**
     * Callback for when file token received successfully
     */
    fun fileTokenReceivedSuccessfully(hsh: Hashtable<String, String>)

    /**
     * Callback for when file token service unsuccessful
     */
    fun fileTokenReceivedWithFailure(errCode: String, reason: String)
}