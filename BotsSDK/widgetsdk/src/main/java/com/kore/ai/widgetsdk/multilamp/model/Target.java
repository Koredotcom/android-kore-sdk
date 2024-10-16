package com.kore.ai.widgetsdk.multilamp.model;

import android.graphics.Paint;
import android.view.View;

import com.kore.ai.widgetsdk.multilamp.shapes.Shape;

public class Target {
    public View getRecy() {
        return recy;
    }

    public void setRecy(View recy) {
        this.recy = recy;
    }

    private View recy;
    private View view;
    private  WidgetEnum widgetEnum;

    private String message;

    public int getAppendPosition() {
        return appendPosition;
    }

    public void setAppendPosition(int appendPosition) {
        this.appendPosition = appendPosition;
    }

    int appendPosition;
    public Paint.Align getAlign() {
        return align;
    }

    public void setAlign(Paint.Align align) {
        this.align = align;
    }

    private Shape shape;
    private Paint.Align align;
    public char getDirection() {
        return direction;
    }
    int lineCount, textPlace;

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getTextPlace() {
        return textPlace;
    }

    public void setTextPlace(int textPlace) {
        this.textPlace = textPlace;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    private char direction;
    private  int drawableId;
    public Target() {

    }

    public WidgetEnum getWidgetEnum() {
        return widgetEnum;
    }

    public void setWidgetEnum(WidgetEnum widgetEnum) {
        this.widgetEnum = widgetEnum;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}

