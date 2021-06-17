package com.kore.findlysdk.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SearchAssistCarouselAdapter extends PagerAdapter
{
    private LayoutInflater ownLayoutInflater;
    private ArrayList<LiveSearchResultsModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    private float dp1;
    private float pageWidth = 0.8f;

    public SearchAssistCarouselAdapter(Context context, ArrayList<LiveSearchResultsModel> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface, ComposeFooterInterface composeFooterInterface)
    {
        ownLayoutInflater = LayoutInflater.from(context);
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.from = from;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        this.composeFooterInterface = composeFooterInterface;
        this.dp1 = (int) AppControl.getInstance(context).getDimensionUtil().density;

        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(R.dimen.carousel_item_width_factor, typedValue, true);
        pageWidth = typedValue.getFloat();
    }

    @Override
    public int getCount()
    {
        return model.size();
    }

    @Override
    public float getPageWidth(int position) {
        if (getCount() == 0) {
            return super.getPageWidth(position);
        } else {
            return pageWidth;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        final LiveSearchResultsModel liveSearchResultsModel = model.get(position);
        View carouselItemLayout = ownLayoutInflater.inflate(R.layout.search_carousel_item, container, false);
        final ViewHolder holder = new ViewHolder(carouselItemLayout);

        holder.ivSuggestedPage.setVisibility(View.GONE);
        holder.tvFullDescriptionCentredContent.setMovementMethod(new ScrollingMovementMethod());
        holder.llPages.setVisibility(VISIBLE);
        holder.llTask.setVisibility(GONE);
        holder.rlArrows.setVisibility(VISIBLE);

        if(liveSearchResultsModel.getSysContentType() != null)
        {
            if(liveSearchResultsModel.getSysContentType().equalsIgnoreCase(BundleConstants.FAQ))
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

                    holder.tvTitle.setMaxLines(3);
                    holder.tvDescription.setMaxLines(4);

                    if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.QUESTION))
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getFaqQuestion().trim());
                        holder.tvDescription.setText(liveSearchResultsModel.getFaqAnswer().trim());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getFaqAnswer().trim());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getFaqQuestion().trim());
                        holder.tvDescriptionCentredContent.setText(liveSearchResultsModel.getFaqAnswer().trim());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getFaqAnswer().trim());
                    }
                    else
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getAnswer().trim());
                        holder.tvDescription.setText(liveSearchResultsModel.getFaqQuestion().trim());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getFaqQuestion().trim());

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getFaqAnswer().trim());
                        holder.tvDescriptionCentredContent.setText(liveSearchResultsModel.getFaqQuestion().trim());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getFaqQuestion().trim());
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                    {
                        holder.rlArrows.setVisibility(GONE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.tvFullDescription.setVisibility(VISIBLE);
                        holder.tvTitle.setMaxLines(Integer.MAX_VALUE);
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable())
                        holder.rlArrows.setVisibility(GONE);

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
                    else
                    {
//                        holder.ivPagesCell.setVisibility(View.GONE);
                        holder.ivPagesCell.setBackgroundResource(R.mipmap.imageplaceholder_left);
                    }
                }
            }
            else if (liveSearchResultsModel.getSysContentType().equalsIgnoreCase(BundleConstants.WEB))
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

                    holder.tvTitle.setMaxLines(3);
                    holder.tvDescription.setMaxLines(4);

                    if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.PAGE_TITLE))
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvDescription.setText(liveSearchResultsModel.getPagePreview().replace("\n", ""));
                        holder.tvFullDescription.setText(liveSearchResultsModel.getPageBody().replace("\n", ""));

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getPagePreview().replace("\n", ""));
                    }
                    else
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getPagePreview().replace("\n", ""));
                        holder.tvDescription.setText(liveSearchResultsModel.getPageTitle());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getPageBody().replace("\n", ""));
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                    {
                        holder.rlArrows.setVisibility(GONE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.tvFullDescription.setVisibility(VISIBLE);
                        holder.tvTitle.setMaxLines(Integer.MAX_VALUE);
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable())
                        holder.rlArrows.setVisibility(GONE);

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
                    else
                    {
//                        holder.ivPagesCell.setVisibility(View.GONE);
                        holder.ivPagesCell.setBackgroundResource(R.mipmap.imageplaceholder_left);
                    }

                    holder.llPages.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(liveSearchResultsModel.getAppearance().getTemplate().getLayout() != null && liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable() &&
                                    invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getPageUrl()))
                                invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getPageUrl());
                        }
                    });
                }
            }
            else if (liveSearchResultsModel.getSysContentType().equalsIgnoreCase(BundleConstants.FILE))
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

                    holder.tvTitle.setMaxLines(3);
                    holder.tvDescription.setMaxLines(4);

                    if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.PAGE_TITLE))
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getFileTitle());
                        holder.tvDescription.setText(liveSearchResultsModel.getFilePreview().replace("\n", ""));
                        holder.tvFullDescription.setText(liveSearchResultsModel.getFileContent().replace("\n", ""));

                        holder.tvTitleCentredContent.setText(liveSearchResultsModel.getFileTitle());
                        holder.tvFullDescriptionCentredContent.setText(liveSearchResultsModel.getFilePreview().replace("\n", ""));
                    }
                    else
                    {
                        holder.tvTitle.setText(liveSearchResultsModel.getFileTitle());
                        holder.tvDescription.setText(liveSearchResultsModel.getFilePreview());
                        holder.tvFullDescription.setText(liveSearchResultsModel.getFileContent().replace("\n", ""));
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID))
                    {
                        holder.rlArrows.setVisibility(GONE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.tvFullDescription.setVisibility(VISIBLE);
                        holder.tvTitle.setMaxLines(Integer.MAX_VALUE);
                    }

                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable())
                        holder.rlArrows.setVisibility(GONE);

                    if (!StringUtils.isNullOrEmpty(liveSearchResultsModel.getFileImageUrl()))
                    {
                        Glide.with(context)
                                .load(liveSearchResultsModel.getFileImageUrl())
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivPagesCell));
                        Glide.with(context)
                                .load(liveSearchResultsModel.getFileImageUrl())
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .error(R.mipmap.imageplaceholder_left)
                                .into(new DrawableImageViewTarget(holder.ivCenteredContent));
                    }
                    else
                    {
//                        holder.ivPagesCell.setVisibility(View.GONE);
                        holder.ivPagesCell.setBackgroundResource(R.mipmap.imageplaceholder_left);
                    }

                    holder.llPages.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(liveSearchResultsModel.getAppearance().getTemplate().getLayout() != null && liveSearchResultsModel.getAppearance().getTemplate().getLayout().getIsClickable() &&
                                    invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getFileUrl()))
                                invokeGenericWebViewInterface.invokeGenericWebView(liveSearchResultsModel.getFileUrl());
                        }
                    });
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

            holder.llTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(composeFooterInterface != null)
                        composeFooterInterface.onSendClick(liveSearchResultsModel.getPayload(), false);
                }
            });
        }



        container.addView(carouselItemLayout);
        return carouselItemLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
