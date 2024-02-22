package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListWidgetModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class BotListWidgetTemplateAdapter extends BaseAdapter {
    private ArrayList<BotListWidgetModel> botListModelArrayList = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final RoundedCornersTransform roundedCornersTransform;
    final ListView parentListView;
    private final int count;
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public BotListWidgetTemplateAdapter(@NonNull Context context, @NonNull ListView parentListView, int count) {
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
        this.count = count;
        this.sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        this.context = context;
    }

    @Override
    public int getCount() {
        if (count > 0) {
            return count;
        } else {
            return botListModelArrayList.size();
        }
    }

    @Override
    public BotListWidgetModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return botListModelArrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.bot_listwidget_template_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotListWidgetModel botListModel = getItem(position);
        holder.botListItemImage.setVisibility(View.GONE);

        if(!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.bot_list_item_cost.setText(botListModel.getValue().getText());

        if(botListModel.getColor() != null)
            holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getColor()));

        if(!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getSubtitle());

            if(sharedPreferences != null)
                holder.botListItemSubtitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, "#505968")));
        }

        holder.botListItemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    int position = parentListView.getPositionForView(v);
                    BotListWidgetModel _botListModel = getItem(position);
                    if (_botListModel != null && _botListModel.getDefault_action() != null) {
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(_botListModel.getDefault_action().getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {

                            if(!StringUtils.isNullOrEmpty(_botListModel.getTitle()) && !StringUtils.isNullOrEmpty(botListModel.getSubtitle()))
                                composeFooterInterface.onSendClick(_botListModel.getTitle()+"\n"+_botListModel.getSubtitle(),false);
                            else if(!StringUtils.isNullOrEmpty(_botListModel.getTitle()))
                                composeFooterInterface.onSendClick(_botListModel.getTitle(),false);
                        }
                    }
                }
            }
        });

    }

    public void setBotListModelArrayList(@NonNull ArrayList<BotListWidgetModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();

        holder.botListItemRoot = view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);
        holder.bot_list_item_cost = view.findViewById(R.id.bot_list_item_cost);

        view.setTag(holder);
    }

    static class ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_cost;
    }
}
