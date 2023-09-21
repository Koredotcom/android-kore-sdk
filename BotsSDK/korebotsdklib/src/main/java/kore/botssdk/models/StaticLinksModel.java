package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class StaticLinksModel implements Serializable {
    boolean show;
    String type;
    String layout;
    private ArrayList<BrandingQuickStartButtonButtonsModel> links;

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setLinks( ArrayList<BrandingQuickStartButtonButtonsModel> links) {
        this.links = links;
    }
    public void setType( String type) {
        this.type = type;
    }
    public boolean isShow() {
        return show;
    }
    public String getType() {
        return type;
    }
    public String getLayout() {
        return layout;
    }
    public ArrayList<BrandingQuickStartButtonButtonsModel> getLinks() {
        return links;
    }
}
