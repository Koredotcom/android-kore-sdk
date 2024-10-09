package com.kore.uploadfile.upload

import com.kore.uploadfile.listeners.FileUploadedListener

interface Work {
    fun initiateFileUpload(file: FileUploadedListener?)
}