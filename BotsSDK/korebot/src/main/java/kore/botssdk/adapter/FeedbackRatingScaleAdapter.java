package kore.botssdk.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.FeedbackRatingModel;
import kore.botssdk.views.CustomTextView;

public class FeedbackRatingScaleAdapter extends RecyclerView.Adapter<FeedbackRatingScaleAdapter.ViewHolder> {
    private final List<FeedbackRatingModel> items;
    private final boolean isEnabled;
    private int selectedPos = -1;
    private ComposeFooterInterface composeFooterInterface;
    private ChatContentStateListener listener;
    private final String msgId;

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setListener(ChatContentStateListener listener) {
        this.listener = listener;
    }

    public FeedbackRatingScaleAdapter(String msgId, List<FeedbackRatingModel> items, boolean isEnabled, int selectedPos) {
        this.msgId = msgId;
        this.selectedPos = selectedPos;
        this.items = items;
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.feedback_rating_scale_cell, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedbackRatingModel ratingModel = items.get(position);
        Context context = holder.tvRating.getContext();
        holder.tvRating.setText(String.valueOf(ratingModel.getNumberId()));
        if (selectedPos == position) {
            holder.tvRating.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvRating.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)));
        } else {
            holder.tvRating.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(ratingModel.getColor())));
            holder.tvRating.setTextColor(ContextCompat.getColor(context, R.color.gray_modern));
        }
        holder.tvRating.setOnClickListener(view -> {
            if (!isEnabled) return;
            if (listener != null) listener.onSelect(msgId, position, BotResponse.SELECTED_FEEDBACK);
            if (composeFooterInterface != null) {
                composeFooterInterface.onSendClick(ratingModel.getNumberId() + "", ratingModel.getNumberId() + "", false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRating = itemView.findViewById(R.id.tvRatingScale);
        }
    }
}
