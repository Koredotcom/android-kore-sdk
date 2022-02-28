package com.kore.findlysdk.adapters;

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
import com.kore.findlysdk.models.ResultsViewAppearance;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SearchResultsCarouselAdapter extends PagerAdapter {
    private LayoutInflater ownLayoutInflater;
    private ArrayList<HashMap<String, Object>> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    private float dp1;
    private float pageWidth = 0.8f;

    public SearchResultsCarouselAdapter(Context context, ArrayList<HashMap<String, Object>> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface, ComposeFooterInterface composeFooterInterface) {
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
    public int getCount() {
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
    public Object instantiateItem(ViewGroup container, int position) {
//        final LiveSearchResultsModel liveSearchResultsModel = model.get(position);
        final HashMap<String, Object> objectHashMap = model.get(position);
        View carouselItemLayout = ownLayoutInflater.inflate(R.layout.search_carousel_item, container, false);
        final SearchAssistCarouselAdapter.ViewHolder holder = new SearchAssistCarouselAdapter.ViewHolder(carouselItemLayout);

        holder.ivSuggestedPage.setVisibility(View.GONE);
        holder.tvFullDescriptionCentredContent.setMovementMethod(new ScrollingMovementMethod());
        holder.llPages.setVisibility(VISIBLE);
        holder.llTask.setVisibility(GONE);
        holder.rlArrows.setVisibility(GONE);

        if (objectHashMap.get(BundleConstants.SYS_CONTENT_TYPE) != null) {
            holder.llPages.setVisibility(VISIBLE);

            final ResultsViewAppearance resultsViewAppearance = (ResultsViewAppearance) objectHashMap.get(BundleConstants.APPEARANCE);

            if (resultsViewAppearance != null && resultsViewAppearance.getTemplate() != null) {
                if (resultsViewAppearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_TEXT))
                    holder.ivPagesCell.setVisibility(View.GONE);
                else if (resultsViewAppearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_IMAGE))
                    holder.ivPagesCell.setVisibility(VISIBLE);
                else if (resultsViewAppearance.getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.TILE_WITH_CENTERED_CONTENT)) {
                    holder.ivPagesCell.setVisibility(GONE);
                    holder.llPages.setVisibility(GONE);
                    holder.llImageCentredContent.setVisibility(VISIBLE);
                    holder.llCentredContent.setVisibility(VISIBLE);
                } else {
//                    holder.tvDescription.setVisibility(GONE);
                    holder.ivPagesCell.setVisibility(GONE);
                    holder.rlArrows.setVisibility(GONE);
                }

                holder.tvTitle.setMaxLines(1);

                if (objectHashMap.containsKey(resultsViewAppearance.getTemplate().getMapping().getHeading())) {
                    String heading = (String) objectHashMap.get(resultsViewAppearance.getTemplate().getMapping().getHeading());
                    String description = (String) objectHashMap.get(resultsViewAppearance.getTemplate().getMapping().getDescription());

                    if (!StringUtils.isNullOrEmpty(heading) &&
                            !StringUtils.isNullOrEmpty(description)) {
                        holder.tvTitle.setText(heading);
                        holder.tvDescription.setText(description);
                        holder.tvFullDescription.setText(description);

                        holder.tvTitleCentredContent.setText(heading);
                        holder.tvDescriptionCentredContent.setText(description);
                        holder.tvFullDescriptionCentredContent.setText(description);
                    }
                }

                if (resultsViewAppearance.getTemplate().getType().equalsIgnoreCase(BundleConstants.LAYOUT_TYPE_GRID)) {
                    holder.rlArrows.setVisibility(GONE);
                    holder.tvDescription.setVisibility(GONE);
                    holder.tvFullDescription.setVisibility(VISIBLE);
                    holder.tvTitle.setMaxLines(Integer.MAX_VALUE);
                }

                if (resultsViewAppearance.getTemplate().getLayout().getIsClickable())
                    holder.rlArrows.setVisibility(GONE);

                String imageUrl = (String) objectHashMap.get(BundleConstants.FILE_IMAGE_URL);
                if (!StringUtils.isNullOrEmpty(imageUrl)) {
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE))
                            .error(R.mipmap.imageplaceholder_left)
                            .into(new DrawableImageViewTarget(holder.ivPagesCell));
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE))
                            .error(R.mipmap.imageplaceholder_left)
                            .into(new DrawableImageViewTarget(holder.ivCenteredContent));
                }

                final String file_url = (String) objectHashMap.get(BundleConstants.FILE_URL);

                holder.llPages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (resultsViewAppearance.getTemplate().getLayout() != null && resultsViewAppearance.getTemplate().getLayout().getIsClickable() &&
                                invokeGenericWebViewInterface != null && !StringUtils.isNullOrEmpty(file_url))
                            invokeGenericWebViewInterface.invokeGenericWebView(file_url);
                    }
                });
            }

            holder.ibResults.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.tvFullDescription.getVisibility() == GONE) {
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
                    if (holder.tvFullDescription.getVisibility() == VISIBLE) {
                        holder.tvDescription.setVisibility(VISIBLE);
                        holder.tvFullDescription.setVisibility(GONE);

                        holder.ibResults.setVisibility(VISIBLE);
                        holder.ibResults2.setVisibility(GONE);
                    }
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
