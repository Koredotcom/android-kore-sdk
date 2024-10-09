package com.kore.uploadfile.services

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class FileUploadServiceExecutor private constructor() : Cloneable {
    private var executor: ThreadPoolExecutor? = null
    fun getExecutor(): ThreadPoolExecutor {
        if (executor == null || executor!!.isShutdown) {
            executor = ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                LinkedBlockingQueue()
            )
        }
        return executor!!
    }

    //Restricting cloning of this class
    override fun clone(): Any {
        return CloneNotSupportedException("Clone not supported")
    }

    companion object {
        private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
        private var mInstance: FileUploadServiceExecutor? = null

        @JvmStatic
        @get:Synchronized
        val instance: FileUploadServiceExecutor?
            get() {
                if (mInstance == null) mInstance = FileUploadServiceExecutor()
                return mInstance
            }
    }
}