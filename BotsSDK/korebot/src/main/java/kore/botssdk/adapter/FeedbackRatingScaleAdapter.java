package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.FeedbackRatingModel;

public class FeedbackRatingScaleAdapter extends RecyclerView.Adapter<FeedbackRatingScaleAdapter.ViewHolder> {
    private final List<FeedbackRatingModel> items;
    private final boolean isEnabled;
    private int selectedPos = -1;
    private ComposeFooterInterface composeFooterInterface;
    private ChatContentStateListener listener;
    private final String msgId;
    private String highlightedColor, highlightedTextColor;

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setListener(ChatContentStateListener listener) {
        this.listener = listener;
    }

    public FeedbackRatingScaleAdapter(Context context, String msgId, List<FeedbackRatingModel> items, boolean isEnabled, int selectedPos) {
        this.msgId = msgId;
        this.selectedPos = selectedPos;
        this.items = items;
        this.isEnabled = isEnabled;

        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        highlightedColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.primary));
        highlightedTextColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.white));

        highlightedColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, highlightedColor);
        highlightedTextColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, highlightedTextColor);
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
            holder.tvRating.setTextColor(Color.parseColor(highlightedTextColor));
            holder.tvRating.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(highlightedColor)));
        } else {
            int colorWithAlpha = ColorUtils.setAlphaComponent(Color.parseColor(ratingModel.getColor()), 128);
            holder.tvRating.setBackgroundTintList(ColorStateList.valueOf(colorWithAlpha));
            holder.tvRating.setTextColor(ContextCompat.getColor(context, R.color.gray_modern));
        }
        holder.tvRating.setOnClickListener(view -> {
            if (!isEnabled) return;
            if (listener != null) listener.onSaveState(msgId, position, BotResponse.SELECTED_FEEDBACK);
            if (composeFooterInterface != null) {
                composeFooterInterface.onSendClick(ratingModel.getNumberId() + "", ratingModel.getNumberId() + "", false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRating = itemView.findViewById(R.id.tvRatingScale);
        }
    }
}