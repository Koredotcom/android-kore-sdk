package com.kore.uploadfile.upload

import com.kore.uploadfile.listeners.FileUploadedListener
import okhttp3.internal.notifyAll
import okhttp3.internal.wait
import java.util.Vector

class KoreWorker private constructor() : Runnable, Cloneable {
    private var quit = false
    private val queue: Vector<Work> = Vector()

    @Volatile
    private var isInitial = true

    @Volatile
    var isCallBack = false
    private val next: Work?
        get() {
            var task: Work? = null
            if (!queue.isEmpty()) {
                task = queue.firstElement()
            }
            return task
        }
    private val fileUploaded: FileUploadedListener = object : FileUploadedListener {
        override fun fileUploaded() {
            synchronized(instance!!) { isCallBack = true }
        }
    }

    override fun run() {
        while (!quit) {
            if (isInitial || isCallBack) {
                val task = next
                if (task != null) {
                    synchronized(instance!!) { isCallBack = false }
                    task.initiateFileUpload(fileUploaded)
                    synchronized(queue) { queue.removeElement(task) }
                    isInitial = false
                } else {
                    synchronized(queue) {
                        try {
                            queue.wait()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
            } else if (queue.size == 0) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun startThread() {
        Thread(instance, "KOREWORKER").start()
    }

    fun addTask(task: Work) {
        synchronized(queue) {
            if (!quit) {
                queue.addElement(task)
                if (isInitial) startThread()
                if (isCallBack) {
                    queue.notifyAll()
                }
            }
        }
    }

    override fun clone(): Any {
        return CloneNotSupportedException("Clone not supported")
    }

    companion object {
        private var instance: KoreWorker? = null
        @Synchronized
        fun getInstance(): KoreWorker {
            if (instance == null) {
                instance = KoreWorker()
            }
            return instance!!
        }
    }
}