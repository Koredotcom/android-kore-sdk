package com.kore.ai.widgetsdk.multilamp.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.multilamp.customView.MultiLamp;
import com.kore.ai.widgetsdk.multilamp.model.Target;

public class CircleN implements Shape {


    private float radius;

    public CircleN(float radius) {

        this.radius = radius;

    }


    @Override
    public void draw(Canvas canvas, Context context, PointF point, float value, Target target, Paint paint) {
        float density = context.getResources().getDisplayMetrics().density;
        int[] location = new int[2];

        target.getView().getLocationOnScreen(location);
        point.x = location[0] + target.getView().getWidth() / 2;
        point.y = location[1] + target.getView().getHeight() / 2;


        Log.d("shri","Width-"+location[0] + target.getView().getWidth());
        Log.d("shri","Height-"+location[0] + target.getView().getHeight());

        float x=0;
        float y=0;
        switch (target.getlIneOrientation())
        {
            case Top:
                x=point.x;
                y=point.y+(radius*density);
                break;
            case Left:
                x=point.x-(radius*density);
                y=point.y;
                break;
            case Right:
                x=point.x-((radius*density)/2);
                y=point.y;
                break;
            case Bottom:
                x=point.x-((radius*density)/2);
                y=point.y-(radius*density);
                break;
        }


        canvas.drawCircle(point.x, point.y, radius * density, paint);
        paint.setTextSize(15 * density);

      //  Typeface typeface =Typeface.createFromAsset(context.getAssets(),"font/annie.ttf");
        String[] lines = target.getMessage().split("\n");
        Log.d("Lines ", String.valueOf(lines.length));
        Paint txtPaint = new Paint();
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTextSize(20 * density);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.annie);
        txtPaint.setTypeface(typeface);
        switch (target.getDirection()) {
            //- (radius * density)

            /**
             * BOTTTOM DIRECTION SECTION
             *
             */
            case MultiLamp.BOTTOM:
                txtPaint.setTextAlign(Paint.Align.LEFT);
                float texty = point.y + ((radius * density) + (target.getTextPlace() * density));
                for (int lineb = 0; lineb < lines.length; lineb++) {
                    Log.d("texty", String.valueOf(texty));
                    canvas.drawText(lines[lineb], point.x, texty, txtPaint);  //bottom

                    texty = texty + 22 * density;
                }

                float startYB = point.y + (radius * density);
                float stopYB = (startYB) + (10 * density);
                for (int i = 0; i < target.getLineCount(); i++) {
//                    Log.d("startX and Stopx", String.valueOf(startX) + " " + String.valueOf(stopX));
                    //canvas.drawLine(point.x, startYB, point.x, stopYB, txtPaint);
                    startYB = (stopYB) + (3 * density);
                    stopYB = (startYB) + (10 * density);
                }
                Bitmap bottBit=BitmapFactory.decodeResource(context.getResources(), target.getDrawableId());
                canvas.drawBitmap(bottBit, x,startYB, null);
                break;


            /**
             *
             * TOP DIRECTION SECTION
             */

            case MultiLamp.TOP:

       txtPaint.setTextAlign(target.getAlign());
                float textty = point.y - ((radius * density) + (target.getTextPlace() * density));
                if (lines.length > 0) {
                    textty = textty - ((22 * lines.length) * density);
                }
                for (int line = 0; line < lines.length; line++) {
                    Log.d("texty", String.valueOf(textty));
                    canvas.drawText(lines[line], point.x+target.getAppendPosition(), textty, txtPaint);
                    textty = textty + 22 * density;
                }


                float startYT = y -  (radius * density);
                float stopYT = (startYT) - (10 * density);
                for (int i = 0; i < target.getLineCount(); i++) {
                 // canvas.drawLine(x, startYT, x, stopYT, txtPaint);
                    startYT = (stopYT) - (3 * density);
                    stopYT = (startYT) - (10 * density);
                }

                Bitmap b1=BitmapFactory.decodeResource(context.getResources(), target.getDrawableId());
                canvas.drawBitmap(b1, x,startYT, null);

                break;


            /**
             * LEFT DIRECTION SECTION
             */
            case MultiLamp.LEFT:

                txtPaint.setTextAlign(Paint.Align.CENTER);

                float textly = point.y + (target.getTextPlace() * density);
                if (lines.length > 0) {
                    textly = textly - ((22 * ((int) lines.length / 2) * density));
                }
                for (int line = 0; line < lines.length; line++) {
                    Log.d("texty", String.valueOf(textly));
                    canvas.drawText(lines[line], point.x - ((radius * density) + (52 * density))+target.getAppendPosition(), textly, txtPaint);
                    textly = textly + 22 * density;
                }

                float startXL = point.x - (radius * density);
                float stopXL = (startXL) - (10 * density);

                for (int i = 0; i < target.getLineCount(); i++) {
                   // canvas.drawLine(startXL, point.y, stopXL, point.y, txtPaint);
                    startXL = (stopXL) - (3 * density);
                    stopXL = (startXL) - (10 * density);
                }
                Bitmap leftBit=BitmapFactory.decodeResource(context.getResources(), target.getDrawableId());
                canvas.drawBitmap(leftBit, startXL, point.y, null);

                break;

            /**
             * RIGHT DIRECTION SECTION
             */
            case MultiLamp.RIGHT:


                float textry = point.y + (6 * density);
                if (lines.length > 0) {
                    textry = textry - ((22 * ((int) lines.length / 2) * density));
                }
                for (int line = 0; line < lines.length; line++) {
                    Log.d("texty", String.valueOf(textry));
                    canvas.drawText(lines[line], point.x + ((radius * density) + (60 * density)), textry, txtPaint);        //right
                    textry = textry + 22 * density;
                }


                float startXR = point.x + (radius * density);
                float stopXR = (startXR) + (10 * density);
                for (int i = 0; i < 4; i++) {
                    canvas.drawLine(startXR, point.y, stopXR, point.y, txtPaint);
                    startXR = (stopXR) + (3 * density);
                    stopXR = (startXR) + (10 * density);
                }
                break;

            default:
                break;
        }

paint=null;
    }

    @Override
    public int getHeight() {
        return (int) radius;
    }

    @Override
    public int getWidth() {
        return (int) radius;
    }
}


//        int scaledSize = context.getResources().getDimensionPixelSize(R.dimen.fontSizeCanvas);
//        paint.setTextScaleX(density);
//        paint.setTextSize(scaledSize);
