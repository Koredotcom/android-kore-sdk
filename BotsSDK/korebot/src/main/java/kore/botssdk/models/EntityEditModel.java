package kore.botssdk.models;

public class EntityEditModel {

    String type;
    String postback;

    String icon;
    String title;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostback() {
        return postback;
    }

    public void setPostback(String postback) {
        this.postback = postback;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String isIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
