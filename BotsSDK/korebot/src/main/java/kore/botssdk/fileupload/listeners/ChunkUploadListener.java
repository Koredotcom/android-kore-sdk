package kore.botssdk.fileupload.listeners;

public interface ChunkUploadListener {

	void notifyChunkUploadCompleted(String chunkNo, String fileName);

}
