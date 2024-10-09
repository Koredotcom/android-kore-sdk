package com.kore.data.repository.dynamicurl

import java.io.File

interface DynamicUrlRepository {
    suspend fun downloadFile(url: String, destinationFile: File, onProgress: (progress: Int)-> Unit)
}