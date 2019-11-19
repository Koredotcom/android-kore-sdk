package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.MultiAction;
import kore.botssdk.models.Widget.Element;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class ChartListWidgetAdapter extends RecyclerView.Adapter {
    private boolean isExpanded = false;
    VerticalListViewActionHelper verticalListViewActionHelper;

    ArrayList<Element> eventList = new ArrayList<>();
    private LayoutInflater inflater = null;
    private Context mContext;

    private int DATA_FOUND = 1;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private ComposeFooterInterface composeFooterInterface;

    List<MultiAction> multiActions;
    int preview_length;
    String msg;
    Drawable errorIcon;

    public ChartListWidgetAdapter(Context mContext, String type, boolean isEnabled, boolean isFromFullView) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.type = type;
        notifyDataSetChanged();
    }

    public Element getItem(int position) {
        if (position < eventList.size())
            return eventList.get(position);
        else return null;
    }


    @Override
    public int getItemViewType(int position) {
        return DATA_FOUND;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ChartListWidgetAdapter.ViewHolder(inflater.inflate(R.layout.chart_list_item, parent, false));
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderData, int position) {

        ChartListWidgetAdapter.ViewHolder holder = (ChartListWidgetAdapter.ViewHolder) holderData;
        final Element model = eventList.get(position);

        int dp80 = (int)dp1*80;

        String title = model.getTitle();
        String text = model.getText();
        String color = model.getTheme();
        String type = model.getType();

        if(!StringUtils.isNullOrEmpty(title)){
            holder.title.setText(title);
        }
        int shape = 1;
        if(!StringUtils.isNullOrEmpty(type) && type.equalsIgnoreCase("circle")){
            shape = GradientDrawable.OVAL;
        }else if(!StringUtils.isNullOrEmpty(type) && type.equalsIgnoreCase("rectange")){
            shape = GradientDrawable.RECTANGLE;
        }else if(!StringUtils.isNullOrEmpty(type) && type.equalsIgnoreCase("ring")){
            shape = GradientDrawable.RING;
        }

        if(!StringUtils.isNullOrEmpty(text)){
            holder.text.setText(text);

            Drawable dr = mContext.getDrawable(R.drawable.selected_video);

            GradientDrawable drawable = (GradientDrawable)dr;
            drawable.setColor(Color.parseColor(color));
            drawable.setStroke(10, Utility.getDarkerColor(Color.parseColor(color), 0.7f));
            drawable.setShape(shape);
            holder.text.setBackground(drawable);
        }
    }

    public void setCalData(List<Element> data) {
        this.eventList = (ArrayList<Element>) data;
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(Utility.isIsSingleItemInList())
        {
            return eventList != null && eventList.size() > 0 ? eventList.size() : 1;
        }
        return eventList != null && eventList.size() > 0 ? (!isExpanded && eventList.size() > preview_length ? preview_length : eventList.size()) : 1;
    }


    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setMultiActions(List<MultiAction> multiActions) {
        this.multiActions = multiActions;
    }

    public List<MultiAction> getMultiActions() {
        return multiActions;
    }

    public void setPreviewLength(int preview_length) {
        this.preview_length = preview_length;
    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg = msg;
        this.errorIcon = errorIcon;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.txtId);
            title = itemView.findViewById(R.id.titleTV);
        }
    }
}
