package kore.botssdk.adapter;

import static kore.botssdk.viewholders.BaseViewHolder.getTintDrawable;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.AdvanceListRefreshEvent;
import kore.botssdk.models.AdvanceOptionsModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.LogUtils;

public class AdvanceOptionsAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<AdvanceOptionsModel> contentModels;

    protected AdvanceOptionsAdapter(@NonNull Context context, @NonNull ArrayList<AdvanceOptionsModel> contentModels) {
        this.context = context;
        this.contentModels = contentModels;
        KoreEventCenter.register(this);
    }

    @Override
    public int getCount() {
        return contentModels.size();
    }

    @Override
    public Object getItem(int i) {
        return contentModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AdvanceOptionsAdapter.DetailsViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.advance_options_view, null);
            KaFontUtils.applyCustomFont(context, convertView);
            holder = new AdvanceOptionsAdapter.DetailsViewHolder();
            holder.tvBtnText = convertView.findViewById(R.id.tvBtnText);
            holder.ivOptions = convertView.findViewById(R.id.ivOptions);
            holder.llOptions = convertView.findViewById(R.id.llOptions);
            convertView.setTag(holder);
        } else {
            holder = (AdvanceOptionsAdapter.DetailsViewHolder) convertView.getTag();
        }

        populateData(holder, position);

        return convertView;
    }

    private void populateData(AdvanceOptionsAdapter.DetailsViewHolder holder, int position) {
        AdvanceOptionsModel dataObj = (AdvanceOptionsModel) getItem(position);
        holder.tvBtnText.setText(dataObj.getLabel());
        holder.ivOptions.setVisibility(View.VISIBLE);
        holder.ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataObj.getType().equalsIgnoreCase("radio"))
                    setChecked(position);
                else
                    dataObj.setChecked(!dataObj.isChecked());

                notifyDataSetChanged();
            }
        });

        holder.llOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataObj.getType().equalsIgnoreCase("radio"))
                    setChecked(position);
                else
                    dataObj.setChecked(!dataObj.isChecked());

                notifyDataSetChanged();
            }
        });

        if (dataObj.getType().equalsIgnoreCase("radio")) {
            holder.ivOptions.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_check_off));

            if (dataObj.isChecked())
                holder.ivOptions.setBackground(getTintDrawable(holder.ivOptions.getContext(), SDKConfiguration.BubbleColors.quickReplyColor, R.drawable.ic_radio_checked));
        } else {
            holder.ivOptions.setBackground(ContextCompat.getDrawable(context, R.mipmap.multi_un_checked_checkbox));

            if (dataObj.isChecked()) {
                holder.ivOptions.setBackground(ContextCompat.getDrawable(context, R.mipmap.multi_checked_checkbox));
            }
        }
    }

    @NonNull
    public ArrayList<AdvanceOptionsModel> getData() {
        return contentModels;
    }

    public void setChecked(int pos) {
        int i = 0;
        while (i < contentModels.size()) {
            contentModels.get(i).setChecked(false);

            if (pos == i)
                contentModels.get(i).setChecked(true);
            i++;
        }
        notifyDataSetChanged();
    }

    public void onEvent(@NonNull AdvanceListRefreshEvent advanceListRefreshEvent) {
        LogUtils.e("Testing", advanceListRefreshEvent.getPayLoad());
        notifyDataSetChanged();
    }

    static class DetailsViewHolder {
        TextView tvBtnText;
        ImageView ivOptions;
        LinearLayout llOptions;
    }
}
