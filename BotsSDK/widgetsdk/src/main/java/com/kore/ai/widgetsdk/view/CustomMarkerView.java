package com.kore.ai.widgetsdk.view;

import android.content.Context;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.charts.components.MarkerView;
import com.kore.ai.widgetsdk.charts.data.CandleEntry;
import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.highlight.Highlight;
import com.kore.ai.widgetsdk.charts.utils.MPPointF;
import com.kore.ai.widgetsdk.charts.utils.Utils;

/**
 * Created by Shiva Krishna on 11/7/2017.
 */

public class CustomMarkerView extends MarkerView {

    private final TextView tvContent;
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}