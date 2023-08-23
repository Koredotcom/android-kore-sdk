package kore.botssdk.models;

import java.util.ArrayList;

public class BrandingQuickStartButtons
{
    private boolean show;
    private String style;
    private ArrayList<BrandingQuickStartButtonButtonsModel> buttons;
    private String input;
    private BrandingQuickStartButtonActionModel action;

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
