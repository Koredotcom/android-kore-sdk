package kore.botssdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class BotTableListModel implements Serializable
{
    private String sectionHeader;
    private String sectionHeaderDesc;
    private ArrayList<BotTableListRowItemsModel> rowItems;

    public ArrayList<BotTableListRowItemsModel> getRowItems() {
        return rowItems;
    }

    public void setRowItems(ArrayList<BotTableListRowItemsModel> rowItems) {
        this.rowItems = rowItems;
    }

    public void setSectionHeader(String sectionHeader) {
        this.sectionHeader = sectionHeader;
    }

    public String getSectionHeader() {
        return sectionHeader;
    }

    public void setSectionHeaderDesc(String sectionHeaderDesc) {
        this.sectionHeaderDesc = sectionHeaderDesc;
    }

    public String getSectionHeaderDesc() {
        return sectionHeaderDesc;
    }
}
