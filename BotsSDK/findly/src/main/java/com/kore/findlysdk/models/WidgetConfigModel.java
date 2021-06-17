package com.kore.findlysdk.models;

import java.util.ArrayList;

public class WidgetConfigModel
{
    private String searchBarFillColor;
    private String searchBarBorderColor;
    private String searchBarPlaceholderText;
    private String searchBarPlaceholderTextColor;
    private String searchButtonEnabled;
    private String buttonText;
    private String buttonTextColor;
    private String buttonFillColor;
    private String buttonBorderColor;
    private ArrayList<String> userSelectedColors;
    private String buttonPlacementPosition;
    private String searchBarIcon;

    public ArrayList<String> getUserSelectedColors() {
        return userSelectedColors;
    }

    public String getButtonBorderColor() {
        return buttonBorderColor;
    }

    public String getButtonFillColor() {
        return buttonFillColor;
    }

    public String getButtonPlacementPosition() {
        return buttonPlacementPosition;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public String getSearchBarBorderColor() {
        return searchBarBorderColor;
    }

    public String getSearchBarFillColor() {
        return searchBarFillColor;
    }

    public String getSearchBarIcon() {
        return searchBarIcon;
    }

    public String getSearchBarPlaceholderText() {
        return searchBarPlaceholderText;
    }

    public String getSearchBarPlaceholderTextColor() {
        return searchBarPlaceholderTextColor;
    }

    public String getSearchButtonEnabled() {
        return searchButtonEnabled;
    }

    public void setButtonBorderColor(String buttonBorderColor) {
        this.buttonBorderColor = buttonBorderColor;
    }

    public void setButtonFillColor(String buttonFillColor) {
        this.buttonFillColor = buttonFillColor;
    }

    public void setButtonPlacementPosition(String buttonPlacementPosition) {
        this.buttonPlacementPosition = buttonPlacementPosition;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public void setSearchBarBorderColor(String searchBarBorderColor) {
        this.searchBarBorderColor = searchBarBorderColor;
    }

    public void setSearchBarFillColor(String searchBarFillColor) {
        this.searchBarFillColor = searchBarFillColor;
    }

    public void setSearchBarIcon(String searchBarIcon) {
        this.searchBarIcon = searchBarIcon;
    }

    public void setSearchBarPlaceholderText(String searchBarPlaceholderText) {
        this.searchBarPlaceholderText = searchBarPlaceholderText;
    }

    public void setSearchBarPlaceholderTextColor(String searchBarPlaceholderTextColor) {
        this.searchBarPlaceholderTextColor = searchBarPlaceholderTextColor;
    }

    public void setSearchButtonEnabled(String searchButtonEnabled) {
        this.searchButtonEnabled = searchButtonEnabled;
    }

    public void setUserSelectedColors(ArrayList<String> userSelectedColors) {
        this.userSelectedColors = userSelectedColors;
    }
}

