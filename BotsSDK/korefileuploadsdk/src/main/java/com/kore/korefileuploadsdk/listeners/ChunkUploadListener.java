package com.kore.korefileuploadsdk.listeners;

public interface ChunkUploadListener {

	/**
	 * 
	 * @param chunkNo
	 * @param fileName
	 */
	void notifyChunkUploadCompleted(String chunkNo, String fileName);

}
