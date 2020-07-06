package com.kore.ai.widgetsdk.multilamp.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.kore.ai.widgetsdk.multilamp.model.Target;

public interface Shape {


    void draw(Canvas canvas, Context context, PointF point, float value, Target target, Paint paint);

    int getHeight();

    int getWidth();
}
