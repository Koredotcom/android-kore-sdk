package kore.botssdk.models;

import java.io.Serializable;
import java.util.ArrayList;

import kore.botssdk.utils.StringUtils;

public class StaticLinksModel implements Serializable {
    Boolean show;
    String type;
    String layout;
    private ArrayList<BrandingQuickStartButtonButtonsModel> links;

    public StaticLinksModel updateWith(StaticLinksModel configModel) {
        show = configModel.show != null ? configModel.show : show;
        type = !StringUtils.isNullOrEmpty(configModel.type) ? configModel.type : type;
        layout = !StringUtils.isNullOrEmpty(configModel.layout) ? configModel.layout : layout;
        links = configModel.links != null && !configModel.links.isEmpty() ? configModel.links : links;
        return this;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setLinks(ArrayList<BrandingQuickStartButtonButtonsModel> links) {
        this.links = links;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShow() {
        return show != null ? show : false;
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
