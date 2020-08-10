package kore.botssdk.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotListModel;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class ListViewMoreAdapter extends RecyclerView.Adapter<ListViewMoreAdapter.ViewHolder>{
    private ArrayList<BotListModel> model;
    RoundedCornersTransform roundedCornersTransform;

    // RecyclerView recyclerView;
    public ListViewMoreAdapter(ArrayList<BotListModel> model) {
        this.model = model;
        this.roundedCornersTransform = new RoundedCornersTransform();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bot_listview_template_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BotListModel botListModel = model.get(position);
        holder.botListItemImage.setVisibility(View.GONE);

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
        }
    }


    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle, bot_list_item_cost;

        public ViewHolder(View itemView) {
            super(itemView);
            this.botListItemRoot = (LinearLayout) itemView.findViewById(R.id.bot_list_item_root);
            this.botListItemImage = (ImageView) itemView.findViewById(R.id.bot_list_item_image);
            this.botListItemTitle = (TextView) itemView.findViewById(R.id.bot_list_item_title);
            this.botListItemSubtitle = (TextView) itemView.findViewById(R.id.bot_list_item_subtitle);
            this.bot_list_item_cost = (TextView) itemView.findViewById(R.id.bot_list_item_cost);
        }
    }
}
