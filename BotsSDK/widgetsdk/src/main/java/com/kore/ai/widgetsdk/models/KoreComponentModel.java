package com.kore.ai.widgetsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class KoreComponentModel implements Parcelable {

    @Expose(serialize = true)
    @SerializedName("componentType")
    private String mediaType;

    @Expose(serialize = true)
    @SerializedName("componentId")
    private String mediaFileName;

    @Expose(serialize = true)
    private String mediaFilePath;

    @Expose(serialize = true)
    private String mediaThumbnail;

    @Expose(serialize = true)
    private String componentBody;

    @Expose(serialize = true)
    private HashMap<String, Object> componentData;

    private String componentDataJson;

    @Expose(serialize = true)
    @SerializedName("componentFileId")
    private String mediafileId;

    //use for multiselection; just to compare with compose footer component
    @Expose(serialize = false)
    private String dropboxFileId;

    @Expose(serialize = true)
    @SerializedName("componentLength")
    private String mediaDuration;

    @Expose(serialize = true)
    private String referenceId;

    @Expose(serialize = false)
    private String messageID;

    @Expose(serialize = true)
    private String componentTitle;

    @Expose(serialize = true)
    private String componentDescription;

    @Expose(serialize = false)
    private boolean delete;

    @Expose(serialize = false)
    private int imageId;

    @Expose(serialize = false)
    private String mediaKey;

    @Expose(serialize = true)
    @SerializedName("templateData")
    private HashMap<String, Object> templateData;

    private boolean showLoader;
    private String fileSize;

    /**
     * @return the mediaType
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * @param mediaType the mediaType to set
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * @return the mediaFileName
     */
    public String getMediaFileName() {
        return mediaFileName;
    }

    /**
     * @param mediaFileName the mediaFileName to set
     */
    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    /**
     * @return the mediaFilePath
     */
    public String getMediaFilePath() {
        return mediaFilePath;
    }

    /**
     * @param mediaFilePath the mediaFilePath to set
     */
    public void setMediaFilePath(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
    }

    /**
     * @return the mediaThumbnail
     */
    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    /**
     * @param mediaThumbnail the mediaThumbnail to set
     */
    public void setMediaThumbnail(String mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    /**
     * @return the mediafileId
     */
    public String getMediafileId() {
        return mediafileId;
    }

    /**
     * @param mediafileId the mediafileId to set
     */
    public void setMediafileId(String mediafileId) {
        this.mediafileId = mediafileId;
    }

    /**
     * @return the mediaDuration
     */
    public String getMediaDuration() {
        return mediaDuration;
    }

    /**
     * @param mediaDuration the mediaDuration to set
     */
    public void setMediaDuration(String mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    /**
     * @return the referenceId
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     * @param referenceId the referenceId to set
     */
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    /**
     * @return the componentBody
     */
    public String getComponentBody() {
        return componentBody;
    }

    public HashMap<String, Object> getComponentData() {
        return componentData;
    }

    public void setComponentData(HashMap<String, Object> componentData) {
        this.componentData = componentData;
    }

    /**
     * @param componentBody the componentBody to set
     */
    public void setComponentBody(String componentBody) {
        this.componentBody = componentBody;
    }

    /**
     * @return the componentTitle
     */
    public String getComponentTitle() {
        return componentTitle;
    }

    /**
     * @param componentTitle the componentTitle to set
     */
    public void setComponentTitle(String componentTitle) {
        this.componentTitle = componentTitle;
    }

    /**
     * @return the componentDescription
     */
    public String getComponentDescription() {
        return componentDescription;
    }

    /**
     * @param componentDescription the componentDescription to set
     */
    public void setComponentDescription(String componentDescription) {
        this.componentDescription = componentDescription;
    }

    /**
     * @return the messageID
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * @param messageID the messageID to set
     */
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getDropboxFileId() {
        return dropboxFileId;
    }

    public void setDropboxFileId(String dropboxFileId) {
        this.dropboxFileId = dropboxFileId;
    }

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }

    public String getComponentDataJson() {
        return componentDataJson;
    }

    public void setComponentDataJson(String componentDataJson) {
        this.componentDataJson = componentDataJson;
    }

    public HashMap<String, Object> getTemplateData() {
        return templateData;
    }

    public void setTemplateData(HashMap<String, Object> templateData) {
        this.templateData = templateData;
    }

    public KoreComponentModel() {

    }

    protected KoreComponentModel(Parcel in) {
        mediaType = in.readString();
        mediaFileName = in.readString();
        mediaFilePath = in.readString();
        mediaThumbnail = in.readString();
        componentBody = in.readString();
        componentData = (HashMap) in.readValue(HashMap.class.getClassLoader());
        componentDataJson = in.readString();
        mediafileId = in.readString();
        dropboxFileId = in.readString();
        mediaDuration = in.readString();
        referenceId = in.readString();
        messageID = in.readString();
        componentTitle = in.readString();
        componentDescription = in.readString();
        delete = in.readByte() != 0x00;
        imageId = in.readInt();
        mediaKey = in.readString();
        templateData = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mediaType);
        dest.writeString(mediaFileName);
        dest.writeString(mediaFilePath);
        dest.writeString(mediaThumbnail);
        dest.writeString(componentBody);
        dest.writeValue(componentData);
        dest.writeString(componentDataJson);
        dest.writeString(mediafileId);
        dest.writeString(dropboxFileId);
        dest.writeString(mediaDuration);
        dest.writeString(referenceId);
        dest.writeString(messageID);
        dest.writeString(componentTitle);
        dest.writeString(componentDescription);
        dest.writeByte((byte) (delete ? 0x01 : 0x00));
        dest.writeInt(imageId);
        dest.writeString(mediaKey);
        dest.writeValue(templateData);
    }

    @SuppressWarnings("unused")
    public static final Creator<KoreComponentModel> CREATOR = new Creator<KoreComponentModel>() {
        @Override
        public KoreComponentModel createFromParcel(Parcel in) {
            return new KoreComponentModel(in);
        }

        @Override
        public KoreComponentModel[] newArray(int size) {
            return new KoreComponentModel[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KoreComponentModel) {

            KoreComponentModel obj1 = this;
            KoreComponentModel obj2 = (KoreComponentModel) obj;

            String type = obj1.getMediaType();

            if (obj1 != null) {
                switch (type) {
                    case KoreMedia.MEDIA_TYPE_IMAGE:
                    case KoreMedia.MEDIA_TYPE_VIDEO:
                        if (obj1.getImageId() != 0) {
                            return obj1.getImageId() == obj2.getImageId();
                        } else if (obj1.getDropboxFileId() != null){
                            return obj1.getDropboxFileId().equalsIgnoreCase(obj2.getDropboxFileId());
                        } else {
                            super.equals(obj);
                        }

                    case KoreMedia.MEDIA_TYPE_CONTACT:
                        if (obj1.getMediaKey() != null) {
                            return obj1.getMediaKey().equalsIgnoreCase(obj2.getMediaKey());
                        } else {
                            return super.equals(obj);
                        }

                    case KoreMedia.MEDIA_TYPE_LINK:
                        if (obj1.getMediafileId() != null) {
                            return obj1.getMediafileId().equalsIgnoreCase(obj2.getMediafileId());
                        } else {
                            return super.equals(obj);
                        }

                    default:
                        return super.equals(obj);
                }
            } else {
                return super.equals(obj);
            }

        } else {
            return super.equals(obj);
        }
    }

    public boolean isShowLoader() {
        return showLoader;
    }

    public void setShowLoader(boolean showLoader) {
        this.showLoader = showLoader;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
