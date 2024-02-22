package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.view.viewHolder.QuickReplyViewHolder;

public class AgentQuickOptionsTemplateAdapter extends RecyclerView.Adapter<QuickReplyViewHolder> {
    ArrayList<BotButtonModel> quickReplyTemplateArrayList;
    final Context context;
    final RecyclerView parentRecyclerView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final int quickReplyFontColor;

    public AgentQuickOptionsTemplateAdapter(@NonNull Context context, @NonNull RecyclerView parentRecyclerView) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
        quickReplyFontColor = Color.parseColor("#000000");
    }

    @NonNull
    @Override
    public QuickReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.quick_replies_item_cell, null);
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
                int position =  parentRecyclerView.getChildAdapterPosition(v);
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

    public void setAgentQuickReplyTemplateArrayList(@NonNull ArrayList<BotButtonModel> quickReplyTemplateArrayList) {
        this.quickReplyTemplateArrayList = quickReplyTemplateArrayList;
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
