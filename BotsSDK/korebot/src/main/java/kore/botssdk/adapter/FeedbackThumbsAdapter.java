package kore.botssdk.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.FeedbackThumbsModel;
import kore.botssdk.utils.BundleConstants;

public class FeedbackThumbsAdapter extends RecyclerView.Adapter<FeedbackThumbsAdapter.ViewHolder>{
    private final List<FeedbackThumbsModel> items;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private final String msgId;
    private Context context;

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public FeedbackThumbsAdapter(Context context, String msgId, ArrayList<FeedbackThumbsModel> items, boolean isEnabled) {
        this.msgId = msgId;
        this.items = items;
        this.isEnabled = isEnabled;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedbackThumbsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.feedback_thumbs_layout, parent, false);
        return new FeedbackThumbsAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackThumbsAdapter.ViewHolder holder, int position) {
        FeedbackThumbsModel ratingModel = items.get(position);
        holder.tvFeedbackThumbs.setText(String.valueOf(ratingModel.getReviewText()));

        if(!ratingModel.getReviewText().equalsIgnoreCase(BundleConstants.EXTREMELY_LIKELY))
        {
            GradientDrawable gradientDrawable = (GradientDrawable) holder.thumbs_feedback_root.getBackground();
            gradientDrawable.setColor(ContextCompat.getColor(context, R.color.color_ffeaec));
            holder.tvFeedbackThumbs.setTextColor(ContextCompat.getColor(context, R.color.color_fc4a61));
            holder.ivFeedbackThumbs.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.thumbs_down, context.getTheme()));
            holder.ivFeedbackThumbs.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_fc4a61)));
        }

        holder.thumbs_feedback_root.setOnClickListener(view -> {
            if (!isEnabled) return;
            if (composeFooterInterface != null) {
                composeFooterInterface.onSendClick(ratingModel.getValue(), ratingModel.getValue(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFeedbackThumbs;
        TextView tvFeedbackThumbs;
        LinearLayoutCompat thumbs_feedback_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFeedbackThumbs = itemView.findViewById(R.id.tvFeedbackThumbs);
            ivFeedbackThumbs = itemView.findViewById(R.id.ivFeedbackThumbs);
            thumbs_feedback_root = itemView.findViewById(R.id.thumbs_feedback_root);
        }
    }
}
