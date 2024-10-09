package com.kore.uploadfile.listeners

interface ChunkUploadListener {
    /**
     * @param chunkNo
     * @param fileName
     */
    fun notifyChunkUploadCompleted(chunkNo: String?, fileName: String)
}