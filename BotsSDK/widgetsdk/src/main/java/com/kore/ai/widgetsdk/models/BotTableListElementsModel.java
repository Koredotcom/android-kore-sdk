package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class BotTableListElementsModel {
    private String sectionHeader;
    private String sectionHeaderDesc;
    private ArrayList<BotTableListElementsItemsModel> elements;

    public ArrayList<BotTableListElementsItemsModel> getElements() {
        return elements;
    }

    public void setElements(ArrayList<BotTableListElementsItemsModel> elements) {
        this.elements = elements;
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
