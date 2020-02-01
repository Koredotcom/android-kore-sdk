package kore.botssdk.view.viewUtils;

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


public class CustomRoundedTransform implements Transformation {
    public void setValues(float r, float width, float height) {
        this.r = r;
        this.width = width;
        this.height = height;
    }

    private float r = 0;
    private float width = 0;
    private float height = 0;
    public CustomRoundedTransform(int r, int width, int height){
        this.r = r;
        this.width = width;
        this.height = height;
    }
    @Override
    public Bitmap transform(Bitmap source) {
//        if(source == null)return null;
        if (source == null) return source;
        try {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - (int)width) / 2;
            int y = (source.getHeight() - (int)height) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, (int)width, (int)height);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap.Config config = source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;

            Bitmap bitmap = Bitmap.createBitmap((int)width, (int)height, config);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            if (r == 0) {
                r = size / 8f;
            }
            canvas.drawRoundRect(new RectF(0, 0, width, height), r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String key() {
        return CustomRoundedTransform.class.getSimpleName() + "." + r;
    }
}