package kore.botssdk.models;

import java.util.ArrayList;

public class WidgetListElementModel {
    private ImageModel image;
    private String title;
    private String subtitle;
    private String description;
    private HeaderOptionsModel value;

    private String stock_availability = "";

    private String topRated = "";

    private ViewStyles templateStyles;

    private ViewStyles availabilityStyles;

    private ViewStyles topRatedStyles;

    private Object buttonsLayout;
    private String icon;
    private String theme;
    private ArrayList<ContentModel> content = null;
    private boolean hasMore;
    private ArrayList<ContentModel> details = null;
    private Widget.DefaultAction default_action;
    private ArrayList<Widget.Button> buttons = null;

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

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

    public HeaderOptionsModel getValue() {
        return value;
    }

    public void setValue(HeaderOptionsModel value) {
        this.value = value;
    }

    public ArrayList<ContentModel> getContent() {
        return content;
    }

    public void setContent(ArrayList<ContentModel> content) {
        this.content = content;
    }

    public Widget.DefaultAction getDefault_action() {
        return default_action;
    }

    public void setDefault_action(Widget.DefaultAction default_action) {
        this.default_action = default_action;
    }

    public ArrayList<Widget.Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Widget.Button> buttons) {
        this.buttons = buttons;
    }

    public Object getButtonsLayout() {
        return buttonsLayout;
    }

    public void setButtonsLayout(Object buttonsLayout) {
        this.buttonsLayout = buttonsLayout;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTheme() {
        return theme;
    }

    public String getStockAvailability() {
        return stock_availability;
    }

    public String getTopRated() {
        return topRated;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setTopRated(String topRated) {
        this.topRated = topRated;
    }

    public ArrayList<ContentModel> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ContentModel> details) {
        this.details = details;
    }

    public ViewStyles getTemplateStyles() {
        return templateStyles;
    }

    public ViewStyles getAvailabilityStyles() {
        return availabilityStyles;
    }

    public ViewStyles getTopRatedStyles() {
        return topRatedStyles;
    }

    public void setStockAvailability(String stock_availability) {
        this.stock_availability = stock_availability;
    }

    public void setTemplateStyles(ViewStyles templateStyles) {
        this.templateStyles = templateStyles;
    }

    public void setAvailabilityStyles(ViewStyles availabilityStyles) {
        this.availabilityStyles = availabilityStyles;
    }

    public void setTopRatedStyles(ViewStyles topRatedStyles) {
        this.topRatedStyles = topRatedStyles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
