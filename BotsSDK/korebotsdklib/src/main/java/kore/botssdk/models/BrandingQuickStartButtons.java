package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class BrandingQuickStartButtons implements Serializable
{
    private boolean show;
    private String style;
    private ArrayList<BrandingQuickStartButtonButtonsModel> buttons;
    private String input;
    private BrandingQuickStartButtonActionModel action;

    public void setAction(@NonNull BrandingQuickStartButtonActionModel action) {
        this.action = action;
    }

    public void setInput(@NonNull String input) {
        this.input = input;
    }

    @NonNull
    public BrandingQuickStartButtonActionModel getAction() {
        return action;
    }

    @NonNull
    public String getInput() {
        return input;
    }

    public boolean isShow() {
        return show;
    }

    @NonNull
    public String getStyle() {
        return style;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setStyle(@NonNull String style) {
        this.style = style;
    }

    public void setButtons(@NonNull ArrayList<BrandingQuickStartButtonButtonsModel> buttons) {
        this.buttons = buttons;
    }

    @NonNull
    public ArrayList<BrandingQuickStartButtonButtonsModel> getButtons() {
        return buttons;
    }
}
