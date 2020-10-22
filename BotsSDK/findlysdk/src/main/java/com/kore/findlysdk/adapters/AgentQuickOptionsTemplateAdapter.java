package com.kore.findlysdk.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.BotButtonModel;
import com.kore.findlysdk.net.SDKConfiguration;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.view.viewHolder.QuickReplyViewHolder;

import java.util.ArrayList;

public class AgentQuickOptionsTemplateAdapter extends RecyclerView.Adapter<QuickReplyViewHolder> {

    private ArrayList<BotButtonModel> quickReplyTemplateArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView parentRecyclerView;

    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private int quickWidgetColor,fillColor,quickReplyFontColor;
    private int dp1;

    public AgentQuickOptionsTemplateAdapter(Context context, RecyclerView parentRecyclerView) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
        layoutInflater = LayoutInflater.from(context);
        quickWidgetColor = Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor);
        fillColor = Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor);
        quickReplyFontColor = Color.parseColor("#000000");
        dp1= (int) AppControl.getInstance(context).getDimensionUtil().dp1;
    }

    @NonNull
    @Override
    public QuickReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.quick_replies_item_findly_cell, null);
        QuickReplyViewHolder viewHolder = new QuickReplyViewHolder(convertView);
        viewHolder.getQuickReplyTitle().setTextColor(quickReplyFontColor);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuickReplyViewHolder holder, int position) {
        BotButtonModel quickReplyTemplate = quickReplyTemplateArrayList.get(position);
        holder.getQuickReplyImage().setVisibility(View.GONE);
        holder.getQuickReplyTitle().setText(quickReplyTemplate.getName());

        holder.getQuickReplyRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =  parentRecyclerView.getChildPosition(v);
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    BotButtonModel quickReplyTemplate = quickReplyTemplateArrayList.get(position);
                    if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(quickReplyTemplate.getType())) {
                        String quickReplyTitle = quickReplyTemplate.getName();
                        String quickReplyPayload = quickReplyTemplate.getBotOptionPostBackModel().getValue();
                        composeFooterInterface.onSendClick(quickReplyTitle, quickReplyPayload,false);
                    } else if(BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(quickReplyTemplate.getType())){
                        invokeGenericWebViewInterface.invokeGenericWebView(BundleConstants.BUTTON_TYPE_USER_INTENT);
                    }else if(BundleConstants.BUTTON_TYPE_TEXT.equalsIgnoreCase(quickReplyTemplate.getType())){
                        composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(),quickReplyTemplate.getPayload(),false);
                    }else if(BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(quickReplyTemplate.getType())){
                        invokeGenericWebViewInterface.invokeGenericWebView(quickReplyTemplate.getPayload());
                    }else{
                        String quickReplyTitle = quickReplyTemplate.getName();
                        String quickReplyPayload = quickReplyTemplate.getBotOptionPostBackModel().getValue();
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

    public void setAgentQuickReplyTemplateArrayList(ArrayList<BotButtonModel> quickReplyTemplateArrayList) {
        this.quickReplyTemplateArrayList = quickReplyTemplateArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
