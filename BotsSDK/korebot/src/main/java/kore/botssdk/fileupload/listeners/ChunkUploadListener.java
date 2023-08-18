package kore.botssdk.fileupload.listeners;

public interface ChunkUploadListener {

	/**
	 * 
	 * @param chunkNo
	 * @param fileName
	 */
	void notifyChunkUploadCompleted(String chunkNo, String fileName);

}
