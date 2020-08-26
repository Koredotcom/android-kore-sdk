package com.kore.ai.widgetsdk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.listeners.ComposeFooterInterface;
import com.kore.ai.widgetsdk.listeners.InvokeGenericWebViewInterface;
import com.kore.ai.widgetsdk.models.BotTableListElementsItemsModel;
import com.kore.ai.widgetsdk.models.BotTableListRowItemsModel;
import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.views.viewutils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BotTableListInnerAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<BotTableListElementsItemsModel> botTableListRowItemsModels;
    String LOG_TAG = BotTableListInnerAdapter.class.getSimpleName();
    LayoutInflater ownLayoutInflator;
    ListView parentListView;
    RoundedCornersTransform roundedCornersTransform;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private int dp1;

    protected BotTableListInnerAdapter(Context context, ArrayList<BotTableListElementsItemsModel> botTableListRowItemsModels)
    {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.botTableListRowItemsModels = botTableListRowItemsModels;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }

    @Override
    public int getCount()
    {
        return botTableListRowItemsModels.size();
    }

    @Override
    public BotTableListElementsItemsModel getItem(int position)
    {
        return botTableListRowItemsModels.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = ownLayoutInflator.inflate(R.layout.bot_table_list_inner_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotTableListElementsItemsModel botListModel = getItem(position);
        holder.botListItemImage.setVisibility(View.GONE);

        if(botListModel.getImage() != null)
        {
            if(!StringUtils.isNullOrEmpty(botListModel.getImage().getImage_src()))
            {
                holder.botListItemImage.setVisibility(View.VISIBLE);
                Picasso.get().load(botListModel.getImage().getImage_src()).transform(roundedCornersTransform).into(holder.botListItemImage);

                if(botListModel.getImage().getRadius() > 0)
                {
                    holder.botListItemImage.getLayoutParams().height = botListModel.getImage().getRadius() * 2 * dp1;
                    holder.botListItemImage.getLayoutParams().width = botListModel.getImage().getRadius() * 2 * dp1;
                }
            }
        }

        holder.botListItemTitle.setTag(botListModel);

        if(botListModel.getTitle() != null)
        {
            holder.botListItemTitle.setText(botListModel.getTitle());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);

            if(!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
                holder.botListItemSubtitle.setVisibility(View.VISIBLE);
                holder.botListItemSubtitle.setText(botListModel.getSubtitle());
            }
        }
//        else
//            if(botListModel.getTitle().getUrl() != null)
//        {
//            holder.botListItemTitle.setText(botListModel.getTitle().getUrl().getTitle());
//            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
//
//            if(!StringUtils.isNullOrEmpty(botListModel.getTitle().getUrl().getSubtitle())) {
//                holder.botListItemSubtitle.setVisibility(View.VISIBLE);
//                holder.botListItemSubtitle.setText(botListModel.getTitle().getUrl().getSubtitle());
//            }
//        }

        holder.bot_list_item_cost.setTextColor(context.getResources().getColor(R.color.txtFontBlack));

//        if(!StringUtils.isNullOrEmpty(botListModel.getTitle().getRowColor()))
//        {
//            holder.botListItemTitle.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
//            holder.botListItemSubtitle.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
//            holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
//        }

        if(botListModel.getValue().getType().equalsIgnoreCase("text"))
        {
            if(!StringUtils.isNullOrEmpty(botListModel.getValue().getText())) {
                holder.bot_list_item_cost.setVisibility(View.VISIBLE);
                holder.bot_list_item_cost.setText(botListModel.getValue().getText());

                if(botListModel.getValue().getLayout() != null && !StringUtils.isNullOrEmpty(botListModel.getValue().getLayout().getColor()))
                {
                    holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getValue().getLayout().getColor()));
                }
            }
        }
        else if (botListModel.getValue().getType().equalsIgnoreCase("url"))
        {
            if(!StringUtils.isNullOrEmpty(botListModel.getValue().getUrl().getTitle())) {
                holder.bot_list_item_cost.setVisibility(View.VISIBLE);
                holder.bot_list_item_cost.setText(botListModel.getValue().getUrl().getTitle());
            }
        }

        holder.botListItemRoot.setBackgroundColor(0);

        if(!StringUtils.isNullOrEmpty(botListModel.getBgcolor())) {
            holder.botListItemRoot.setBackgroundColor(Color.parseColor(botListModel.getBgcolor()));
        }

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
