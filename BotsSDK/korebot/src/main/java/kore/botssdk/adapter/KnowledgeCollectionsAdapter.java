package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewHolder.KnowledgeCollectionViewHolder;

public class KnowledgeCollectionsAdapter extends RecyclerView.Adapter<KnowledgeCollectionViewHolder> implements RecyclerViewDataAccessor {

    Context context;
    ArrayList<KnowledgeCollectionModel> modelData;
    VerticalListViewActionHelper verticalListViewActionHelper;

    public KnowledgeCollectionsAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public KnowledgeCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.us_knowledge_collection_layout, parent, false);
        return new KnowledgeCollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeCollectionViewHolder holder, int position) {

        holder.header.setVisibility(View.GONE);
        KnowledgeCollectionModel model = modelData.get(position);
        holder.title_view.setText(model.getElements().get(0).getQuestion());
        holder.sub_view.setText(model.getElements().get(0).getAnswer());
    }

    @Override
    public int getItemCount() {
        return modelData!=null?modelData.size():0;
    }


    @Override
    public ArrayList getData() {
        return modelData;
    }

    @Override
    public void setData(ArrayList modelData) {
        this.modelData = modelData;
        notifyDataSetChanged();
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }
}
