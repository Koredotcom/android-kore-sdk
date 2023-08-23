package kore.botssdk.models;

public class CardHeadingModel
{
    private String title;
    private String description;
    private String icon;
    private String iconSize;
    public String getIconSize() {
        return iconSize;
    }
    public String getIcon() {
        return icon;
    }
    public String getDescription() {
        return description;
    }
    private HeaderStyles headerStyles;

    private AdvanceListTableModel.AdvanceTableRowDataModel headerExtraInfo;

    public AdvanceListTableModel.AdvanceTableRowDataModel getHeaderExtraInfo() {
        return headerExtraInfo;
    }

    public String getTitle() {
        return title;
    }

    public HeaderStyles getHeaderStyles() {
        return headerStyles;
    }

}
