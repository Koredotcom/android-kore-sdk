package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListElementButton;
import kore.botssdk.models.BotListModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotListTemplateAdapter extends BaseAdapter {

    String LOG_TAG = BotListTemplateAdapter.class.getSimpleName();

    ArrayList<BotListModel> botListModelArrayList = new ArrayList<>();
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    LayoutInflater ownLayoutInflator;
    Context context;
    RoundedCornersTransform roundedCornersTransform;
    ListView parentListView;

    public BotListTemplateAdapter(Context context, ListView parentListView) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
    }

    @Override
    public int getCount() {
        if (botListModelArrayList != null) {
            return botListModelArrayList.size() >3 ? 3 : botListModelArrayList.size();
        } else {
            return 0;
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
            convertView = ownLayoutInflator.inflate(R.layout.bot_list_template_item_layout, null);
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
        if(!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        if(!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getSubtitle());
        }
        if (botListModel.getButtons() == null || botListModel.getButtons().isEmpty()) {
            holder.botListItemButton.setVisibility(View.GONE);
        } else {
            holder.botListItemButton.setVisibility(View.VISIBLE);
            holder.botListItemButton.setText(botListModel.getButtons().get(0).getTitle());
            holder.botListItemButton.setTag(botListModel.getButtons().get(0));

            holder.botListItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        BotListElementButton botListElementButton = (BotListElementButton) v.getTag();
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListElementButton.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botListElementButton.getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botListElementButton.getType())) {
                            String listElementButtonPayload = botListElementButton.getPayload();
                            String listElementButtonTitle = botListElementButton.getTitle();
                            composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload,false);
                        }
                    }
                }
            });
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
                            composeFooterInterface.onSendClick(_botListModel.getDefault_action().getPayload(),false);
                        }
                    }
                }
            }
        });

    }

    public void setBotListModelArrayList(ArrayList<BotListModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();

        holder.botListItemRoot = (RelativeLayout) view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = (ImageView) view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = (TextView) view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = (TextView) view.findViewById(R.id.bot_list_item_subtitle);
        holder.botListItemButton = (Button) view.findViewById(R.id.bot_list_item_button);

        view.setTag(holder);
    }

    private static class ViewHolder {
        RelativeLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle;
        Button botListItemButton;
    }
}
