package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.content.SharedPreferences;
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
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class ListViewTemplateAdapter extends RecyclerView.Adapter<ListViewTemplateAdapter.ViewHolder> {
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final boolean isEnabled;
    private final SharedPreferences sharedPreferences;
    private final RoundedCornersTransform roundedCornersTransform = new RoundedCornersTransform();
    private final List<BotListModel> botListModels;
    private final int size;

    public ListViewTemplateAdapter(Context context, List<BotListModel> botListModels, boolean isEnabled, int size) {
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
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

        if (sharedPreferences != null) {
            GradientDrawable rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(holder.itemView.getContext().getResources(), R.drawable.rounded_rect_feedback, holder.itemView.getContext().getTheme());
            if (rightDrawable != null) {
                rightDrawable.setColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#ffffff")));
                String themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1);
                if (themeName.equalsIgnoreCase(BotResponse.THEME_NAME_1)) {
                    rightDrawable.setStroke((int) (1 * dp1), Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#ffffff")));
                    holder.botListItemRoot.setBackground(rightDrawable);
                } else {
                    rightDrawable.setStroke((int) (2 * dp1), Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, "#ffffff")));
                    holder.botListItemRoot.setBackground(rightDrawable);
                }
            }

            holder.botListItemTitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#505968")));
        }

        if (!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

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

            if (sharedPreferences != null)
                holder.botListItemSubtitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#505968")));
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
