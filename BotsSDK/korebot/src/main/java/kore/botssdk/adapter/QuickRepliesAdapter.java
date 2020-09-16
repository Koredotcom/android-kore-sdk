package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.view.viewHolder.QuickReplyViewHolder;

/**
 * Created by Pradeep Mahato on 28/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class QuickRepliesAdapter extends RecyclerView.Adapter<QuickReplyViewHolder> {

    private ArrayList<QuickReplyTemplate> quickReplyTemplateArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView parentRecyclerView;

    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private int quickWidgetColor,fillColor,quickReplyFontColor;
    private int dp1;

    public QuickRepliesAdapter(Context context, RecyclerView parentRecyclerView) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
        layoutInflater = LayoutInflater.from(context);
        quickWidgetColor = Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor);
        fillColor = Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor);
        quickReplyFontColor = Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor);
        dp1= (int)AppControl.getInstance(context).getDimensionUtil().dp1;
    }

    @NonNull
    @Override
    public QuickReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.quick_reply_item_layout, null);
        GradientDrawable gradientDrawable = (GradientDrawable)convertView.findViewById(R.id.quick_reply_view).getBackground();
        gradientDrawable.setStroke(dp1, quickWidgetColor);
        gradientDrawable.setColor(fillColor);
        QuickReplyViewHolder viewHolder = new QuickReplyViewHolder(convertView);
        viewHolder.getQuickReplyTitle().setTextColor(quickReplyFontColor);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuickReplyViewHolder holder, int position) {
        QuickReplyTemplate quickReplyTemplate = quickReplyTemplateArrayList.get(position);

        if (quickReplyTemplate.getImage_url() != null && !quickReplyTemplate.getImage_url().isEmpty()) {
            Picasso.get().load(quickReplyTemplate.getImage_url()).into(holder.getQuickReplyImage());
            holder.getQuickReplyImage().setVisibility(View.VISIBLE);
        } else {
            holder.getQuickReplyImage().setVisibility(View.GONE);
        }

        holder.getQuickReplyTitle().setText(quickReplyTemplate.getTitle());

        holder.getQuickReplyRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =  parentRecyclerView.getChildPosition(v);
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    QuickReplyTemplate quickReplyTemplate = quickReplyTemplateArrayList.get(position);
                    if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(quickReplyTemplate.getContent_type())) {
                        String quickReplyTitle = quickReplyTemplate.getTitle();
                        String quickReplyPayload = quickReplyTemplate.getPayload();
                        composeFooterInterface.onSendClick(quickReplyTitle, quickReplyPayload,false);
                    } else if(BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(quickReplyTemplate.getContent_type())){
                        invokeGenericWebViewInterface.invokeGenericWebView(BundleConstants.BUTTON_TYPE_USER_INTENT);
                    }else if(BundleConstants.BUTTON_TYPE_TEXT.equalsIgnoreCase(quickReplyTemplate.getContent_type())){
                        composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(),quickReplyTemplate.getPayload(),false);
                    }else if(BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(quickReplyTemplate.getContent_type())){
                        invokeGenericWebViewInterface.invokeGenericWebView(quickReplyTemplate.getPayload());
                    }else{
                        String quickReplyTitle = quickReplyTemplate.getTitle();
                        String quickReplyPayload = quickReplyTemplate.getPayload();
                        composeFooterInterface.onSendClick(quickReplyTitle, quickReplyPayload,false);
                    }
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        if (quickReplyTemplateArrayList == null) {
            return 0;
        } else {
            return quickReplyTemplateArrayList.size();
        }
    }

    public void setQuickReplyTemplateArrayList(ArrayList<QuickReplyTemplate> quickReplyTemplateArrayList) {
        this.quickReplyTemplateArrayList = quickReplyTemplateArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
