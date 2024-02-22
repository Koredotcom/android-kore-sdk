package kore.botssdk.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import androidx.annotation.NonNull;

public class AutoExpandListView extends ListView {

    boolean expanded = true;

    public AutoExpandListView(@NonNull Context context, @NonNull AttributeSet attrs,
                              int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public AutoExpandListView(@NonNull Context context){
        super(context,null);
    }

    public AutoExpandListView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            android.view.ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
