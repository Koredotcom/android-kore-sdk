package kore.botssdk.charts.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.List;

import kore.botssdk.charts.formatter.DefaultValueFormatter;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.utils.LogUtils;

public abstract class Utils {
    private static DisplayMetrics mMetrics;
    private static int mMinimumFlingVelocity = 50;
    private static int mMaximumFlingVelocity = 8000;
    public static final double DEG2RAD = 0.017453292519943295D;
    public static final float FDEG2RAD = 0.017453292F;
    public static final double DOUBLE_EPSILON = Double.longBitsToDouble(1L);
    public static final float FLOAT_EPSILON = Float.intBitsToFloat(1);
    private static final Rect mCalcTextHeightRect = new Rect();
    private static final Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();
    private static final Rect mCalcTextSizeRect = new Rect();
    private static final int[] POW_10 = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
    private static final ValueFormatter mDefaultValueFormatter = generateDefaultValueFormatter();
    private static final Rect mDrawableBoundsCache = new Rect();
    private static final Rect mDrawTextRectBuffer = new Rect();
    private static final Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();

    public Utils() {
    }

    public static void init(Context context) {
        if (context == null) {
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            LogUtils.e("MPChartLib-Utils", "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL");
        } else {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
            Resources res = context.getResources();
            mMetrics = res.getDisplayMetrics();
        }

    }

