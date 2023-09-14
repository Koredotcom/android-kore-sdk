package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class StaticLinksModel implements Serializable {
    private boolean show;
    private String type;
    private ArrayList<BrandingQuickStartButtonButtonsModel> links;

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setLinks(@NonNull ArrayList<BrandingQuickStartButtonButtonsModel> links) {
        this.links = links;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public boolean isShow() {
        return show;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public ArrayList<BrandingQuickStartButtonButtonsModel> getLinks() {
        return links;
    }
}
