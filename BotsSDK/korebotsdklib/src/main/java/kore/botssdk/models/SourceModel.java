package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class SourceModel {
    private String title;
    private String url;
    @SerializedName("chunk_id")
    private String chunkId;
    @SerializedName("doc_id")
    private String docId;
    @SerializedName("source_id")
    private String sourceId;
    @SerializedName("source_type")
    private String sourceType;
    @SerializedName("image_url")
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getChunkId() {
        return chunkId;
    }

    public String getDocId() {
        return docId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
