package kore.botssdk.models;

import java.util.ArrayList;

public class AdvanceListTableModel
{
    private ArrayList<AdvanceTableRowDataModel> rowData;

    public ArrayList<AdvanceTableRowDataModel> getRowData() {
        return rowData;
    }

    public class AdvanceTableRowDataModel
    {
        private String title;
        private String description;
        private String icon;
        private String iconSize;
        private String type;

        public String getType() {
            return type;
        }

        private ArrayList<Widget.Button> dropdownOptions;

        public ArrayList<Widget.Button> getDropdownOptions() {
            return dropdownOptions;
        }

        public String getIcon() {
            return icon;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getIconSize() {
            return iconSize;
        }
    }
}
