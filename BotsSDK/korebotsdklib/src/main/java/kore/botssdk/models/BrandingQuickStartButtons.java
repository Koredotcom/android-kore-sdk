package kore.botssdk.models;

import java.io.Serializable;
import java.util.ArrayList;

import kore.botssdk.utils.StringUtils;

public class BrandingQuickStartButtons implements Serializable {
    private Boolean show;
    private String style;
    private ArrayList<BrandingQuickStartButtonButtonsModel> buttons;
    private String input;
    private BrandingQuickStartButtonActionModel action;

    public BrandingQuickStartButtons updateWith(BrandingQuickStartButtons configModel) {
        show = configModel.show != null ? configModel.show : show;
        style = !StringUtils.isNullOrEmpty(configModel.style) ? configModel.style : style;
        input = !StringUtils.isNullOrEmpty(configModel.input) ? configModel.input : input;
        action = action != null && configModel.action != null ? action.updateWith(configModel.action) : action;
        return this;
    }

    public void setAction(BrandingQuickStartButtonActionModel action) {
        this.action = action;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public BrandingQuickStartButtonActionModel getAction() {
        return action;
    }

    public String getInput() {
        return input;
    }

    public boolean isShow() {
        return show;
    }

    public String getStyle() {
        return style;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setButtons(ArrayList<BrandingQuickStartButtonButtonsModel> buttons) {
        this.buttons = buttons;
    }

    public ArrayList<BrandingQuickStartButtonButtonsModel> getButtons() {
        return buttons;
    }
}
