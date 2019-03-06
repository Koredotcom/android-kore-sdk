
package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
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
import kore.botssdk.view.viewUtils.KaRoundedCornersTransform;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class KnowledgeRecyclerAdapter extends RecyclerView.Adapter<KnowledgeRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<KnowledgeDetailModel> knowledgeDetailModels;
    private boolean isExpanded;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private static KaRoundedCornersTransform roundedCornersTransform = new KaRoundedCornersTransform();
    private static int dp1;
    public KnowledgeRecyclerAdapter(ArrayList<KnowledgeDetailModel> knowledgeDetailModels, Context context) {
        this.knowledgeDetailModels = knowledgeDetailModels;
        this.context = context;
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.knowledge_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.knowledgeItemViewBinding.setKnowledge(knowledgeDetailModels.get(position));
        holder.knowledgeItemViewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString(BundleConstants.KNOWLEDGE_ID, knowledgeDetailModels.get(position).getId());
                verticalListViewActionHelper.knowledgeItemClicked(extras);
            }
        });
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return knowledgeDetailModels != null ? (!isExpanded && knowledgeDetailModels.size() > 3 ? 3 : knowledgeDetailModels.size()) : 0;
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

        public ViewHolder(@NonNull KnowledgeItemViewBinding itemView) {
            super(itemView.getRoot());
            this.knowledgeItemViewBinding = itemView;
        }
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView imageView, String src){
        if(!StringUtils.isNullOrEmpty(src)) {
            Picasso.with(imageView.getContext()).load(src).transform(roundedCornersTransform)
                    .resize(imageView.getWidth()>0?imageView.getWidth():(40*dp1),imageView.getHeight()>0?imageView.getWidth():(40*dp1)).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    imageView.setVisibility(View.GONE);
                }
            });
        }else{
            imageView.setVisibility(View.GONE);
        }
    }

}
