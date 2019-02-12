
package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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
import kore.botssdk.models.KnowledgeDetailModel;

/**
 * Created by Shiva Krishna Kongara on 06-feb-19.
 */

public class KnowledgeRecyclerAdapter extends RecyclerView.Adapter<KnowledgeRecyclerAdapter.ViewHolder> implements RecyclerViewDataAccessor {

    private Context context;
    private ArrayList<KnowledgeDetailModel> knowledgeDetailModels;
    private boolean isExpanded;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

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
    public ArrayList getData() {
        return knowledgeDetailModels;
    }

    @Override
    public void setData(ArrayList data) {
        knowledgeDetailModels = data;

    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @Override
    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        KnowledgeItemViewBinding knowledgeItemViewBinding;

        public ViewHolder(@NonNull KnowledgeItemViewBinding itemView) {
            super(itemView.getRoot());
            this.knowledgeItemViewBinding = itemView;
        }
    }

}
