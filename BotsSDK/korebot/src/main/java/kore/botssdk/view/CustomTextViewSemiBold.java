package kore.botssdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import kore.botssdk.R;

public class CustomTextViewSemiBold extends AppCompatTextView {


    public CustomTextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomTextViewSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        Typeface tfRegular = ResourcesCompat.getFont(context, R.font.latosemibold);
        setTypeface(tfRegular);
        a.recycle();
    }
}
