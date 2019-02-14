
package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.databinding.KnowledgeItemViewBinding;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.utils.BundleConstants;

/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class KnowledgeRecyclerAdapter extends RecyclerView.Adapter<KnowledgeRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<KnowledgeDetailModel> knowledgeDetailModels;
    private boolean isExpanded;
    private VerticalListViewActionHelper verticalListViewActionHelper;

    public KnowledgeRecyclerAdapter(ArrayList<KnowledgeDetailModel> knowledgeDetailModels, Context context) {
        this.knowledgeDetailModels = knowledgeDetailModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.knowledge_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.knowledgeItemViewBinding.setKnowledge(knowledgeDetailModels.get(position));
        try {
            holder.knowledgeItemViewBinding.profileView.setColor(Color.parseColor(knowledgeDetailModels.get(position).getOwner().getColor()));
        } catch (Exception e) {
            holder.knowledgeItemViewBinding.profileView.setColor(context.getResources().getColor(R.color.splash_color));
        }
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

}
