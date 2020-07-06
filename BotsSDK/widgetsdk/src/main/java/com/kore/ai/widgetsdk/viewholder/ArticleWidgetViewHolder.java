package com.kore.ai.widgetsdk.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.ai.widgetsdk.views.widgetviews.ArticlesWidgetView;

public class ArticleWidgetViewHolder extends RecyclerView.ViewHolder {

  /* public TabLayout sliding_tabs;
    public TextView view_more,widget_header;
    public ImageView widget_menu;
    public ProgressBar files_progress;
    public ViewPager viewpager;*/
    ArticlesWidgetView articlesWidgetView;
    public ArticleWidgetViewHolder(@NonNull View itemView) {
        super(itemView);
        articlesWidgetView=(ArticlesWidgetView)itemView;
        /*widget_menu = itemView.findViewById(R.id.widget_menu);
        view_more = itemView.findViewById(R.id.view_more);
        widget_header = itemView.findViewById(R.id.widget_header);
        sliding_tabs = itemView.findViewById(R.id.sliding_tabs);
        files_progress = itemView.findViewById(R.id.files_progress);
        viewpager = itemView.findViewById(R.id.pager);*/

    }

    public ArticlesWidgetView getCustomView() {

        return articlesWidgetView;
    }
}
