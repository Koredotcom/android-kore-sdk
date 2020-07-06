package com.kore.ai.widgetsdk.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;

/**
 * Created by nicholaspontiff on 7/16/15.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ProfileDrawable extends BaseDrawable {

    private int backgroundColor;
    private String initials;
    private Paint backgroundPaint;
    private Paint mTextPaint;

    public ProfileDrawable(int color, String initials, float textSize) {
        this(color, initials, textSize, null);
    }

    public ProfileDrawable(int color, String initials, float textSize, Typeface textTypeface) {
        this.backgroundColor = color;
        this.initials = initials;
        backgroundPaint = getPaint(this.backgroundColor, Paint.Style.FILL);
        mTextPaint = getPaint(Color.WHITE, Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        float xCenter = getBounds().centerX();
        float yCenter = getBounds().centerY();

        canvas.drawCircle(xCenter, yCenter, getBounds().width()/2 , backgroundPaint);

        if(initials != null) {
            //((mTextPaint.descent() + mTextPaint.ascent()) / 2) is the distance from the baseline to the center.
            float yTextCenter = (yCenter - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));

            canvas.drawText(initials, xCenter, yTextCenter, mTextPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mTextPaint.setAlpha(alpha);
        backgroundPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mTextPaint.setColorFilter(cf);
        backgroundPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }



}
