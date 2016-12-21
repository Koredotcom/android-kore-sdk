package kore.botssdk.models;

/**
 * Created by Anil Kumar on 12/20/2016.
 */
public class BotCustomListModel {
    String title;
    String subtitle;
    String image_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String link) {
        this.image_url = link;
    }
}
