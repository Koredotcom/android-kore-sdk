package kore.botssdk.models;

import java.util.ArrayList;

public class BrandingFooterMenuButtonModel {
    private boolean show;
    private String icon;
    private ArrayList<BrandingQuickStartButtonActionModel> actions;

    public String getIcon() {
        return icon;
    }

    public ArrayList<BrandingQuickStartButtonActionModel> getActions() {
        return actions;
    }

    public boolean isShow() {
        return show;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setActions(ArrayList<BrandingQuickStartButtonActionModel> actions) {
        this.actions = actions;
    }
}
