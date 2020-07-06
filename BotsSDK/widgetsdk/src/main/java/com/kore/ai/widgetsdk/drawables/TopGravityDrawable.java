package com.kore.ai.widgetsdk.drawables;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import com.kore.ai.widgetsdk.application.AppControl;

public class TopGravityDrawable extends BitmapDrawable {
    private int dp1;
    public TopGravityDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }

    @Override
    public void draw(Canvas canvas) {
        int halfCanvas = canvas.getHeight() / 2;
        int halfDrawable = getIntrinsicHeight() / 2;
        canvas.save();
        canvas.translate(0, -halfCanvas + halfDrawable + 6*dp1);
        super.draw(canvas);
        canvas.restore();
    }
}