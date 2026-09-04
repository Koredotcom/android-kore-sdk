package kore.botssdk.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;

public class CustomTypefaceSpan extends MetricAffectingSpan {
    private final Typeface typeface;

    public CustomTypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, typeface);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        applyCustomTypeFace(paint, typeface);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        if (tf != null) {
            paint.setTypeface(tf);
        }
    }
}
