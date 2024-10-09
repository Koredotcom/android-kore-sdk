package com.kore.uploadfile.managers

import com.kore.uploadfile.models.ChunkInfo
import com.kore.uploadfile.models.FileUploadInfo

class BotDBManager private constructor() : Cloneable {
    val fileUploadInfoMap: MutableMap<String, FileUploadInfo> = HashMap()
    val chunkInfoMap: MutableMap<String, MutableMap<Int, ChunkInfo>> = HashMap()

    override fun clone(): Any {
        return CloneNotSupportedException("Clone not supported")
    }

    companion object {
        @Volatile
        private var botDBManager: BotDBManager? = null

        @JvmStatic
        val instance: BotDBManager
            get() {
                if (botDBManager == null) botDBManager = BotDBManager()
                return botDBManager!!
            }
    }
}