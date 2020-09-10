package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

public class BotListViewTemplateAdapter extends BaseAdapter {

    String LOG_TAG = BotListTemplateAdapter.class.getSimpleName();
    ArrayList<BotListModel> botListModelArrayList = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    LayoutInflater ownLayoutInflator;
    Context context;
    RoundedCornersTransform roundedCornersTransform;
    ListView parentListView;
    private GradientDrawable bgDrawable;
    int count = 0;

    public BotListViewTemplateAdapter(Context context, ListView parentListView, int count) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
        this.count = count;
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
    public BotListModel getItem(int position) {
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
            convertView = ownLayoutInflator.inflate(R.layout.bot_listview_template_item_layout, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotListModel botListModel = getItem(position);
        holder.botListItemImage.setVisibility(View.GONE);

        GradientDrawable rightDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.rounded_rect_feedback);
        rightDrawable.setColor(Color.parseColor("#FFFFFF"));

        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        String themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1);
        String themeBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff");
        String themeTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#444444");
        rightDrawable.setColor(Color.parseColor(themeBgColor));

        if(themeName.equalsIgnoreCase(BotResponse.THEME_NAME_1))
        {
            rightDrawable.setStroke((int) (1*dp1), Color.parseColor("#ffffff"));
            holder.botListItemRoot.setBackground(rightDrawable);
        }
        else
        {
            rightDrawable.setStroke((int) (2*dp1), Color.parseColor("#d3d3d3"));
            holder.botListItemRoot.setBackground(rightDrawable);
        }



        if(!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        holder.botListItemTitle.setTextColor(Color.parseColor(themeTextColor));
        holder.bot_list_item_cost.setText(botListModel.getValue());

        if(botListModel.getColor() != null)
            holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getColor()));

        holder.bot_list_item_cost.setTypeface(null, Typeface.BOLD);

        if(!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getSubtitle());
        }

        holder.botListItemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    int position = parentListView.getPositionForView(v);
                    BotListModel _botListModel = getItem(position);
                    if (_botListModel != null && _botListModel.getDefault_action() != null) {
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(_botListModel.getDefault_action().getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {

                            if(!StringUtils.isNullOrEmpty(_botListModel.getDefault_action().getPayload()))
                                composeFooterInterface.onSendClick(_botListModel.getDefault_action().getPayload(),false);
                            else if(!StringUtils.isNullOrEmpty(_botListModel.getDefault_action().getTitle()))
                                composeFooterInterface.onSendClick(_botListModel.getDefault_action().getTitle(),false);
                        }
                    }
                }
            }
        });

    }

    public void setBotListModelArrayList(ArrayList<BotListModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
        notifyDataSetChanged();
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();

        holder.botListItemRoot = (LinearLayout) view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = (ImageView) view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = (TextView) view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = (TextView) view.findViewById(R.id.bot_list_item_subtitle);
        holder.bot_list_item_cost = (TextView) view.findViewById(R.id.bot_list_item_cost);

        view.setTag(holder);
    }

    private static class ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_cost;
    }
}

