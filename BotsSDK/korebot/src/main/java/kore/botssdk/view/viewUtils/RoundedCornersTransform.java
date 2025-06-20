package kore.botssdk.view.viewUtils;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;


public class RoundedCornersTransform implements Transformation {
    public void setRadius(float radius) {
        this.radius = radius;
    }

    private float radius = 0;

    @Override
    public Bitmap transform(Bitmap source) {
        try {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // Crop the image to a square
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            // Ensure the config is valid
            Bitmap.Config config = source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;

            // Create the output bitmap
            Bitmap bitmap = Bitmap.createBitmap(size, size, config);

            // Set up canvas and paint
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            // Create the shader
            BitmapShader shader = new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            // Corner radius
            radius = size / 8f; // Or set a fixed value like 10f
            canvas.drawRoundRect(new RectF(0f, 0f, size, size), radius, radius, paint);

            // Clean up
            squaredBitmap.recycle();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String key() {
        return "rounded_corners";
    }
}