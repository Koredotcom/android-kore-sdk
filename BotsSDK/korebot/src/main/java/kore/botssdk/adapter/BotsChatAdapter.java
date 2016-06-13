package kore.botssdk.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.view.BaseBubbleContainer;
import kore.botssdk.view.BaseBubbleLayout;
import kore.botssdk.view.ReceivedBubbleContainer;
import kore.botssdk.view.ReceivedBubbleLayout;
import kore.botssdk.view.SendBubbleContainer;
import kore.botssdk.view.SendBubbleLayout;
import kore.botssdk.view.viewUtils.BubbleViewUtil;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotsChatAdapter extends BaseAdapter {

    public static String LOG_TAG = BotsChatAdapter.class.getSimpleName();

    Context context;
    private LayoutInflater ownLayoutInflater;
    private int BUBBLE_CONTENT_LAYOUT_WIDTH;
    private int BUBBLE_CONTENT_LAYOUT_HEIGHT;
    private ListView listView;
    int viewWidth;
    int dp1;
    int position;
    boolean shallShowProfilePic;

    private ArrayList<BaseBotMessage> baseBotMessageArrayList;

    public static final int BUBBLE_ADAPTER_DIFFERENT_VIEW_COUNTS = 3;
    public static final int BUBBLE_LEFT_LAYOUT = 0;
    public static final int BUBBLE_RIGHT_LAYOUT = BUBBLE_LEFT_LAYOUT + 1;

    public BotsChatAdapter(Context context) {
        this.context = context;
        ownLayoutInflater = LayoutInflater.from(context);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;

        BUBBLE_CONTENT_LAYOUT_WIDTH = BubbleViewUtil.getBubbleContentWidth();
        BUBBLE_CONTENT_LAYOUT_HEIGHT = BubbleViewUtil.getBubbleContentHeight();
        viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        baseBotMessageArrayList = new ArrayList<>();
    }

    @Override
    public int getViewTypeCount() {
        return BUBBLE_ADAPTER_DIFFERENT_VIEW_COUNTS;
    }

    @Override
    public int getItemViewType(int position) {

        BaseBotMessage baseBotMessage = getItem(position);

        if (baseBotMessage.isSend()) {
            return BUBBLE_RIGHT_LAYOUT;
        } else {
            return BUBBLE_LEFT_LAYOUT;
        }

    }

    @Override
    public int getCount() {
        return baseBotMessageArrayList.size();
    }

    @Override
    public BaseBotMessage getItem(int position) {
        return baseBotMessageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.position = position;
        boolean isRight = false;

        int itemViewType = getItemViewType(position);
        if (itemViewType == BUBBLE_RIGHT_LAYOUT) {
            // Inflation for Right side
            if (convertView == null || convertView.getId() != R.id.send_bubble_layout_container)
                convertView = ownLayoutInflater.inflate(R.layout.bubble_layout_right, null);
            isRight = true;
        } else {
            // Inflation for Left side
            if (convertView == null || convertView.getId() != R.id.received_bubble_layout_container)
                convertView = ownLayoutInflater.inflate(R.layout.bubble_layout_left, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView, position);
        }

        if (listView == null) {
            listView = (ListView) parent;
        }
        if (convertView.getTag() != null) {
            // Stuffs
            ViewHolder holder = (ViewHolder) convertView.getTag();

            //Bubble Population logic
            holder.baseBubbleContainer.setDimensions(BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);

            holder.baseBubbleLayout.setContinuousMessage(false);
            holder.baseBubbleLayout.setGroupMessage(shallShowProfilePic);

            holder.baseBubbleLayout.fillBubbleLayout(position, getItem(position), true, BUBBLE_CONTENT_LAYOUT_WIDTH, BUBBLE_CONTENT_LAYOUT_HEIGHT);

        }

        return convertView;
    }

    /**
     * View Holder Initialization
     */
    private View initializeViewHolder(View view, int position) {

        ViewHolder holder = new ViewHolder();

        if (getItemViewType(position) == BUBBLE_RIGHT_LAYOUT) {
            // Right Side
            holder.baseBubbleLayout = (SendBubbleLayout) view.findViewById(R.id.sendBubbleLayout);
            holder.baseBubbleContainer = (SendBubbleContainer) view.findViewById(R.id.send_bubble_layout_container);
        } else {
            // Left Side
            holder.baseBubbleLayout = (ReceivedBubbleLayout) view.findViewById(R.id.receivedBubbleLayout);
            holder.baseBubbleContainer = (ReceivedBubbleContainer) view.findViewById(R.id.received_bubble_layout_container);
        }

        view.setTag(holder);

        return view;

    }

    private static class ViewHolder {
        BaseBubbleContainer baseBubbleContainer;
        BaseBubbleLayout baseBubbleLayout;
        RelativeLayout bubbleLayoutContainer;
    }

    public void addBaseBotMessage(BaseBotMessage baseBotMessage) {
        baseBotMessageArrayList.add(baseBotMessage);
        notifyDataSetChanged();
    }

    public void setShallShowProfilePic(boolean shallShowProfilePic) {
        this.shallShowProfilePic = shallShowProfilePic;
    }
}
