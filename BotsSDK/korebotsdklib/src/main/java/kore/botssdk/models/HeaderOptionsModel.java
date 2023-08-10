package kore.botssdk.models;

import java.util.ArrayList;

public class HeaderOptionsModel {

    private String type;
    private String text;
    private UrlModel url;
    private Widget.Button button;
    private ArrayList<Widget.Button> menu;
    private ArrayList<Widget.Button> dropdownOptions;
    private BotListLayoutModel layout;
    private String icon;
    private String contenttype;
    private String title;
    private String value;
    private ButtonStyleModel buttonStyles;
    private ButtonStyleModel styles;
    private String payload;

    public ArrayList<Widget.Button> getDropdownOptions() {
        return dropdownOptions;
    }

    public void setDropdownOptions(ArrayList<Widget.Button> dropdownOptions) {
        this.dropdownOptions = dropdownOptions;
    }

    public BotListLayoutModel getLayout() {
        return layout;
    }

    public ButtonStyleModel getButtonStyles() {
        return buttonStyles;
    }

    public ButtonStyleModel getStyles() {
        return styles;
    }

    public String getContenttype() {
        return contenttype;
    }

    public String getIcon() {
        return icon;
    }

    public String getPayload() {
        return payload;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public void setButtonStyles(ButtonStyleModel buttonStyles) {
        this.buttonStyles = buttonStyles;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setLayout(BotListLayoutModel layout) {
        this.layout = layout;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setStyles(ButtonStyleModel styles) {
        this.styles = styles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UrlModel getUrl() {
        return url;
    }

    public void setUrl(UrlModel url) {
        this.url = url;
    }

    public Widget.Button getButton() {
        return button;
    }

    public void setButton(Widget.Button button) {
        this.button = button;
    }

    public ArrayList<Widget.Button> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Widget.Button> menu) {
        this.menu = menu;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    private ImageModel image;

}
