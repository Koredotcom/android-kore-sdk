package kore.botssdk.charts.components;

import android.graphics.Paint;

import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;

public class Description extends ComponentBase {
    private String text = "Description Label";
    private MPPointF mPosition;
    private Paint.Align mTextAlign;

    public Description() {
        this.mTextAlign = Paint.Align.RIGHT;
        this.mTextSize = Utils.convertDpToPixel(8.0F);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setPosition(float x, float y) {
        if (this.mPosition == null) {
            this.mPosition = MPPointF.getInstance(x, y);
        } else {
            this.mPosition.x = x;
            this.mPosition.y = y;
        }

    }

    public MPPointF getPosition() {
        return this.mPosition;
    }

    public void setTextAlign(Paint.Align align) {
        this.mTextAlign = align;
    }

    public Paint.Align getTextAlign() {
        return this.mTextAlign;
    }
}
