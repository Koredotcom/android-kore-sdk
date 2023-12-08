package kore.botssdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BrandingQuickStartButtonActionModel;
import kore.botssdk.models.BrandingQuickStartButtonButtonsModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;

public class WelcomeStaticLinkListAdapter extends RecyclerView.Adapter<WelcomeStaticLinkListAdapter.StaticViewHolder> {
    private ArrayList<BrandingQuickStartButtonButtonsModel> quickReplyTemplateArrayList;
    final Context context;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public WelcomeStaticLinkListAdapter(Context context, RecyclerView parentRecyclerView) {
        this.context = context;
    }

    @NonNull
    @Override
    public StaticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.welcome_static_links, null);
        return new StaticViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaticViewHolder staticViewHolder, int position) {
        BrandingQuickStartButtonButtonsModel quickReplyTemplate = quickReplyTemplateArrayList.get(position);
        staticViewHolder.link_title.setText(quickReplyTemplate.getTitle());
        staticViewHolder.link_desc.setText(quickReplyTemplate.getDescription());

        staticViewHolder.quick_reply_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(composeFooterInterface != null && invokeGenericWebViewInterface != null)
                {
                    String quickReplyPayload;
                    try
                    {
                        BrandingQuickStartButtonActionModel buttonActionModel = quickReplyTemplate.getAction();
                        if(!StringUtils.isNullOrEmpty(buttonActionModel.getValue()))
                        {
                            quickReplyPayload = buttonActionModel.getValue();

                            if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(buttonActionModel.getType())) {
                                composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(), quickReplyPayload,false);
                            } else if(BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(buttonActionModel.getType())){
                                invokeGenericWebViewInterface.invokeGenericWebView(BundleConstants.BUTTON_TYPE_USER_INTENT);
                            }else if(BundleConstants.BUTTON_TYPE_TEXT.equalsIgnoreCase(buttonActionModel.getType())){
                                composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(),quickReplyPayload,false);
                            }else if(BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(buttonActionModel.getType())
                                    || BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(buttonActionModel.getType())){
                                invokeGenericWebViewInterface.invokeGenericWebView(quickReplyPayload);
                            }else{
                                composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(), quickReplyPayload,false);
                            }
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
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

    public void setWelcomeStaticLinksArrayList(ArrayList<BrandingQuickStartButtonButtonsModel> quickReplyTemplateArrayList) {
        this.quickReplyTemplateArrayList = quickReplyTemplateArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public static class StaticViewHolder extends RecyclerView.ViewHolder {
        final TextView link_title;
        final TextView link_desc;
        RelativeLayout quick_reply_view;

        public StaticViewHolder(@NonNull View itemView) {
            super(itemView);
            link_title = itemView.findViewById(R.id.link_title);
            link_desc = itemView.findViewById(R.id.link_desc);
            quick_reply_view = itemView.findViewById(R.id.quick_reply_view);
        }
    }
}
