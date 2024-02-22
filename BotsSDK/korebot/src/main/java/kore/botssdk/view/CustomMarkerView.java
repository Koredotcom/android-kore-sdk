package kore.botssdk.view;

import android.content.Context;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.charts.components.MarkerView;
import kore.botssdk.charts.data.CandleEntry;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;

/**
 * Created by Shiva Krishna on 11/7/2017.
 */

public class CustomMarkerView extends MarkerView {

    private final TextView tvContent;
    public CustomMarkerView (Context context, int layoutResource) {
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

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText(Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}