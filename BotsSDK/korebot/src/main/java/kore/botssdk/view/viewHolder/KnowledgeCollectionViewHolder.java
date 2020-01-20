package kore.botssdk.view.viewHolder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.utils.Utility;

public class KnowledgeCollectionViewHolder extends RecyclerView.ViewHolder {

       public TextView icon_view,staricon,peopleicon, root_title_view, count_view, star_textview, title_view, sub_view, search_view, percent_view;
        public View header;

        public KnowledgeCollectionViewHolder(@NonNull View itemView) {
            super(itemView);

            icon_view=itemView.findViewById(R.id.icon_view);

            staricon=itemView.findViewById(R.id.staricon);


            peopleicon=itemView.findViewById(R.id.peopleicon);


            header=itemView.findViewById(R.id.header);
            root_title_view=itemView.findViewById(R.id.root_title_view);
            count_view=itemView.findViewById(R.id.count_view);
            star_textview=itemView.findViewById(R.id.star_textview);
            title_view=itemView.findViewById(R.id.title_view);
            sub_view=itemView.findViewById(R.id.sub_view);
            search_view=itemView.findViewById(R.id.search_view);
            percent_view=itemView.findViewById(R.id.percent_view);
        }
    }