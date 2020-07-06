package com.kore.ai.widgetsdk.multilamp.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.multilamp.customView.MultiLamp;
import com.kore.ai.widgetsdk.multilamp.model.Target;

public class RectangleWithExtraHeight implements Shape {


    public static Point getLocationOnScreen(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }
    @Override
    public void draw(Canvas canvas, Context context, PointF point, float value, Target target, Paint paint) {
        float density = context.getResources().getDisplayMetrics().density;
        int[] location = new int[2];
        target.getView().getLocationInWindow(location);
        point.x = location[0];
        point.y = location[1];
        float newcalY = 0;
        if (target.getRecy() != null) {
            Point point1=getLocationOnScreen(target.getRecy());
            float y = point1.y;
            newcalY =  (point.y-y);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (target.getWidgetEnum()) {
                case DEFAULT:
                    canvas.drawRoundRect(point.x, point.y-6, point.x + target.getView().getWidth(), point.y + target.getView().getHeight()+6, 50, 50, paint);

                    break;

                case TITLE:
                    canvas.drawRoundRect(point.x, point.y - newcalY, point.x + target.getView().getWidth(), point.y + target.getView().getHeight(), 15, 15, paint);

                    break;
                case TITLE_BUTTOON:
                    canvas.drawRoundRect(point.x - 18, point.y - newcalY, point.x + target.getView().getWidth() + 14, point.y + target.getView().getHeight(), 15, 15, paint);

                    break;
                case TITLE_BUTTOON2:
                    canvas.drawRoundRect(point.x - 18, point.y - newcalY, point.x + target.getView().getWidth() - 15, point.y + target.getView().getHeight(), 15, 15, paint);

                    break;

            }
        } else {
            canvas.drawRect(point.x, point.y, point.x + target.getView().getWidth(), point.y + target.getView().getHeight(), paint);
        }


        //copied from circle

//        canvas.drawCircle(point.x, point.y, radius * density, paint);
        paint.setTextSize(15 * density);
        String[] lines = target.getMessage().split("\n");
        paint.setColor(Color.WHITE);
        Paint txtPaint = new Paint();
        Typeface typeface = ResourcesCompat.getFont(context, R.font.annie);
        txtPaint.setTypeface(typeface);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTextSize(20 * density);
        Log.d("Lines ", String.valueOf(lines.length));
        switch (target.getDirection()) {
            //- (radius * density)
            /**
             * BOTTTOM DIRECTION SECTION
             *
             */
            case MultiLamp.BOTTOM:
                point.x = point.x + target.getView().getWidth() / 2;
//                point.y = point.y;// - target.getView().getHeight();// * density;
                txtPaint.setTextAlign(Paint.Align.CENTER);
//                 * density
                float texty = point.y + ((target.getView().getHeight()) + ((target.getTextPlace() * density)));
                for (int lineb = 0; lineb < lines.length; lineb++) {
                    Log.d("texty", String.valueOf(texty));
                    canvas.drawText(lines[lineb], point.x, texty, txtPaint);  //bottom
                    texty = texty + 22 * density;
                }
// * density
                float startYB = point.y + (target.getView().getHeight());
                float stopYB = (startYB) + (10 * density);
                for (int i = 0; i < target.getLineCount(); i++) {
//                    Log.d("startX and Stopx", String.valueOf(startX) + " " + String.valueOf(stopX));
                    // canvas.drawLine(point.x, startYB, point.x, stopYB, txtPaint);
                    startYB = (stopYB) + (3 * density);
                    stopYB = (startYB) + (10 * density);
                }

                Bitmap b1 = BitmapFactory.decodeResource(context.getResources(), target.getDrawableId());
                canvas.drawBitmap(b1, point.x, startYB, null);
                break;


            /**
             *
             * TOP DIRECTION SECTION
             */

            case MultiLamp.TOP:
                point.x = point.x + (target.getView().getWidth()) / 2;
//                point.y = point.y + target.getView().getHeight() * density;
                txtPaint.setTextAlign(Paint.Align.CENTER);
//                 * density
                float textty = point.y - (target.getTextPlace() * density);//((target.getView().getHeight()) + (55 * density));
                if (lines.length > 0) {
                    textty = textty - ((22 * lines.length) * density);
                }

                for (int line = 0; line < lines.length; line++) {
                    Log.d("texty", String.valueOf(textty));
                    canvas.drawText(lines[line], point.x + target.getAppendPosition(), textty, txtPaint);
                    textty = textty + 22 * density;
                }

//                 * density
                float startYT = point.y;// - (target.getView().getHeight());
                float stopYT = (startYT) - (10 * density);

                for (int i = 0; i < target.getLineCount(); i++) {
                    // canvas.drawLine(point.x, startYT, point.x, stopYT, txtPaint);
                    startYT = (stopYT) - (3 * density);
                    stopYT = (startYT) - (10 * density);
                }
                Bitmap b2 = BitmapFactory.decodeResource(context.getResources(), target.getDrawableId());
                canvas.drawBitmap(b2, point.x, stopYT, null);
                break;


            /**
             * LEFT DIRECTION SECTION
             */
            case MultiLamp.LEFT:

                //+ target.getView().getWidth()
//                point.x = point.x;
                txtPaint.setTextAlign(target.getAlign());
                float textly = point.y + (target.getView().getHeight() / 2) + (6 * density);
                if (lines.length > 0) {
                    textly = textly - ((22 * ((int) lines.length / 2) * density));
                }
                for (int line = 0; line < lines.length; line++) {
                    Log.d("texty", String.valueOf(textly));
//                    (target.getView().getWidth() * density)
                    canvas.drawText(lines[line], point.x - (52 * density), textly, txtPaint);
                    textly = textly + 22 * density;
                }

                float startXL = point.x;//- (target.getView().getWidth() * density);
                float stopXL = (startXL) - (10 * density);

                for (int i = 0; i < 4; i++) {
                    canvas.drawLine(startXL, point.y + target.getView().getHeight() / 2, stopXL, point.y + target.getView().getHeight() / 2, txtPaint);
                    startXL = (stopXL) - (3 * density);
                    stopXL = (startXL) - (10 * density);
                }
                break;

            /**
             * RIGHT DIRECTION SECTION
             */
            case MultiLamp.RIGHT:


                float textry = point.y + (target.getView().getHeight()) + (6 * density);
                if (lines.length > 0) {
                    textry = textry - ((22 * ((int) lines.length / 1.4f) * density));
                }
                for (int line = 0; line < lines.length; line++) {
                    Log.d("texty", String.valueOf(textry));
                    canvas.drawText(lines[line], point.x + ((target.getView().getWidth()) + (50 * density)), textry, txtPaint);        //right
                    textry = textry + 22 * density;
                }


                /*float startXR = point.x + (target.getView().getWidth());
                float stopXR = (startXR) + (10 * density);
                for (int i = 0; i < 4; i++) {
                   // canvas.drawLine(startXR, point.y + (target.getView().getHeight() / 2), stopXR, point.y + (target.getView().getHeight() / 2), txtPaint);
                    startXR = (stopXR) + (3 * density);
                    stopXR = (startXR) + (10 * density);
                }*/

                Bitmap leftBit = BitmapFactory.decodeResource(context.getResources(), target.getDrawableId());
                canvas.drawBitmap(leftBit, point.x + (target.getView().getWidth()), point.y + (target.getView().getHeight() / 3), null);
                break;

            default:
                break;

        }
        paint = null;

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }
}
