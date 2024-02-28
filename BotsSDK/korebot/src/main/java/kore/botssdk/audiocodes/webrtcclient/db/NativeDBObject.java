package kore.botssdk.audiocodes.webrtcclient.db;


import androidx.annotation.NonNull;

import java.util.List;

public class NativeDBObject implements Comparable<NativeDBObject>{

    private String id;
    private String displayName;
    private String photoURI;
    private String photoThumbnailURI;
    private List<NativeDBPhones> phones;


    public NativeDBObject()
    {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public String getPhotoThumbnailURI() {
        return photoThumbnailURI;
    }

    public void setPhotoThumbnailURI(String photoThumbnailURI) {
        this.photoThumbnailURI = photoThumbnailURI;
    }

    public void setPhones(List<NativeDBPhones> phones) {
        this.phones = phones;
    }

    public List<NativeDBPhones> getPhones() {
        return phones;
    }

    @Override
    public int compareTo(@NonNull NativeDBObject another) {
        if(this.getDisplayName()!=null && another.getDisplayName()!=null)
        {
            return this.getDisplayName().toLowerCase().compareTo(another.getDisplayName().toLowerCase());
        }
        return 1;
    }

}
