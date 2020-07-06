package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class FileLookUpData {
    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Object getOwners() {
        return owners;
    }

    public void setOwners(Object owners) {
        this.owners = owners;
    }

    private String ext;
    private String thumbnail;
    private String modifiedTime;

//    private List<FileOwner> owners;
    private Object owners;

}
