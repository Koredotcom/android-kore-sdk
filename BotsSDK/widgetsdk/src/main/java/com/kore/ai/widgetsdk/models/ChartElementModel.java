package com.kore.ai.widgetsdk.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kore.ai.widgetsdk.utils.WidgetConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChartElementModel {
    private String templateType;
    private String title;
    private String subTitle;
    private String pieType;
    private List<Object> elements = null;
    private List<ContentModel> content = null;
    private List<BotButtonModel> buttons = null;
    private String Auto_adjust_X_axis;
    private List<String> X_axis;

    public ArrayList<BotPieChartElementModel> getPieChartElements() {
        return pieChartElements;
    }

    public void setPieChartElements(ArrayList<BotPieChartElementModel> pieChartElements) {
        this.pieChartElements = pieChartElements;
    }

    public ArrayList<BotBarChartDataModel> getBarChartElements() {
        return barChartElements;
    }

    public void setBarChartElements(ArrayList<BotBarChartDataModel> barChartElements) {
        this.barChartElements = barChartElements;
    }

    public ArrayList<BotLineChartDataModel> getLineChartElements() {
        return lineChartElements;
    }

    public void setLineChartElements(ArrayList<BotLineChartDataModel> lineChartElements) {
        this.lineChartElements = lineChartElements;
    }

    private ArrayList<BotPieChartElementModel> pieChartElements = null;
    private ArrayList<BotBarChartDataModel> barChartElements = null;
    private ArrayList<BotLineChartDataModel> lineChartElements = null;

    public String getAuto_adjust_X_axis() {
        return Auto_adjust_X_axis;
    }

    public void setAuto_adjust_X_axis(String auto_adjust_X_axis) {
        Auto_adjust_X_axis = auto_adjust_X_axis;
    }

    public List<String> getX_axis() {
        return X_axis;
    }

    public void setX_axis(List<String> x_axis) {
        X_axis = x_axis;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPieType() {
        return pieType;
    }

    public void setPieType(String pieType) {
        this.pieType = pieType;
    }

    public List<Object> getElements() {
        return elements;
    }

    public void setElements(List<Object> elements) {
        this.elements = elements;
    }

    public List<ContentModel> getContent() {
        return content;
    }

    public void setContent(List<ContentModel> content) {
        this.content = content;
    }

    public List<BotButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(List<BotButtonModel> buttons) {
        this.buttons = buttons;
    }

    public void convertElementsToModel(){
        if (elements != null) {
            Gson gson = new Gson();
            String elementsAsString = gson.toJson(elements);
            if(WidgetConstants.PIE_CHART.equals(templateType)){
                Type piType = new TypeToken<ArrayList<BotPieChartElementModel>>() {
                }.getType();
                pieChartElements = gson.fromJson(elementsAsString, piType);
            }else if(WidgetConstants.BAR_CHART.equals(templateType)){
                Type barType = new TypeToken<ArrayList<BotBarChartDataModel>>() {
                }.getType();
                barChartElements = gson.fromJson(elementsAsString, barType);
            }else if(WidgetConstants.LINE_CHART.equals(templateType)){
                Type lineType = new TypeToken<ArrayList<BotLineChartDataModel>>() {
                }.getType();
                lineChartElements = gson.fromJson(elementsAsString, lineType);
            }
        }
    }

}
