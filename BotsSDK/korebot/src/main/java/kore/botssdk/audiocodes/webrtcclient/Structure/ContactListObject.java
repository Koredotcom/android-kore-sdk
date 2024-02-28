package kore.botssdk.audiocodes.webrtcclient.Structure;


public class ContactListObject {

    private String name;
    private String number;
    private String photoURI;
    private String photoThumbnailURI;

    public ContactListObject(String name, String number, String photoURI, String photoThumbnailURI) {
        this.name = name;
        this.number = number;
        this.photoURI = photoURI;
        this.photoThumbnailURI = photoThumbnailURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
