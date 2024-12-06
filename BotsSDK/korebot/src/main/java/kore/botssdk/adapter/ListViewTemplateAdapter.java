package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class ListViewTemplateAdapter extends RecyclerView.Adapter<ListViewTemplateAdapter.ViewHolder> {
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final boolean isEnabled;
    private final RoundedCornersTransform roundedCornersTransform = new RoundedCornersTransform();
    private final List<BotListModel> botListModels;
    private final int size;

    public ListViewTemplateAdapter(List<BotListModel> botListModels, boolean isEnabled, int size) {
        this.botListModels = botListModels;
        this.isEnabled = isEnabled;
        this.size = size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_listview_template_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotListModel botListModel = getItem(position);
        if (botListModel == null) return;
        holder.botListItemImage.setVisibility(View.GONE);

        GradientDrawable rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(holder.botListItemRoot.getContext().getResources(), R.drawable.multi_select_list_bg, holder.botListItemRoot.getContext().getTheme());
        if (rightDrawable != null)
            rightDrawable.setStroke((int) (1 * dp1), Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
        holder.botListItemRoot.setBackground(rightDrawable);


        if (!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        } else holder.botListItemImage.setVisibility(View.INVISIBLE);

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        holder.botListItemCost.setText(botListModel.getValue());

        if (botListModel.getColor() != null)
            holder.botListItemCost.setTextColor(Color.parseColor(botListModel.getColor()));

        holder.botListItemCost.setTypeface(null, Typeface.BOLD);

        if (!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getSubtitle());
        }

        holder.botListItemRoot.setOnClickListener(v -> {
            if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                BotListModel _botListModel = getItem(position);
                if (_botListModel != null && _botListModel.getDefault_action() != null) {
                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(_botListModel.getDefault_action().getUrl());
                    } else if (isEnabled && BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getDefault_action().getType())) {
                        if (!StringUtils.isNullOrEmpty(_botListModel.getDefault_action().getPayload()))
                            composeFooterInterface.onSendClick(_botListModel.getDefault_action().getPayload(), false);
                        else if (!StringUtils.isNullOrEmpty(_botListModel.getDefault_action().getTitle()))
                            composeFooterInterface.onSendClick(_botListModel.getDefault_action().getTitle(), false);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    private BotListModel getItem(int position) {
        return botListModels != null ? botListModels.get(position) : null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, botListItemCost;

        public ViewHolder(@NonNull View view) {
            super(view);
            botListItemRoot = view.findViewById(R.id.bot_list_item_root);
            botListItemImage = view.findViewById(R.id.bot_list_item_image);
            botListItemTitle = view.findViewById(R.id.bot_list_item_title);
            botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);
            botListItemCost = view.findViewById(R.id.bot_list_item_cost);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
