package kore.botssdk.adapter;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.ListClickListner;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.FeedbackNumberModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;
@SuppressLint("UnknownNullness")
public class FeedbackRatingScaleAdapter extends RecyclerView.Adapter<FeedbackRatingScaleAdapter.ViewHolder>{
    private final ArrayList<FeedbackNumberModel> model;
    final RoundedCornersTransform roundedCornersTransform;
    ComposeFooterInterface composeFooterInterface;
    ListClickListner listClickListner;
    int selectedPosition = -1;
    Context context;

    public FeedbackRatingScaleAdapter(Context context, ArrayList<FeedbackNumberModel> model, ComposeFooterInterface composeFooterInterface, ListClickListner listClickListner, int selectedPos) {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.composeFooterInterface = composeFooterInterface;
        this.listClickListner = listClickListner;
        this.selectedPosition = selectedPos;
    }

    @NonNull
    @Override
    public FeedbackRatingScaleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.feedback_scale_cell, parent, false);

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FeedbackRatingScaleAdapter.ViewHolder holder, int position) {
        FeedbackNumberModel botListModel = model.get(position);
        holder.tvRating.setText(String.valueOf(botListModel.getNumberId()));

        holder.tvRating.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(botListModel.getColor())));
        holder.tvRating.setTextColor(ContextCompat.getColor(context, R.color.gray_modern));

        if(selectedPosition == position)
        {
            holder.tvRating.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvRating.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)));
        }


        holder.tvRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(selectedPosition == -1)
                {
                    listClickListner.listItemClicked(holder.getBindingAdapterPosition());
                    composeFooterInterface.onSendClick(String.valueOf(botListModel.getNumberId()), true);
                }
            }
        });
    }

    public void setEnabled(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyItemChanged(selectedPosition);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvRating;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tvRating = itemView.findViewById(R.id.tvRatingScale);
        }
    }
}
