package kore.botssdk.adapter;

import static android.content.Context.MODE_PRIVATE;
import static kore.botssdk.models.BotResponsePayLoadText.THEME_NAME;

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
import kore.botssdk.viewUtils.RoundedCornersTransform;

public class ListViewTemplateAdapter extends RecyclerView.Adapter<ListViewTemplateAdapter.ViewHolder> {
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private final boolean isEnabled;
    private final SharedPreferences sharedPreferences;
    private final RoundedCornersTransform roundedCornersTransform = new RoundedCornersTransform();
    private final List<BotListModel> botListModels;
    private final int size;

    public ListViewTemplateAdapter(Context context, List<BotListModel> botListModels, boolean isEnabled, int size) {
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, MODE_PRIVATE);
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

        if (!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        holder.botListItemCost.setText(botListModel.getValue());

        if (botListModel.getColor() != null) holder.botListItemCost.setTextColor(Color.parseColor(botListModel.getColor()));

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

            SharedPreferences pref = botListItemRoot.getContext().getSharedPreferences(THEME_NAME, MODE_PRIVATE);
            GradientDrawable drawable = (GradientDrawable) botListItemRoot.getBackground().mutate();
            drawable.setStroke(2, Color.parseColor(pref.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff")));
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}