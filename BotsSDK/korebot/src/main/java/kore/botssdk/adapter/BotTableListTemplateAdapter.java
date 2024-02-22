package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotTableListModel;
import kore.botssdk.models.BotTableListRowItemsModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.AutoExpandListView;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class BotTableListTemplateAdapter extends BaseAdapter {
    ArrayList<BotTableListModel> botTableListModels = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    final LayoutInflater ownLayoutInflator;
    final Context context;
    final RoundedCornersTransform roundedCornersTransform;
    final ListView parentListView;
    final int count;

    public BotTableListTemplateAdapter(@NonNull Context context, @NonNull ListView parentListView, int count) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
        this.count = count;
    }

    @Override
    public int getCount() {
        if (botTableListModels != null) {
            return Math.min(botTableListModels.size(), count);
        } else {
            return 0;
        }
    }

    @Override
    public BotTableListModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return botTableListModels.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.bot_table_list_view, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        BotTableListModel botListModel = getItem(position);

        if(!StringUtils.isNullOrEmpty(botListModel.getSectionHeader())) {
            holder.botListItemTitle.setTag(botListModel);
            holder.botListItemTitle.setVisibility(View.VISIBLE);
            holder.botListItemTitle.setText(botListModel.getSectionHeader());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        }

        if(!StringUtils.isNullOrEmpty(botListModel.getSectionHeaderDesc())) {
            holder.bot_list_item_desc.setTag(botListModel);
            holder.bot_list_item_desc.setVisibility(View.VISIBLE);
            holder.bot_list_item_desc.setText(botListModel.getSectionHeaderDesc());
            holder.bot_list_item_desc.setTypeface(null, Typeface.BOLD);
        }

        if(botListModel.getRowItems() != null && botListModel.getRowItems().size() > 0)
        {
            BotTableListInnerAdapter botTableListInnerAdapter = new BotTableListInnerAdapter(context, botListModel.getRowItems());
            botTableListInnerAdapter.setComposeFooterInterface(composeFooterInterface);
            botTableListInnerAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            holder.botTableListView.setAdapter(botTableListInnerAdapter);

            holder.botTableListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    BotTableListRowItemsModel botTableListRowItemsModel = botListModel.getRowItems().get(position);

                    if(botTableListRowItemsModel.getDefault_action() != null && botTableListRowItemsModel.getDefault_action().getType() != null &&
                            botTableListRowItemsModel.getDefault_action().getType().equalsIgnoreCase("postback"))
                    {
                        if(composeFooterInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getDefault_action().getPayload())) {
                            composeFooterInterface.onSendClick(botTableListRowItemsModel.getDefault_action().getTitle(), botTableListRowItemsModel.getDefault_action().getPayload(),false);
                        }
                    }
                    else if(botTableListRowItemsModel.getDefault_action() != null && botTableListRowItemsModel.getDefault_action().getType() != null &&
                            botTableListRowItemsModel.getDefault_action().getType().equalsIgnoreCase("url"))
                    {
                        if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getDefault_action().getUrl())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botTableListRowItemsModel.getDefault_action().getUrl());
                        }
                    }
                    else if(botTableListRowItemsModel.getDefault_action() == null && botTableListRowItemsModel.getTitle() != null
                        && botTableListRowItemsModel.getTitle().getUrl() != null)
                    {
                        if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botTableListRowItemsModel.getTitle().getUrl().getLink())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botTableListRowItemsModel.getTitle().getUrl().getLink());
                        }
                    }
                }
            });
        }
    }

    public void setBotListModelArrayList(@NonNull ArrayList<BotTableListModel> botListModelArrayList) {
        this.botTableListModels = botListModelArrayList;
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
        holder.botListItemTitle = view.findViewById(R.id.bot_list_item_title);
        holder.bot_list_item_desc = view.findViewById(R.id.bot_list_item_desc);
        holder.botTableListView = view.findViewById(R.id.botTableListView);
        view.setTag(holder);
    }

    static class ViewHolder {
        TextView botListItemTitle;
        TextView bot_list_item_desc;
        AutoExpandListView botTableListView;
    }
}
