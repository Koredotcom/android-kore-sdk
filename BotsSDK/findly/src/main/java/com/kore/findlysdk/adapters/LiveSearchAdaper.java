package com.kore.findlysdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.kore.findlysdk.R;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class LiveSearchAdaper extends BaseAdapter
{
    private Context context;
    private ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels;

    public LiveSearchAdaper(Context context, ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels)
    {
        this.context = context;
        this.arrLiveSearchResultsModels = arrLiveSearchResultsModels;
    }

    @Override
    public int getCount()
    {
        return arrLiveSearchResultsModels.size();
    }

    @Override
    public Object getItem(int i)
    {
        return arrLiveSearchResultsModels.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        final LiveSearchViewHolder liveSearchViewHolder;

        if (view == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.live_search_pages_findly_item, null);
            liveSearchViewHolder = new LiveSearchViewHolder();
            liveSearchViewHolder.tvTitle = view.findViewById(R.id.tvTitle);
            liveSearchViewHolder.tvDescription = view.findViewById(R.id.tvDescription);
            liveSearchViewHolder.tvFullDescription = view.findViewById(R.id.tvFullDescription);
            liveSearchViewHolder.ivPagesCell = view.findViewById(R.id.ivPagesCell);
            liveSearchViewHolder.tvPageTitle = view.findViewById(R.id.tvPageTitle);
            liveSearchViewHolder.llPages = view.findViewById(R.id.llPages);
            view.setTag(liveSearchViewHolder);
        } else {
            liveSearchViewHolder = (LiveSearchViewHolder) view.getTag();
        }

        final LiveSearchResultsModel popularSearchModel = (LiveSearchResultsModel) getItem(position);
        liveSearchViewHolder.ivPagesCell.setVisibility(View.GONE);
        liveSearchViewHolder.llPages.setVisibility(VISIBLE);

        if(popularSearchModel.getQuestion() != null)
        {
            liveSearchViewHolder.tvTitle.setMaxLines(1);
            liveSearchViewHolder.tvTitle.setText(popularSearchModel.getQuestion());
            liveSearchViewHolder.tvDescription.setText(popularSearchModel.getAnswer());
            liveSearchViewHolder.tvFullDescription.setText(popularSearchModel.getAnswer());
        }
        else if(popularSearchModel.getTitle() != null)
        {
            liveSearchViewHolder.tvTitle.setMaxLines(2);
            liveSearchViewHolder.ivPagesCell.setVisibility(View.VISIBLE);
            liveSearchViewHolder.tvTitle.setText(popularSearchModel.getTitle());
            liveSearchViewHolder.tvDescription.setText(popularSearchModel.getSearchResultPreview());

            if(!StringUtils.isNullOrEmpty(popularSearchModel.getImageUrl()))
                Glide.with(context).load(popularSearchModel.getImageUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(liveSearchViewHolder.ivPagesCell));
        }

        if ((position - 1) >= 0)
        {
            if (!popularSearchModel.get__contentType().equalsIgnoreCase(arrLiveSearchResultsModels.get(position - 1).get__contentType()))
            {
                liveSearchViewHolder.tvPageTitle.setVisibility(View.VISIBLE);

                if (popularSearchModel.get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
                    liveSearchViewHolder.tvPageTitle.setText("Matched Pages");
                else
                    liveSearchViewHolder.tvPageTitle.setText("Matched FAQS");


            } else
                liveSearchViewHolder.tvPageTitle.setVisibility(View.GONE);
        }
        else if (position == 0)
        {
            liveSearchViewHolder.tvPageTitle.setVisibility(View.VISIBLE);

            if (popularSearchModel.get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
                liveSearchViewHolder.tvPageTitle.setText("Matched Pages");
            else
                liveSearchViewHolder.tvPageTitle.setText("Matched FAQS");

        } else
            liveSearchViewHolder.tvPageTitle.setVisibility(View.GONE);

        liveSearchViewHolder.tvTitle.setTag(true);
        liveSearchViewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popularSearchModel.get__contentType().equalsIgnoreCase(BundleConstants.FAQ)) {
                    if ((boolean) view.getTag()) {
                        liveSearchViewHolder.tvDescription.setVisibility(View.GONE);
                        liveSearchViewHolder.tvFullDescription.setVisibility(View.VISIBLE);
                        view.setTag(false);
                    } else {
                        liveSearchViewHolder.tvDescription.setVisibility(View.VISIBLE);
                        liveSearchViewHolder.tvFullDescription.setVisibility(View.GONE);
                        view.setTag(true);
                    }
                }
            }
        });

        return view;
    }

    private class LiveSearchViewHolder {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle;
        ImageView ivPagesCell;
        LinearLayout llPages;
    }
}