    /** @deprecated */
    @Deprecated
    public static void init(Resources res) {
        mMetrics = res.getDisplayMetrics();
        mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    public static float convertDpToPixel(float dp) {
        if (mMetrics == null) {
            LogUtils.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertDpToPixel(...). Otherwise conversion does not take place.");
            return dp;
        } else {
            return dp * mMetrics.density;
        }
    }

    public static float convertPixelsToDp(float px) {
        if (mMetrics == null) {
            LogUtils.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertPixelsToDp(...). Otherwise conversion does not take place.");
            return px;
        } else {
            return px / mMetrics.density;
        }
    }

    public static int calcTextWidth(Paint paint, String demoText) {
        return (int)paint.measureText(demoText);
    }

    public static int calcTextHeight(Paint paint, String demoText) {
        Rect r = mCalcTextHeightRect;
        r.set(0, 0, 0, 0);
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }

    public static float getLineHeight(Paint paint) {
        return getLineHeight(paint, mFontMetrics);
    }

    public static float getLineHeight(Paint paint, Paint.FontMetrics fontMetrics) {
        paint.getFontMetrics(fontMetrics);
        return fontMetrics.descent - fontMetrics.ascent;
    }

    public static float getLineSpacing(Paint paint) {
        return getLineSpacing(paint, mFontMetrics);
    }

    public static float getLineSpacing(Paint paint, Paint.FontMetrics fontMetrics) {
        paint.getFontMetrics(fontMetrics);
        return fontMetrics.ascent - fontMetrics.top + fontMetrics.bottom;
    }

    public static FSize calcTextSize(Paint paint, String demoText) {
        FSize result = FSize.getInstance(0.0F, 0.0F);
        calcTextSize(paint, demoText, result);
        return result;
    }

    public static void calcTextSize(Paint paint, String demoText, FSize outputFSize) {
        Rect r = mCalcTextSizeRect;
        r.set(0, 0, 0, 0);
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        outputFSize.width = (float)r.width();
        outputFSize.height = (float)r.height();
    }

    private static ValueFormatter generateDefaultValueFormatter() {
        return new DefaultValueFormatter(1);
    }

    public static ValueFormatter getDefaultValueFormatter() {
        return mDefaultValueFormatter;
    }

    public static String formatNumber(float number, int digitCount, boolean separateThousands) {
        return formatNumber(number, digitCount, separateThousands, '.');
    }

    public static String formatNumber(float number, int digitCount, boolean separateThousands, char separateChar) {
        char[] out = new char[35];
        boolean neg = false;
        if (number == 0.0F) {
            return "0";
        } else {
            boolean zero = number < 1.0F && number > -1.0F;

            if (number < 0.0F) {
                neg = true;
                number = -number;
            }

            if (digitCount > POW_10.length) {
                digitCount = POW_10.length - 1;
            }

            number *= (float)POW_10[digitCount];
            long lval = Math.round(number);
            int ind = out.length - 1;
            int charCount = 0;
            boolean decimalPointAdded = false;

            int start;
            while(lval != 0L || charCount < digitCount + 1) {
                start = (int)(lval % 10L);
                lval /= 10L;
                out[ind--] = (char)(start + 48);
                ++charCount;
                if (charCount == digitCount) {
                    out[ind--] = ',';
                    ++charCount;
                    decimalPointAdded = true;
                } else if (separateThousands && lval != 0L && charCount > digitCount) {
                    if (decimalPointAdded) {
                        if ((charCount - digitCount) % 4 == 0) {
                            out[ind--] = separateChar;
                            ++charCount;
                        }
                    } else if ((charCount - digitCount) % 4 == 3) {
                        out[ind--] = separateChar;
                        ++charCount;
                    }
                }
            }

            if (zero) {
                out[ind--] = '0';
                ++charCount;
            }

            if (neg) {
                out[ind--] = '-';
                ++charCount;
            }

            start = out.length - charCount;
            return String.valueOf(out, start, out.length - start);
        }
    }

    public static float roundToNextSignificant(double number) {
        if (!Double.isInfinite(number) && !Double.isNaN(number) && number != 0.0D) {
            float d = (float)Math.ceil((float)Math.log10(number < 0.0D ? -number : number));
            int pw = 1 - (int)d;
            float magnitude = (float)Math.pow(10.0D, pw);
            long shifted = Math.round(number * (double)magnitude);
            return (float)shifted / magnitude;
        } else {
            return 0.0F;
        }
    }

    public static int getDecimals(float number) {
        float i = roundToNextSignificant(number);
        return Float.isInfinite(i) ? 0 : (int)Math.ceil(-Math.log10(i)) + 2;
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        copyIntegers(integers, ret);
        return ret;
    }

    public static void copyIntegers(List<Integer> from, int[] to) {
        int count = to.length < from.size() ? to.length : from.size();

        for(int i = 0; i < count; ++i) {
            to[i] = from.get(i);
        }

    }

    public static String[] convertStrings(List<String> strings) {
        String[] ret = new String[strings.size()];

        for(int i = 0; i < ret.length; ++i) {
            ret[i] = strings.get(i);
        }

        return ret;
    }

    public static void copyStrings(List<String> from, String[] to) {
        int count = to.length < from.size() ? to.length : from.size();

        for(int i = 0; i < count; ++i) {
            to[i] = from.get(i);
        }

    }

    public static double nextUp(double d) {
        if (d == 1.0D / 0.0) {
            return d;
        } else {
            d += 0.0D;
            return Double.longBitsToDouble(Double.doubleToRawLongBits(d) + (d >= 0.0D ? 1L : -1L));
        }
    }

    public static MPPointF getPosition(MPPointF center, float dist, float angle) {
        MPPointF p = MPPointF.getInstance(0.0F, 0.0F);
        getPosition(center, dist, angle, p);
        return p;
    }

    public static void getPosition(MPPointF center, float dist, float angle, MPPointF outputPoint) {
        outputPoint.x = (float)((double)center.x + (double)dist * Math.cos(Math.toRadians(angle)));
        outputPoint.y = (float)((double)center.y + (double)dist * Math.sin(Math.toRadians(angle)));
    }

    public static void velocityTrackerPointerUpCleanUpIfNecessary(MotionEvent ev, VelocityTracker tracker) {
        tracker.computeCurrentVelocity(1000, (float)mMaximumFlingVelocity);
        int upIndex = ev.getActionIndex();
        int id1 = ev.getPointerId(upIndex);
        float x1 = tracker.getXVelocity(id1);
        float y1 = tracker.getYVelocity(id1);
        int i = 0;

        for(int count = ev.getPointerCount(); i < count; ++i) {
            if (i != upIndex) {
                int id2 = ev.getPointerId(i);
                float x = x1 * tracker.getXVelocity(id2);
                float y = y1 * tracker.getYVelocity(id2);
                float dot = x + y;
                if (dot < 0.0F) {
                    tracker.clear();
                    break;
                }
            }
        }

    }

    @SuppressLint({"NewApi"})
    public static void postInvalidateOnAnimation(View view) {
        view.postInvalidateOnAnimation();

    }

    public static int getMinimumFlingVelocity() {
        return mMinimumFlingVelocity;
    }

    public static int getMaximumFlingVelocity() {
        return mMaximumFlingVelocity;
    }

    public static float getNormalizedAngle(float angle) {
        while(angle < 0.0F) {
            angle += 360.0F;
        }

        return angle % 360.0F;
    }

    public static void drawImage(Canvas canvas, Drawable drawable, int x, int y, int width, int height) {
        MPPointF drawOffset = MPPointF.getInstance();
        drawOffset.x = (float)(x - width / 2);
        drawOffset.y = (float)(y - height / 2);
        drawable.copyBounds(mDrawableBoundsCache);
        drawable.setBounds(mDrawableBoundsCache.left, mDrawableBoundsCache.top, mDrawableBoundsCache.left + width, mDrawableBoundsCache.top + width);
        int saveId = canvas.save();
        canvas.translate(drawOffset.x, drawOffset.y);
        drawable.draw(canvas);
        canvas.restoreToCount(saveId);
    }

    public static void drawXAxisValue(Canvas c, String text, float x, float y, Paint paint, MPPointF anchor, float angleDegrees) {
        float drawOffsetX = 0.0F;
        float drawOffsetY = 0.0F;
        float lineHeight = paint.getFontMetrics(mFontMetricsBuffer);
        paint.getTextBounds(text, 0, text.length(), mDrawTextRectBuffer);
        drawOffsetX -= (float)mDrawTextRectBuffer.left;
        drawOffsetY += -mFontMetricsBuffer.ascent;
        Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);
        if (angleDegrees != 0.0F) {
            drawOffsetX -= (float)mDrawTextRectBuffer.width() * 0.5F;
            drawOffsetY -= lineHeight * 0.5F;
            float translateX = x;
            float translateY = y;
            if (anchor.x != 0.5F || anchor.y != 0.5F) {
                FSize rotatedSize = getSizeOfRotatedRectangleByDegrees((float)mDrawTextRectBuffer.width(), lineHeight, angleDegrees);
                translateX = x - rotatedSize.width * (anchor.x - 0.5F);
                translateY = y - rotatedSize.height * (anchor.y - 0.5F);
                FSize.recycleInstance(rotatedSize);
            }

            c.save();
            c.translate(translateX, translateY);
            c.rotate(angleDegrees);
            c.drawText(text, drawOffsetX, drawOffsetY, paint);
            c.restore();
        } else {
            if (anchor.x != 0.0F || anchor.y != 0.0F) {
                drawOffsetX -= (float)mDrawTextRectBuffer.width() * anchor.x;
                drawOffsetY -= lineHeight * anchor.y;
            }

            drawOffsetX += x;
            drawOffsetY += y;
            c.drawText(text, drawOffsetX, drawOffsetY, paint);
        }

        paint.setTextAlign(originalTextAlign);
    }

