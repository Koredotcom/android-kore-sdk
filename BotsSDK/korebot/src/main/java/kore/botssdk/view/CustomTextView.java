package kore.botssdk.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import kore.botssdk.R;
import kore.botssdk.net.SDKConfiguration;


public class CustomTextView extends AppCompatTextView {

    String customFont;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);
    }

    private void style(Context context, AttributeSet attrs) {
        if (SDKConfiguration.getRegular() != null) {
            setTypeface(SDKConfiguration.getRegular());
            return;
        }
        Typeface tfRegular = ResourcesCompat.getFont(context, R.font.latoregular);
        setTypeface(tfRegular);
    }
}
