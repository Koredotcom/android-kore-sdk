package kore.botssdk.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import kore.botssdk.R;

public class CustomTextViewBold extends AppCompatTextView {

    String customFont;

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        Typeface tfRegular = ResourcesCompat.getFont(context, R.font.latobold);
        setTypeface(tfRegular);
        a.recycle();
    }
}
