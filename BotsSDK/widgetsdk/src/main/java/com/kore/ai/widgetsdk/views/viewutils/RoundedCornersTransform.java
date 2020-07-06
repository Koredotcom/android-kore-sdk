package com.kore.ai.widgetsdk.views.viewutils;

/**
 * Created by Pradeep Mahato on 22/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;


public class RoundedCornersTransform implements Transformation {
    public void setR(float r) {
        this.r = r;
    }

    private float r = 0;
    @Override
    public Bitmap transform(Bitmap source) {
        try {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap.Config config = source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;

            Bitmap bitmap = Bitmap.createBitmap(size, size, config);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            if (r == 0) {
                r = size / 8f;
            }
            canvas.drawRoundRect(new RectF(0, 0, source.getWidth(), source.getHeight()), r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String key() {
        return "rounded_corners";
    }
}