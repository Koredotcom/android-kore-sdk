package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    final Context context;
    KnowledgeCollectionModel.Elements modelData;
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
        KnowledgeCollectionModel.DataElements model = modelData.getCombinedData().get(position);
        holder.view_suggest.setVisibility(model.isSuggestive()?View.VISIBLE:View.GONE);
        holder.title_view.setText(model.getQuestion());
        holder.sub_view.setText(model.getAnswerPayload().get(0).getText());
        holder.peopleicon.setTypeface(Utility.getTypeFaceObj(context));
        holder.search_view.setText(model.getName());
        String str = model.getScore()+"% Match";
        holder.percent_view.setText(str);

        holder.view_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verticalListViewActionHelper.knowledgeCollectionItemClick(model,"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelData!=null&&modelData.getCombinedData()!=null?modelData.getCombinedData().size():0;
    }


    @Override
    public ArrayList getData() {
        return null;
    }

    @Override
    public void setData(ArrayList data) {

    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }
}
