package kore.botssdk.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import kore.botssdk.R;

public class CustomEditTextView extends AppCompatEditText {

    String customFont;

    public CustomEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {

        Typeface tfRegular = ResourcesCompat.getFont(context, R.font.latoregular);
        setTypeface(tfRegular);
    }
}
