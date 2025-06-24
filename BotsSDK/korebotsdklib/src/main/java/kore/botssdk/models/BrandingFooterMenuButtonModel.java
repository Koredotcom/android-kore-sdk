package kore.botssdk.models;

import java.io.Serializable;
import java.util.ArrayList;

import kore.botssdk.utils.StringUtils;

public class BrandingFooterMenuButtonModel implements Serializable {
    private Boolean show;
    private String icon;
    private ArrayList<BrandingQuickStartButtonActionModel> actions;

    public BrandingFooterMenuButtonModel updateWith(BrandingFooterMenuButtonModel configModel) {
        show = configModel.show != null ? configModel.show : show;
        icon = !StringUtils.isNullOrEmpty(configModel.icon) ? configModel.icon : icon;
        actions = configModel.actions != null && !configModel.actions.isEmpty() ? configModel.actions : actions;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public ArrayList<BrandingQuickStartButtonActionModel> getActions() {
        return actions;
    }

    public boolean isShow() {
        return show != null ? show : false;
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