    public static void drawMultilineText(Canvas c, StaticLayout textLayout, float x, float y, TextPaint paint, MPPointF anchor, float angleDegrees) {
        float drawOffsetX = 0.0F;
        float drawOffsetY = 0.0F;
        float lineHeight = paint.getFontMetrics(mFontMetricsBuffer);
        float drawWidth = (float)textLayout.getWidth();
        float drawHeight = (float)textLayout.getLineCount() * lineHeight;
        drawOffsetX -= (float)mDrawTextRectBuffer.left;
        drawOffsetY += drawHeight;
        Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);
        if (angleDegrees != 0.0F) {
            drawOffsetX -= drawWidth * 0.5F;
            drawOffsetY -= drawHeight * 0.5F;
            float translateX = x;
            float translateY = y;
            if (anchor.x != 0.5F || anchor.y != 0.5F) {
                FSize rotatedSize = getSizeOfRotatedRectangleByDegrees(drawWidth, drawHeight, angleDegrees);
                translateX = x - rotatedSize.width * (anchor.x - 0.5F);
                translateY = y - rotatedSize.height * (anchor.y - 0.5F);
                FSize.recycleInstance(rotatedSize);
            }

            c.save();
            c.translate(translateX, translateY);
            c.rotate(angleDegrees);
        } else {
            if (anchor.x != 0.0F || anchor.y != 0.0F) {
                drawOffsetX -= drawWidth * anchor.x;
                drawOffsetY -= drawHeight * anchor.y;
            }

            drawOffsetX += x;
            drawOffsetY += y;
            c.save();
        }
        c.translate(drawOffsetX, drawOffsetY);
        textLayout.draw(c);
        c.restore();

        paint.setTextAlign(originalTextAlign);
    }

    public static void drawMultilineText(Canvas c, String text, float x, float y, TextPaint paint, FSize constrainedToSize, MPPointF anchor, float angleDegrees) {
        StaticLayout textLayout = new StaticLayout(text, 0, text.length(), paint, (int)Math.max(Math.ceil(constrainedToSize.width), 1.0D), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
        drawMultilineText(c, textLayout, x, y, paint, anchor, angleDegrees);
    }

    public static FSize getSizeOfRotatedRectangleByDegrees(FSize rectangleSize, float degrees) {
        float radians = degrees * 0.017453292F;
        return getSizeOfRotatedRectangleByRadians(rectangleSize.width, rectangleSize.height, radians);
    }

    public static FSize getSizeOfRotatedRectangleByRadians(FSize rectangleSize, float radians) {
        return getSizeOfRotatedRectangleByRadians(rectangleSize.width, rectangleSize.height, radians);
    }

    public static FSize getSizeOfRotatedRectangleByDegrees(float rectangleWidth, float rectangleHeight, float degrees) {
        float radians = degrees * 0.017453292F;
        return getSizeOfRotatedRectangleByRadians(rectangleWidth, rectangleHeight, radians);
    }

    public static FSize getSizeOfRotatedRectangleByRadians(float rectangleWidth, float rectangleHeight, float radians) {
        return FSize.getInstance(Math.abs(rectangleWidth * (float)Math.cos(radians)) + Math.abs(rectangleHeight * (float)Math.sin(radians)), Math.abs(rectangleWidth * (float)Math.sin(radians)) + Math.abs(rectangleHeight * (float)Math.cos(radians)));
    }

    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }
}
