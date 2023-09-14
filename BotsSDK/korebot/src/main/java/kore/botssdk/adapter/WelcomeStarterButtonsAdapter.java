package kore.botssdk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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
import kore.botssdk.view.viewHolder.QuickReplyViewHolder;

public class WelcomeStarterButtonsAdapter extends RecyclerView.Adapter<QuickReplyViewHolder> {
    private ArrayList<BrandingQuickStartButtonButtonsModel> quickReplyTemplateArrayList;
    final Context context;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public WelcomeStarterButtonsAdapter(Context context, RecyclerView parentRecyclerView) {
        this.context = context;
    }

    @NonNull
    @Override
    public QuickReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.welcome_quick_buttons, null);
        return new QuickReplyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickReplyViewHolder holder, int position) {
        BrandingQuickStartButtonButtonsModel quickReplyTemplate = quickReplyTemplateArrayList.get(position);
        holder.getQuickReplyTitle().setText(quickReplyTemplate.getTitle());

        holder.getQuickReplyRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null)
                {
                    String quickReplyPayload = null;
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
                            }else if(BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(buttonActionModel.getType())){
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

    public void setWelcomeStarterButtonsArrayList(ArrayList<BrandingQuickStartButtonButtonsModel> quickReplyTemplateArrayList) {
        this.quickReplyTemplateArrayList = quickReplyTemplateArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
