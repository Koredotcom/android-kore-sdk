package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.SearchFaqModel;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;

public class SearchRecycleAdapter extends RecyclerView.Adapter<SearchRecycleAdapter.ViewHolder> {
    private ArrayList<SearchFaqModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public SearchRecycleAdapter(Context context, ArrayList<SearchFaqModel> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.from = from;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.live_search_pages_findly_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SearchFaqModel liveSearchResultsModel = model.get(position);

        holder.ivPagesCell.setVisibility(View.GONE);
        holder.ivSuggestedPage.setVisibility(View.GONE);

        if (liveSearchResultsModel.getQuestion() != null) {
            holder.tvTitle.setMaxLines(1);
            holder.tvTitle.setText(liveSearchResultsModel.getQuestion());
            holder.tvDescription.setText(liveSearchResultsModel.getAnswer());
            holder.tvFullDescription.setText(liveSearchResultsModel.getAnswer());

        }
//        else if (liveSearchResultsModel.getTitle() != null) {
//            holder.tvTitle.setMaxLines(2);
//            holder.ivPagesCell.setVisibility(View.VISIBLE);
//            holder.tvTitle.setText(liveSearchResultsModel.getTitle());
//            holder.tvDescription.setText(liveSearchResultsModel.getSearchResultPreview());
//            holder.ivSuggestedPage.setVisibility(View.VISIBLE);
//
//            if (!StringUtils.isNullOrEmpty(liveSearchResultsModel.getImageUrl()))
//                Glide.with(context).load(liveSearchResultsModel.getImageUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(holder.ivPagesCell));
//        }

//        if ((position - 1) >= 0)
//        {
//            if (!liveSearchResultsModel.getContentType().equalsIgnoreCase(model.get(position - 1).getContentType())) {
//                holder.tvPageTitle.setVisibility(View.VISIBLE);
//
//                switch (from)
//                {
//                    case 0:
//                        if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
//                            holder.tvPageTitle.setText("Suggested Pages");
//                        else
//                            holder.tvPageTitle.setText("Suggested FAQS");
//                        break;
//                    case 1:
//                        if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
//                            holder.tvPageTitle.setText("Matched Pages");
//                        else
//                            holder.tvPageTitle.setText("Matched FAQS");
//                        break;
//                }
//
//
//            } else
//                holder.tvPageTitle.setVisibility(View.GONE);
//        } else if (position == 0) {
            holder.tvPageTitle.setVisibility(View.VISIBLE);
//
//            switch (from)
//            {
//                case 0:
//                    if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
//                        holder.tvPageTitle.setText("Suggested Pages");
//                    else
//                        holder.tvPageTitle.setText("Suggested FAQS");
//                    break;
//                case 1:
//                    if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
//                        holder.tvPageTitle.setText("Matched Pages");
//                    else
                        holder.tvPageTitle.setText("Matched FAQS");
//                    break;
//            }
//
//
//        } else
//            holder.tvPageTitle.setVisibility(View.GONE);

        holder.tvTitle.setTag(true);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.FAQ)) {
                    if ((boolean) view.getTag()) {
                        holder.tvDescription.setVisibility(View.GONE);
                        holder.tvFullDescription.setVisibility(View.VISIBLE);
                        view.setTag(false);
                    } else {
                        holder.tvDescription.setVisibility(View.VISIBLE);
                        holder.tvFullDescription.setVisibility(View.GONE);
                        view.setTag(true);
                    }
                }
            }
        });

//        holder.llPages.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
//                    invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getUrl());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle;
        ImageView ivPagesCell, ivSuggestedPage;
        LinearLayout llPages;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ivPagesCell = (ImageView) itemView.findViewById(R.id.ivPagesCell);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            this.tvFullDescription = (TextView) itemView.findViewById(R.id.tvFullDescription);
            this.tvPageTitle = (TextView) itemView.findViewById(R.id.tvPageTitle);
            this.ivSuggestedPage = (ImageView) itemView.findViewById(R.id.ivSuggestedPage);
            this.llPages = (LinearLayout) itemView.findViewById(R.id.llPages);
        }
    }
}
