package com.kore.findlysdk.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LiveSearchCyclerAdapter extends RecyclerView.Adapter<LiveSearchCyclerAdapter.ViewHolder> {
    private ArrayList<LiveSearchResultsModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    private String cardImage= "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAAJ1BMVEUAAAAAVaoEbq4DbK8GbK4Gbq8Gba0Fba8Fba4Fbq4Eba4Fba7////SVqJwAAAAC3RSTlMAA0hJVYKDqKmq4875bAAAAAABYktHRAyBs1FjAAAAP0lEQVQI12NgwACMJi5A4CzAwLobDBIYOCaAxDknMLCvnAkEsyYwcECkkBicMDV4GGwQxQEMjCogK5wEMC0HALyTIMofpWLWAAAAAElFTkSuQmCC";

    public LiveSearchCyclerAdapter(Context context, ArrayList<LiveSearchResultsModel> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface, ComposeFooterInterface composeFooterInterface) {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.from = from;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        this.composeFooterInterface = composeFooterInterface;
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
        final LiveSearchResultsModel liveSearchResultsModel = model.get(position);


        holder.ivSuggestedPage.setVisibility(View.GONE);

        holder.llPages.setVisibility(VISIBLE);
        holder.llTask.setVisibility(GONE);
        holder.rlArrows.setVisibility(VISIBLE);

        if(liveSearchResultsModel.get__contentType() != null)
        {
            if(liveSearchResultsModel.get__contentType().equalsIgnoreCase(BundleConstants.FAQ))
            {
                if(liveSearchResultsModel.getAppearance() != null && liveSearchResultsModel.getAppearance().getTemplate() != null)
                {
                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_TEXT))
                        holder.ivPagesCell.setVisibility(View.GONE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_IMAGE))
                        holder.ivPagesCell.setVisibility(VISIBLE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
                    {
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.llPages.setVisibility(GONE);
                        holder.llImageCentredContent.setVisibility(VISIBLE);
                        holder.llCentredContent.setVisibility(VISIBLE);
                    }
                    else
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }

                    holder.tvTitle.setMaxLines(1);

                    if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.QUESTION))
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvDescription.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getAnswer().trim());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvDescriptionCentredContent.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getAnswer().trim());
                    }
                    else
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvDescription.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getQuestion().trim());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvDescriptionCentredContent.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getQuestion().trim());
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                    {
                        holder.rlArrows.setVisibility(GONE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.tvFullDescription.setVisibility(VISIBLE);
                        holder.tvTitle.setMaxLines(Integer.MAX_VALUE);
                    }
                }
            }
            else if (liveSearchResultsModel.get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
            {
                holder.llPages.setVisibility(VISIBLE);

                if(liveSearchResultsModel.getAppearance() != null && liveSearchResultsModel.getAppearance().getTemplate() != null)
                {
                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_TEXT))
                        holder.ivPagesCell.setVisibility(View.GONE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_IMAGE))
                        holder.ivPagesCell.setVisibility(VISIBLE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
                    {
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.llPages.setVisibility(GONE);
                        holder.llImageCentredContent.setVisibility(VISIBLE);
                        holder.llCentredContent.setVisibility(VISIBLE);
                    }
                    else
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }

                    holder.tvTitle.setMaxLines(1);

                    if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.PAGE_TITLE))
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvDescription.setText(liveSearchResultsModel.getPageSearchResultPreview());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getPageSearchResultPreview());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getPageSearchResultPreview());
                    }
                    else
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getPageSearchResultPreview());
                        holder.tvDescription.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getPageSearchResultPreview());
                    }

                    if (!StringUtils.isNullOrEmpty(liveSearchResultsModel.getPageImageUrl()))
                    {
                        Glide.with(context)
                                .load(liveSearchResultsModel.getPageImageUrl())
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivPagesCell));
                        Glide.with(context)
                                .load(liveSearchResultsModel.getPageImageUrl())
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivCenteredContent));
                    }
                }
            }
            else
            {
                holder.llPages.setVisibility(View.GONE);
                holder.llTask.setVisibility(View.VISIBLE);
                holder.rlArrows.setVisibility(GONE);

                if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getTaskName()))
                    holder.tvTaskName.setText(liveSearchResultsModel.getTaskName());
                else if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getName()))
                    holder.tvTaskName.setText(liveSearchResultsModel.getName());
            }
        }
        else if(liveSearchResultsModel.getContentType() != null)
        {
            if(liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.FAQ))
            {
                if(liveSearchResultsModel.getAppearance() != null && liveSearchResultsModel.getAppearance().getTemplate() != null)
                {
                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_TEXT))
                        holder.ivPagesCell.setVisibility(View.GONE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_IMAGE))
                        holder.ivPagesCell.setVisibility(VISIBLE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
                    {
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.llPages.setVisibility(GONE);
                        holder.llImageCentredContent.setVisibility(VISIBLE);
                        holder.llCentredContent.setVisibility(VISIBLE);
                    }
                    else
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }

                    holder.tvTitle.setMaxLines(1);

                    if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.QUESTION))
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvDescription.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getAnswer().trim());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvDescriptionCentredContent.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getAnswer().trim());
                    }
                    else
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvDescription.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getQuestion().trim());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvDescriptionCentredContent.setText(liveSearchResultsModel.getQuestion().trim());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getQuestion().trim());
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                    {
                        holder.rlArrows.setVisibility(GONE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.tvFullDescription.setVisibility(VISIBLE);
                    }
                }
            }
            else if (liveSearchResultsModel.getContentType().equalsIgnoreCase(BundleConstants.PAGE))
            {
                holder.llPages.setVisibility(VISIBLE);

                if(liveSearchResultsModel.getAppearance() != null && liveSearchResultsModel.getAppearance().getTemplate() != null)
                {
                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_TEXT))
                        holder.ivPagesCell.setVisibility(View.GONE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_IMAGE))
                        holder.ivPagesCell.setVisibility(VISIBLE);
                    else if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT))
                    {
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.llPages.setVisibility(GONE);
                        holder.llImageCentredContent.setVisibility(VISIBLE);
                        holder.llCentredContent.setVisibility(VISIBLE);
                    }
                    else
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }

                    holder.tvTitle.setMaxLines(1);

                    if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.PAGE_TITLE))
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvDescription.setText(liveSearchResultsModel.getPageSearchResultPreview());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getPageSearchResultPreview());
                    }
                    else
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getPageSearchResultPreview());
                        holder.tvDescription.setText(liveSearchResultsModel.getPageTitle());
                    }

                    if (!StringUtils.isNullOrEmpty(liveSearchResultsModel.getPageImageUrl()))
                    {
                        Glide.with(context)
                                .load(liveSearchResultsModel.getPageImageUrl())
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
//                        .thumbnail(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivPagesCell));
                        Glide.with(context)
                                .load(liveSearchResultsModel.getPageImageUrl())
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
//                        .thumbnail(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivCenteredContent));
                    }
                }
            }
            else
            {
                holder.llPages.setVisibility(View.GONE);
                holder.llTask.setVisibility(View.VISIBLE);
                holder.rlArrows.setVisibility(GONE);

                if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getTaskName()))
                    holder.tvTaskName.setText(liveSearchResultsModel.getTaskName());
                else if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getName()))
                    holder.tvTaskName.setText(liveSearchResultsModel.getName());
            }
        }

        holder.tvPageTitle.setVisibility(View.GONE);

        if(this.from == 1)
        {
            if(position == 0)
            {
                holder.tvPageTitle.setVisibility(VISIBLE);

                if(liveSearchResultsModel.get__contentType() != null)
                    holder.tvPageTitle.setText(liveSearchResultsModel.get__contentType());
                else if(liveSearchResultsModel.getContentType() != null)
                    holder.tvPageTitle.setText(liveSearchResultsModel.getContentType());
            }

            if ((position - 1) >= 0)
            {
                if(liveSearchResultsModel.get__contentType() != null)
                {
                    if (!liveSearchResultsModel.get__contentType().equalsIgnoreCase(model.get(position - 1).get__contentType())) {
                        holder.tvPageTitle.setVisibility(View.VISIBLE);
                        holder.tvPageTitle.setText(liveSearchResultsModel.get__contentType());
                    }
                }
                else if(liveSearchResultsModel.getContentType() != null)
                {
                    if (!liveSearchResultsModel.getContentType().equalsIgnoreCase(model.get(position - 1).getContentType())) {
                        holder.tvPageTitle.setVisibility(View.VISIBLE);
                        holder.tvPageTitle.setText(liveSearchResultsModel.getContentType());
                    }
                }

            }
        }

