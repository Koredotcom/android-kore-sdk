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

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class ListViewMoreAdapter extends RecyclerView.Adapter<ListViewMoreAdapter.ViewHolder>{
    private final ArrayList<BotListModel> model;
    final RoundedCornersTransform roundedCornersTransform;
    private SharedPreferences sharedPreferences;
    private GradientDrawable rightDrawable;

    public ListViewMoreAdapter(@NonNull ArrayList<BotListModel> model) {
        this.model = model;
        this.roundedCornersTransform = new RoundedCornersTransform();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bot_listview_template_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        sharedPreferences = parent.getContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(parent.getContext().getResources(), R.drawable.rounded_rect_feedback, parent.getContext().getTheme());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotListModel botListModel = model.get(position);
        holder.botListItemImage.setVisibility(View.GONE);

        if(rightDrawable != null && sharedPreferences != null)
        {
            rightDrawable.setColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, "#ffffff")));
            rightDrawable.setStroke((int) (1*dp1), Color.parseColor(sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, SDKConfiguration.BubbleColors.rightBubbleUnSelected)));
            holder.botListItemRoot.setBackground(rightDrawable);

            holder.botListItemTitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#505968")));
        }

        if(!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        holder.bot_list_item_cost.setText(botListModel.getValue());

        if(botListModel.getColor() != null)
            holder.bot_list_item_cost.setTextColor(Color.parseColor(botListModel.getColor()));

        holder.bot_list_item_cost.setTypeface(null, Typeface.BOLD);

        if(!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getSubtitle());

            if(sharedPreferences != null)
                holder.botListItemSubtitle.setTextColor(Color.parseColor(sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#505968")));
        }
    }


    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout botListItemRoot;
        final ImageView botListItemImage;
        final TextView botListItemTitle;
        final TextView botListItemSubtitle;
        final TextView bot_list_item_cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.botListItemRoot = itemView.findViewById(R.id.bot_list_item_root);
            this.botListItemImage = itemView.findViewById(R.id.bot_list_item_image);
            this.botListItemTitle = itemView.findViewById(R.id.bot_list_item_title);
            this.botListItemSubtitle = itemView.findViewById(R.id.bot_list_item_subtitle);
            this.bot_list_item_cost = itemView.findViewById(R.id.bot_list_item_cost);
        }
    }
}
