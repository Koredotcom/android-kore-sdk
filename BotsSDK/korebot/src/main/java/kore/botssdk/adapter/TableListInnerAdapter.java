package kore.botssdk.adapter;

import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.graphics.Color;
import android.graphics.Typeface;
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
import kore.botssdk.models.BotTableListRowItemsModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewUtils.RoundedCornersTransform;

public class TableListInnerAdapter extends RecyclerView.Adapter<TableListInnerAdapter.ViewHolder> {
    private final List<BotTableListRowItemsModel> botTableListModels;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    private final RoundedCornersTransform roundedCornersTransform = new RoundedCornersTransform();

    public TableListInnerAdapter(List<BotTableListRowItemsModel> botTableListModels, boolean isEnabled) {
        this.botTableListModels = botTableListModels;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_table_list_inner_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotTableListRowItemsModel botListModel = getItem(position);
        if (botListModel == null) return;

        holder.botListItemImage.setVisibility(View.GONE);

        if (botListModel.getTitle() != null && botListModel.getTitle().getImage() != null) {
            if (!StringUtils.isNullOrEmpty(botListModel.getTitle().getImage().getImage_src())) {
                holder.botListItemImage.setVisibility(View.VISIBLE);
                Picasso.get().load(botListModel.getTitle().getImage().getImage_src()).transform(roundedCornersTransform).into(holder.botListItemImage);

                if (botListModel.getTitle().getImage().getRadius() > 0) {
                    holder.botListItemImage.getLayoutParams().height = (int) (botListModel.getTitle().getImage().getRadius() * 2 * dp1);
                    holder.botListItemImage.getLayoutParams().width = (int) (botListModel.getTitle().getImage().getRadius() * 2 * dp1);
                }
            }
        }

        holder.botListItemTitle.setTag(botListModel);

        if (botListModel.getTitle().getText() != null) {
            holder.botListItemTitle.setText(botListModel.getTitle().getText().getTitle());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);

            if (!StringUtils.isNullOrEmpty(botListModel.getTitle().getText().getSubtitle())) {
                holder.botListItemSubtitle.setVisibility(View.VISIBLE);
                holder.botListItemSubtitle.setText(botListModel.getTitle().getText().getSubtitle());
            }
        } else if (botListModel.getTitle().getUrl() != null) {
            holder.botListItemTitle.setText(botListModel.getTitle().getUrl().getTitle());
            holder.botListItemTitle.setTypeface(null, Typeface.BOLD);

            if (!StringUtils.isNullOrEmpty(botListModel.getTitle().getUrl().getSubtitle())) {
                holder.botListItemSubtitle.setVisibility(View.VISIBLE);
                holder.botListItemSubtitle.setText(botListModel.getTitle().getUrl().getSubtitle());
            }
        }

        holder.botListItemCost.setTextColor(ResourcesCompat.getColor(holder.itemView.getContext().getResources(), R.color.black, holder.itemView.getContext().getTheme()));

        if (!StringUtils.isNullOrEmpty(botListModel.getTitle().getRowColor())) {
            holder.botListItemTitle.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
            holder.botListItemSubtitle.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
            holder.botListItemCost.setTextColor(Color.parseColor(botListModel.getTitle().getRowColor()));
        }

        if (botListModel.getValue().getType().equalsIgnoreCase("text")) {
            if (!StringUtils.isNullOrEmpty(botListModel.getValue().getText())) {
                holder.botListItemCost.setVisibility(View.VISIBLE);
                holder.botListItemCost.setText(botListModel.getValue().getText());

                if (!StringUtils.isNullOrEmpty(botListModel.getValue().getLayout().getColor())) {
                    holder.botListItemCost.setTextColor(Color.parseColor(botListModel.getValue().getLayout().getColor()));
                }
            }
        } else if (botListModel.getValue().getType().equalsIgnoreCase("url")) {
            if (!StringUtils.isNullOrEmpty(botListModel.getValue().getUrl().getTitle())) {
                holder.botListItemCost.setVisibility(View.VISIBLE);
                holder.botListItemCost.setText(botListModel.getValue().getUrl().getTitle());
            }
        }

        holder.botListItemRoot.setBackgroundColor(0);

        if (!StringUtils.isNullOrEmpty(botListModel.getBgcolor())) {
            holder.botListItemRoot.setBackgroundColor(Color.parseColor(botListModel.getBgcolor()));
        }

        holder.itemView.setOnClickListener(view -> {
            if (botListModel.getDefault_action() != null && botListModel.getDefault_action().getType() != null &&
                    botListModel.getDefault_action().getType().equalsIgnoreCase("postback")) {
                if (isEnabled && composeFooterInterface != null && !StringUtils.isNullOrEmpty(botListModel.getDefault_action().getPayload())) {
                    composeFooterInterface.onSendClick(botListModel.getDefault_action().getTitle(), botListModel.getDefault_action().getPayload(), false);
                }
            } else if (botListModel.getDefault_action() != null && botListModel.getDefault_action().getType() != null &&
                    botListModel.getDefault_action().getType().equalsIgnoreCase("url")) {
                if (invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botListModel.getDefault_action().getUrl())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(botListModel.getDefault_action().getUrl());
                }
            } else if (botListModel.getDefault_action() == null && botListModel.getTitle() != null
                    && botListModel.getTitle().getUrl() != null) {
                if (invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(botListModel.getTitle().getUrl().getLink())) {
                    invokeGenericWebViewInterface.invokeGenericWebView(botListModel.getTitle().getUrl().getLink());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return botTableListModels != null ? botTableListModels.size() : 0;
    }

    private BotTableListRowItemsModel getItem(int position) {
        return botTableListModels != null ? botTableListModels.get(position) : null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle;
        TextView botListItemCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            botListItemRoot = itemView.findViewById(R.id.bot_list_item_root);
            botListItemImage = itemView.findViewById(R.id.bot_list_item_image);
            botListItemTitle = itemView.findViewById(R.id.bot_list_item_title);
            botListItemSubtitle = itemView.findViewById(R.id.bot_list_item_subtitle);
            botListItemCost = itemView.findViewById(R.id.bot_list_item_cost);
        }
    }

    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
