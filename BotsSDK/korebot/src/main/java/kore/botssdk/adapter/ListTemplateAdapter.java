package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListElementButton;
import kore.botssdk.models.BotListModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class ListTemplateAdapter extends RecyclerView.Adapter<ListTemplateAdapter.ViewHolder> {
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final boolean isEnabled;
    private final RoundedCornersTransform roundedCornersTransform = new RoundedCornersTransform();
    private final List<BotListModel> botListModels;

    public ListTemplateAdapter(List<BotListModel> botListModels, boolean isEnabled) {
        this.botListModels = botListModels;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_list_template_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotListModel botListModel = getItem(position);
        if (botListModel == null) return;
        if (!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        GradientDrawable gradientDrawable = (GradientDrawable) holder.botListItemRoot.getBackground();
        gradientDrawable.setStroke((int) (1*dp1), ColorStateList.valueOf(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor)));

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        if (!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getSubtitle());
        }
        if (botListModel.getButtons() == null || botListModel.getButtons().isEmpty()) {
            holder.botListItemButton.setVisibility(View.GONE);
        } else {
            holder.botListItemButton.setVisibility(View.VISIBLE);
            holder.botListItemButton.setText(botListModel.getButtons().get(0).getTitle());
            holder.botListItemButton.setTag(botListModel.getButtons().get(0));

            holder.botListItemButton.setOnClickListener(v -> {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    BotListElementButton botListElementButton = (BotListElementButton) v.getTag();
                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListElementButton.getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(botListElementButton.getUrl());
                    } else if (isEnabled && BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botListElementButton.getType())) {
                        String listElementButtonPayload = botListElementButton.getPayload();
                        String listElementButtonTitle = botListElementButton.getTitle();
                        composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload, false);
                    }
                }
            });
        }

        if (botListModel.getDefault_action() != null) {
            if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListModel.getDefault_action().getType())) {
                holder.botListAction.setVisibility(View.VISIBLE);
                holder.botListAction.setText(botListModel.getDefault_action().getUrl());
                holder.botListAction.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyTextColor));
            }
        }

        holder.botListItemRoot.setOnClickListener(v -> {
            if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                BotListModel _botListModel = getItem(position);
                if (_botListModel != null && _botListModel.getDefault_action() != null) {
                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(_botListModel.getDefault_action().getUrl());
                    } else if (isEnabled && BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {
                        composeFooterInterface.onSendClick(_botListModel.getDefault_action().getPayload(), false);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return botListModels != null ? botListModels.size() : 0;
    }

    private BotListModel getItem(int position) {
        return botListModels != null ? botListModels.get(position) : null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle;
        TextView botListItemButton;
        TextView botListAction;

        public ViewHolder(@NonNull View view) {
            super(view);
            botListItemRoot = view.findViewById(R.id.bot_list_item_root);
            botListItemImage = view.findViewById(R.id.bot_list_item_image);
            botListItemTitle = view.findViewById(R.id.bot_list_item_title);
            botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);
            botListItemButton = view.findViewById(R.id.bot_list_item_button);
            botListAction = view.findViewById(R.id.bot_list_action);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