//        holder.tvTitle.setTag(true);
//        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable())
//                {
//                    if (liveSearchResultsModel.get__contentType().equalsIgnoreCase(BundleConstants.FAQ))
//                    {
//                        if ((boolean) view.getTag()) {
//                            holder.tvDescription.setVisibility(View.GONE);
//                            holder.tvFullDescription.setVisibility(View.VISIBLE);
//                            view.setTag(false);
//                        } else {
//                            holder.tvDescription.setVisibility(View.VISIBLE);
//                            holder.tvFullDescription.setVisibility(View.GONE);
//                            view.setTag(true);
//                        }
//                    }
//                    else if (liveSearchResultsModel.get__contentType().equalsIgnoreCase(BundleConstants.PAGE))
//                    {
//                        if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable())
//                        {
//                            view.setTag(true);
//                            if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
//                                invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getUrl());
//                            else if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getExternalFileUrl()))
//                                invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getExternalFileUrl());
//                        }
//                    }
//                }
//            }
//        });

        holder.ibResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.tvFullDescription.getVisibility() == GONE)
                {
                    holder.tvDescription.setVisibility(GONE);
                    holder.tvFullDescription.setVisibility(VISIBLE);

                    holder.ibResults.setVisibility(GONE);
                    holder.ibResults2.setVisibility(VISIBLE);
                }
            }
        });

        holder.ibResults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.tvFullDescription.getVisibility() == VISIBLE)
                {
                    holder.tvDescription.setVisibility(VISIBLE);
                    holder.tvFullDescription.setVisibility(GONE);

                    holder.ibResults.setVisibility(VISIBLE);
                    holder.ibResults2.setVisibility(GONE);
                }
            }
        });

        holder.llPages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable())
                {
                    if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getUrl()))
                        invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getUrl());
                    else if(invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getExternalFileUrl()))
                        invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getExternalFileUrl());
                }
            }
        });

        holder.llTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(composeFooterInterface != null)
                    composeFooterInterface.onSendClick(liveSearchResultsModel.getPayload(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void refresh(ArrayList<LiveSearchResultsModel> arrLiveSearchResultsModels)
    {
        this.model = arrLiveSearchResultsModels;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle, tvTaskName, tvTitleCentredContent, tvDescriptionCentredContent, tvFullDescriptionCentredContent;
        ImageView ivPagesCell, ivSuggestedPage, ivTaskCell, ivCenteredContent;
        LinearLayout llPages, llTask, llImageCentredContent, llCentredContent;
        ImageButton ibResults, ibResults2;
        RelativeLayout rlArrows;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ivPagesCell = (ImageView) itemView.findViewById(R.id.ivPagesCell);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            this.tvFullDescription = (TextView) itemView.findViewById(R.id.tvFullDescription);
            this.tvPageTitle = (TextView) itemView.findViewById(R.id.tvPageTitle);
            this.ivSuggestedPage = (ImageView) itemView.findViewById(R.id.ivSuggestedPage);
            this.llPages = (LinearLayout) itemView.findViewById(R.id.llPages);
            this.ivTaskCell = (ImageView) itemView.findViewById(R.id.ivTaskCell);
            this.tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            this.llTask = (LinearLayout) itemView.findViewById(R.id.llTask);
            this.llCentredContent = (LinearLayout) itemView.findViewById(R.id.llImageCentredContent);
            this.llImageCentredContent = (LinearLayout) itemView.findViewById(R.id.llCentredContent);
            this.tvTitleCentredContent = (TextView) itemView.findViewById(R.id.tvTitleCentredContent);
            this.tvDescriptionCentredContent = (TextView) itemView.findViewById(R.id.tvDescriptionCentredContent);
            this.tvFullDescriptionCentredContent = (TextView) itemView.findViewById(R.id.tvFullDescriptionCentredContent);
            this.ivCenteredContent = (ImageView) itemView.findViewById(R.id.ivCenteredContent);
            this.ibResults = (ImageButton) itemView.findViewById(R.id.ibResults);
            this.ibResults2 = (ImageButton) itemView.findViewById(R.id.ibResults2);
            this.rlArrows = (RelativeLayout) itemView.findViewById(R.id.rlArrows);
        }
    }
}
