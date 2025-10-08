package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.QuickRepliesPayloadModel;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.viewholders.QuickReplyViewHolder;

public class QuickRepliesTemplateAdapter extends RecyclerView.Adapter<QuickReplyViewHolder> {

    private ArrayList<QuickReplyTemplate> quickReplyTemplateArrayList;
    final Context context;
    private final RecyclerView parentRecyclerView;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final int quickReplyFontColor;
    private final boolean isEnabled;

    public QuickRepliesTemplateAdapter(Context context, RecyclerView parentRecyclerView, boolean isEnabled) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
        quickReplyFontColor = Color.parseColor("#3F51B5");
        this.isEnabled = isEnabled;
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
        QuickReplyTemplate quickReplyTemplate = quickReplyTemplateArrayList.get(position);

        if (quickReplyTemplate.getImage_url() != null && !quickReplyTemplate.getImage_url().isEmpty()) {
            Picasso.get().load(quickReplyTemplate.getImage_url()).into(holder.getQuickReplyImage());
            holder.getQuickReplyImage().setVisibility(View.VISIBLE);
        } else {
            holder.getQuickReplyImage().setVisibility(View.GONE);
        }

        holder.getQuickReplyTitle().setText(quickReplyTemplate.getTitle());

        holder.getQuickReplyRoot().setOnClickListener(v -> {
            if (!isEnabled) return;
            int position1 = parentRecyclerView.getChildAdapterPosition(v);
            QuickReplyTemplate quickReplyTemplate1 = quickReplyTemplateArrayList.get(position1);

            String quickReplyPayload;
            try {
                quickReplyPayload = (String) quickReplyTemplate1.getPayload();
            } catch (Exception e) {
                try {
                    QuickRepliesPayloadModel quickRepliesPayloadModel = (QuickRepliesPayloadModel) quickReplyTemplate1.getPayload();
                    quickReplyPayload = quickRepliesPayloadModel.getName();
                } catch (Exception exception) {
                    quickReplyPayload = "";
                }
            }

            if (composeFooterInterface != null && BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(quickReplyTemplate1.getContent_type())) {
                composeFooterInterface.onSendClick(quickReplyTemplate1.getTitle(), quickReplyPayload, false);
            } else if (invokeGenericWebViewInterface != null && BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(quickReplyTemplate1.getContent_type())) {
                invokeGenericWebViewInterface.invokeGenericWebView(BundleConstants.BUTTON_TYPE_USER_INTENT);
            } else if (composeFooterInterface != null && BundleConstants.BUTTON_TYPE_TEXT.equalsIgnoreCase(quickReplyTemplate1.getContent_type())) {
                composeFooterInterface.onSendClick(quickReplyTemplate1.getTitle(), quickReplyPayload, false);
            } else if (invokeGenericWebViewInterface != null && BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(quickReplyTemplate1.getContent_type())) {
                invokeGenericWebViewInterface.invokeGenericWebView(quickReplyPayload);
            } else if (composeFooterInterface != null) {
                composeFooterInterface.onSendClick(quickReplyTemplate1.getTitle(), quickReplyPayload, false);
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

