package com.kore.ai.widgetsdk.drawables;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by nicholaspontiff on 7/16/15.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BaseDrawable extends Drawable {


    public BaseDrawable() {
    }

    @Override
    public void draw(Canvas canvas) {


    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Paint getPaint(int color, Paint.Style style) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(style);
        return paint;
    }
}
