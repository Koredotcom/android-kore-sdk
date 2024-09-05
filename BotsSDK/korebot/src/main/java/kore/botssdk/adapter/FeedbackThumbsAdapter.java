package kore.botssdk.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.FeedbackThumbsModel;

public class FeedbackThumbsAdapter extends RecyclerView.Adapter<FeedbackThumbsAdapter.ViewHolder>{
    private static final String[] SELECTED_TEXT_COLORS = {"#ffffff", "#ffffff"};
    private static final String[] UNSELECTED_TEXT_COLORS = {"#cc16A346", "#ccDC2626"};
    private static final String[] SELECTED_BG_COLORS = {"#16A346", "#DC2626"};
    private static final String[] UNSELECTED_BG_COLORS = {"#aa16A346", "#aaDC2626"};
    private static final int[] SRC_IMAGES = {R.drawable.thumbs_up, R.drawable.thumbs_down};
    private final List<FeedbackThumbsModel> items;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private final String msgId;
    private final FeedbackThumbsModel selectedItem;
    private final ChatContentStateListener listener;

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public FeedbackThumbsAdapter(String msgId, List<FeedbackThumbsModel> items, FeedbackThumbsModel selectedItem, boolean isEnabled, ChatContentStateListener listener) {
        this.msgId = msgId;
        this.items = items;
        this.isEnabled = isEnabled;
        this.selectedItem = selectedItem;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_feedback_thumbs, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedbackThumbsModel ratingModel = items.get(position);
        holder.tvFeedbackThumbs.setText(String.valueOf(ratingModel.getReviewText()));
        setTextColor(holder.tvFeedbackThumbs, SELECTED_TEXT_COLORS[position], UNSELECTED_TEXT_COLORS[position]);
        setImageViewSrc(holder.ivFeedbackThumbs, SRC_IMAGES[position], SELECTED_TEXT_COLORS[position], UNSELECTED_TEXT_COLORS[position]);
        setBgDrawable(holder.thumbsFeedbackRoot, SELECTED_BG_COLORS[position], UNSELECTED_BG_COLORS[position]);
        holder.thumbsFeedbackRoot.setSelected(selectedItem != null && selectedItem == ratingModel);
        holder.ivFeedbackThumbs.setSelected(selectedItem != null && selectedItem == ratingModel);
        holder.tvFeedbackThumbs.setSelected(selectedItem != null && selectedItem == ratingModel);

        holder.thumbsFeedbackRoot.setOnClickListener(view -> {
            if (!isEnabled) return;
            if (composeFooterInterface != null) {
                if (listener != null) listener.onSelect(msgId, ratingModel, BotResponse.SELECTED_FEEDBACK);
                composeFooterInterface.onSendClick(ratingModel.getValue(), ratingModel.getValue(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setTextColor(TextView textView, String selectedColor, String unselectedColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{-android.R.attr.state_selected}
        };

        int[] colors = new int[]{
                Color.parseColor(selectedColor),
                Color.parseColor(unselectedColor)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

        textView.setTextColor(colorStateList);
    }

    private void setBgDrawable(View view, String selectedColor, String unselectedColor) {
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.color.selector_feedback_thumbs_color);
        if (drawable != null) {
            drawable.mutate();
        }

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{-android.R.attr.state_selected}
        };

        int[] colors = new int[]{
                Color.parseColor(selectedColor),
                Color.parseColor(unselectedColor)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

        if (drawable != null) {
            drawable.setTintList(colorStateList);
        }

        view.setBackground(drawable);
    }

    private void setImageViewSrc(ImageView imageView, int srcId, String selectedColor, String unselectedColor) {
        Drawable drawable = ContextCompat.getDrawable(imageView.getContext(), srcId);
        if (drawable == null) return;

        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{-android.R.attr.state_selected}
        };

        int[] colors = new int[]{
                Color.parseColor(selectedColor),
                Color.parseColor(unselectedColor)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

        DrawableCompat.setTintList(wrappedDrawable, colorStateList);

        imageView.setImageDrawable(wrappedDrawable);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFeedbackThumbs;
        TextView tvFeedbackThumbs;
        LinearLayoutCompat thumbsFeedbackRoot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFeedbackThumbs = itemView.findViewById(R.id.tvFeedbackThumbs);
            ivFeedbackThumbs = itemView.findViewById(R.id.ivFeedbackThumbs);
            thumbsFeedbackRoot = itemView.findViewById(R.id.thumbs_feedback_root);

            thumbsFeedbackRoot.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 8f);
                }
            });
            thumbsFeedbackRoot.setClipToOutline(true);
        }
    }
}
