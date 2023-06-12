package kore.botssdk.drawables;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import kore.botssdk.view.viewUtils.DimensionUtil;

public class TopGravityDrawable extends BitmapDrawable {
    private final int dp1;
    public TopGravityDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
        dp1 = (int) DimensionUtil.dp1;
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