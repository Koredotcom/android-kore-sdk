package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.utils.Utility;

public class KnowledgeCollectionsAdapter extends RecyclerView.Adapter<KnowledgeCollectionsAdapter.KnowledgeCollectionViewHolder> {

    Context context;

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class KnowledgeCollectionViewHolder extends RecyclerView.ViewHolder {

        TextView icon_view,staricon,peopleicon, root_title_view, count_view, star_textview, title_view, sub_view, search_view, percent_view;


        public KnowledgeCollectionViewHolder(@NonNull View itemView) {
            super(itemView);

            icon_view=itemView.findViewById(R.id.icon_view);
            icon_view.setTypeface(Utility.getTypeFaceObj(context));
            icon_view.setBackground(Utility.changeColorOfDrawable(context, Color.parseColor("#f98140")));


            staricon=itemView.findViewById(R.id.staricon);
            staricon.setTypeface(Utility.getTypeFaceObj(context));

            peopleicon=itemView.findViewById(R.id.peopleicon);
            peopleicon.setTypeface(Utility.getTypeFaceObj(context));


            root_title_view=itemView.findViewById(R.id.root_title_view);
            count_view=itemView.findViewById(R.id.count_view);
            star_textview=itemView.findViewById(R.id.star_textview);
            title_view=itemView.findViewById(R.id.title_view);
            sub_view=itemView.findViewById(R.id.sub_view);
            search_view=itemView.findViewById(R.id.search_view);
            percent_view=itemView.findViewById(R.id.percent_view);
        }
    }
}
