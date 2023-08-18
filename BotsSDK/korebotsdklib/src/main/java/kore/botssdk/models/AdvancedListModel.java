package kore.botssdk.models;

import java.util.ArrayList;

public class AdvancedListModel
{
    private String title;
    private TitleStylesModel titleStyles;
    private ArrayList<HeaderOptionsModel> headerOptions;
    private String iconSize;
    private String iconShape;
    private boolean isCollapsed;
    private String view;
    private String type;
    private String payload;
    private String icon;
    private String description;
    private String descriptionIcon;
    private String descriptionIconAlignment;
    private String buttonAligment;
    private DescriptionStylesModel descriptionStyles;
    private DescriptionStylesModel elementStyles;
    private ArrayList<Widget.Button> textInformation;
    private ArrayList<Widget.Button> buttons;
    private ArrayList<AdvanceOptionsModel> optionsData;
    private ArrayList<AdvanceListTableModel> tableListData;
    private ButtonsLayoutModel buttonsLayout;
    public ButtonsLayoutModel getButtonsLayout() {
        return buttonsLayout;
    }

    public String getDescriptionIconAlignment() {
        return descriptionIconAlignment;
    }

    public void setDescriptionIcon(String descriptionIcon) {
        this.descriptionIcon = descriptionIcon;
    }

    public void setButtonAligment(String buttonAligment) {
        this.buttonAligment = buttonAligment;
    }

    public void setButtons(ArrayList<Widget.Button> buttons) {
        this.buttons = buttons;
    }

    public void setDescriptionIconAlignment(String descriptionIconAlignment) {
        this.descriptionIconAlignment = descriptionIconAlignment;
    }

    public void setElementStyles(DescriptionStylesModel elementStyles) {
        this.elementStyles = elementStyles;
    }

    public void setTableListData(ArrayList<AdvanceListTableModel> tableListData) {
        this.tableListData = tableListData;
    }

    public void setTextInformation(ArrayList<Widget.Button> textInformation) {
        this.textInformation = textInformation;
    }

    public String getDescriptionIcon() {
        return descriptionIcon;
    }

    public ArrayList<AdvanceListTableModel> getTableListData() {
        return tableListData;
    }

    public String getButtonAligment() {
        return buttonAligment;
    }

    public ArrayList<AdvanceOptionsModel> getOptionsData() {
        return optionsData;
    }

    public void setOptionsData(ArrayList<AdvanceOptionsModel> optionsData) {
        this.optionsData = optionsData;
    }

    public ArrayList<Widget.Button> getButtons() {
        return buttons;
    }

    public ArrayList<Widget.Button> getTextInformation() {
        return textInformation;
    }

    public DescriptionStylesModel getElementStyles() {
        return elementStyles;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public DescriptionStylesModel getDescriptionStyles() {
        return descriptionStyles;
    }

    public String getIconShape() {
        return iconShape;
    }

    public String getIconSize() {
        return iconSize;
    }

    public String getPayload() {
        return payload;
    }

    public String getView() {
        return view;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
    }

    public void setDescriptionStyles(DescriptionStylesModel descriptionStyles) {
        this.descriptionStyles = descriptionStyles;
    }

    public void setIconShape(String iconShape) {
        this.iconShape = iconShape;
    }

    public void setIconSize(String iconSize) {
        this.iconSize = iconSize;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setView(String view) {
        this.view = view;
    }

    public ArrayList<HeaderOptionsModel> getHeaderOptions() {
        return headerOptions;
    }

    public void setHeaderOptions(ArrayList<HeaderOptionsModel> headerOptions) {
        this.headerOptions = headerOptions;
    }

    public TitleStylesModel getTitleStyles() {
        return titleStyles;
    }

    public void setTitleStyles(TitleStylesModel titleStyles) {
        this.titleStyles = titleStyles;
    }
}
