
package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.databinding.KnowledgeItemViewBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;
import kore.botssdk.view.viewUtils.KaRoundedCornersTransform;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;


/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class KnowledgeRecyclerAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<KnowledgeDetailModel> knowledgeDetailModels;
    private boolean isExpanded;
    private int EMPTY_CARD_FLAG = 0;

    private int DATA_CARD_FLAG = 1;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private static KaRoundedCornersTransform roundedCornersTransform = new KaRoundedCornersTransform();
    private static int dp1;
    public KnowledgeRecyclerAdapter(ArrayList<KnowledgeDetailModel> knowledgeDetailModels, Context context) {
        this.knowledgeDetailModels = knowledgeDetailModels;
        this.context = context;
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DATA_CARD_FLAG) {
            return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.knowledge_item_view, parent, false));
        } else {
            return new EmptyWidgetViewHolder(LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false));


        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderdata, int position) {
        if (holderdata instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holderdata;


            //holder.followers_count.setText(knowledgeDetailModels.get(position).getnFollows()+"");
            holder.knowledgeItemViewBinding.setKnowledge(knowledgeDetailModels.get(position));
            holder.knowledgeItemViewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    extras.putString(BundleConstants.KNOWLEDGE_ID, knowledgeDetailModels.get(position).getId());
                    verticalListViewActionHelper.knowledgeItemClicked(extras);
                }
            });
        } else {

            EmptyWidgetViewHolder holder = (EmptyWidgetViewHolder) holderdata;
            holder.tv_disrcription.setText("No knowledge articles");
            holder.img_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_meeting));


        }

    }


    @Override
    public int getItemViewType(int position) {

        if (knowledgeDetailModels != null && knowledgeDetailModels.size() > 0)
            return DATA_CARD_FLAG;


        return EMPTY_CARD_FLAG;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return knowledgeDetailModels != null &&knowledgeDetailModels.size()>0? (!isExpanded && knowledgeDetailModels.size() > 3 ? 3 : knowledgeDetailModels.size()) : 1;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    @Override
    public ArrayList getData() {
        return knowledgeDetailModels;
    }

    @Override
    public void setData(ArrayList data) {
        knowledgeDetailModels = data;

    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        KnowledgeItemViewBinding knowledgeItemViewBinding;
        TextView followers_count;
        public ViewHolder(@NonNull KnowledgeItemViewBinding itemView) {
            super(itemView.getRoot());
            this.knowledgeItemViewBinding = itemView;
            followers_count=itemView.followersCount;
        }
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView imageView, String src){
        //.resize(imageView.getWidth()>0?imageView.getWidth():(40*dp1),imageView.getHeight()>0?imageView.getWidth():(40*dp1))
        if(!StringUtils.isNullOrEmpty(src)) {
            Picasso.with(imageView.getContext()).load(src).transform(roundedCornersTransform)
                    .into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    imageView.setVisibility(View.GONE);
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

}
