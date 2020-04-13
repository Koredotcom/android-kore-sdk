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
import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.utils.Utility;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.TextMediaLayout;
import kore.botssdk.view.viewHolder.KnowledgeCollectionViewHolder;
import kore.botssdk.view.viewUtils.BubbleViewUtil;
import kore.botssdk.view.viewUtils.TextMediaLayoutUniversal;

public class KnowledgeCollectionsAdapter extends RecyclerView.Adapter<KnowledgeCollectionViewHolder> implements RecyclerViewDataAccessor {

    Context context;
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
      /*  try {
            holder.sub_view.setText(MarkdownUtil.processMarkDown(model.getAnswerPayload().get(0).getText()));
        }catch (Exception e)
        {
            holder.sub_view.setText(model.getAnswerPayload().get(0).getText());
        }
*/
        TextMediaLayoutUniversal  textMediaLayout=new TextMediaLayoutUniversal(context,context.getResources().getColor(R.color.black),true);
        textMediaLayout.setRestrictedLayoutWidth(BubbleViewUtil.getBubbleContentWidth());
        textMediaLayout.widthStyle = BubbleConstants.WRAP_CONTENT;
        try {
            holder.linear_view.removeAllViews();
            textMediaLayout.populateText(model.getAnswerPayload().get(0).getText());
            holder.linear_view.addView(textMediaLayout);
        }catch (Exception e)
        {
            holder.linear_view.removeAllViews();
            holder.sub_view.setText(model.getAnswerPayload().get(0).getText());
            holder.linear_view.addView(holder.sub_view);
        }


        holder.peopleicon.setTypeface(Utility.getTypeFaceObj(context));
        holder.search_view.setText(model.getName());
        holder.percent_view.setText(model.getScore()+"% Match");

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


    public void setDataObject(KnowledgeCollectionModel.Elements modelData) {
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
