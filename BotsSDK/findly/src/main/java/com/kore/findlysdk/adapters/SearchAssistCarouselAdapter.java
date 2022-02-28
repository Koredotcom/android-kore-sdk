package com.kore.findlysdk.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.google.gson.Gson;
import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.listners.InvokeGenericWebViewInterface;
import com.kore.findlysdk.models.LinkedBotNLModel;
import com.kore.findlysdk.models.LiveSearchResultsModel;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BundleConstants;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.utils.markdown.MarkdownImageTagHandler;
import com.kore.findlysdk.utils.markdown.MarkdownTagHandler;
import com.kore.findlysdk.utils.markdown.MarkdownUtil;
import com.kore.findlysdk.view.RoundedCornersTransform;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

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
    private SharedPreferences sharedPreferences;

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
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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

        String imageUrl = "";
        holder.ivSuggestedPage.setVisibility(View.GONE);
        holder.llPages.setVisibility(VISIBLE);
        holder.llTask.setVisibility(GONE);

        holder.ibResults.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                switch (liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType())
                {
                    case BundleConstants.L_ONE:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }
                    break;
                    case BundleConstants.L_TWO:
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.llParentTitle.setVisibility(GONE);
                        holder.llFullDescription.setVisibility(VISIBLE);
                        holder.ivPageGridCell.setVisibility(GONE);
                    }
                    break;
                    case BundleConstants.L_THREE:
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(VISIBLE);
                        holder.ivPageGridCell.setVisibility(GONE);
                    }
                    break;
                    case BundleConstants.L_FOUR:
                    {
                        holder.tvDescription.setVisibility(GONE);
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(VISIBLE);
                        holder.ivPageGridCell.setVisibility(VISIBLE);

                    }
                    break;
                    case BundleConstants.L_FIVE:
                    {

                    }
                    break;
                }

                holder.ibResults.setVisibility(GONE);
                holder.ibResults2.setVisibility(VISIBLE);
            }
        });

        holder.ibResults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType())
                {
                    case BundleConstants.L_ONE:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.llFullDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }
                    break;
                    case BundleConstants.L_TWO:
                    {
                        holder.llParentTitle.setVisibility(GONE);
                        holder.tvDescription.setVisibility(VISIBLE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(GONE);

                        switch (liveSearchResultsModel.getSys_content_type())
                        {
                            case BundleConstants.FILE:
                                holder.rlArrows.setVisibility(GONE);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_THREE:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(VISIBLE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(GONE);

                        switch (liveSearchResultsModel.getSys_content_type())
                        {
                            case BundleConstants.FILE:
                                holder.rlArrows.setVisibility(GONE);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_FOUR:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(VISIBLE);
                        holder.rlArrows.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(GONE);

                        switch (liveSearchResultsModel.getSys_content_type())
                        {
                            case BundleConstants.FILE:
                                holder.ivPagesCell.setVisibility(VISIBLE);
                                holder.rlArrows.setVisibility(GONE);
                                break;
                            case BundleConstants.WEB:
                                holder.ivPagesCell.setVisibility(GONE);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_FIVE:
                    {

                    }
                    break;
                }

                holder.ibResults.setVisibility(VISIBLE);
                holder.ibResults2.setVisibility(GONE);
            }
        });

        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getContentType()))
            liveSearchResultsModel.setSys_content_type(liveSearchResultsModel.getContentType());

        if(liveSearchResultsModel.getSys_content_type() != null)
        {
            if(liveSearchResultsModel.getAppearance() != null && liveSearchResultsModel.getAppearance().getTemplate() != null)
            {
                if(!liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.L_FIVE))
                    holder.ivPagesCell.setVisibility(View.GONE);

                holder.llParentPages.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pages_item_bg));

                if(liveSearchResultsModel.getAppearance().getTemplate().getLayout() != null &&
                        !StringUtils.isNullOrEmpty(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getListType()) &&
                        liveSearchResultsModel.getAppearance().getTemplate().getLayout().getListType().equalsIgnoreCase(BundleConstants.PLAIN))
                    holder.llParentPages.setBackgroundColor(context.getResources().getColor(R.color.white));

                holder.tvTitle.setMaxLines(1);

                switch (liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType())
                {
                    case BundleConstants.L_ONE:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.llFullDescription.setVisibility(GONE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                    }
                    break;
                    case BundleConstants.L_TWO:
                    {
                        holder.llParentTitle.setVisibility(GONE);
                        holder.tvDescription.setVisibility(VISIBLE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(GONE);

                        switch (liveSearchResultsModel.getSys_content_type())
                        {
                            case BundleConstants.FILE:
                                holder.rlArrows.setVisibility(GONE);
                                break;
                        }

                        switch (liveSearchResultsModel.getAppearance().getTemplate().getType())
                        {
                            case BundleConstants.LAYOUT_TYPE_GRID:
                            case BundleConstants.LAYOUT_TYPE_CAROUSEL:
                                holder.rlArrows.setVisibility(GONE);
                                holder.tvDescription.setMaxLines(2);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_THREE:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(VISIBLE);
                        holder.ivPagesCell.setVisibility(GONE);
                        holder.rlArrows.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(GONE);

                        switch (liveSearchResultsModel.getSys_content_type())
                        {
                            case BundleConstants.FILE:
                                holder.rlArrows.setVisibility(GONE);
                                break;
                        }

                        switch (liveSearchResultsModel.getAppearance().getTemplate().getType())
                        {
                            case BundleConstants.LAYOUT_TYPE_GRID:
                            case BundleConstants.LAYOUT_TYPE_CAROUSEL:
                                holder.rlArrows.setVisibility(GONE);
                                holder.tvDescription.setMaxLines(2);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_FOUR:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(VISIBLE);
                        holder.rlArrows.setVisibility(VISIBLE);
                        holder.llFullDescription.setVisibility(GONE);

                        switch (liveSearchResultsModel.getSys_content_type())
                        {
                            case BundleConstants.FILE:
                                holder.ivPagesCell.setVisibility(VISIBLE);
                                holder.rlArrows.setVisibility(GONE);
                                break;
                            case BundleConstants.WEB:
                                holder.ivPageGridCell.setVisibility(GONE);
                                holder.ivPagesCell.setVisibility(VISIBLE);
                                holder.rlArrows.setVisibility(GONE);
                                break;
                        }

                        switch (liveSearchResultsModel.getAppearance().getTemplate().getType())
                        {
                            case BundleConstants.LAYOUT_TYPE_GRID:
                            case BundleConstants.LAYOUT_TYPE_CAROUSEL:
                                holder.rlArrows.setVisibility(GONE);
                                holder.ivListFourCell.setVisibility(VISIBLE);
                                holder.tvDescription.setMaxLines(2);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_FIVE:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                        holder.llFullDescription.setVisibility(VISIBLE);
                        holder.ivPageGridCell.setVisibility(VISIBLE);

                        holder.tvFullDescription.setMaxLines(2);

                        switch (liveSearchResultsModel.getAppearance().getTemplate().getType())
                        {
                            case BundleConstants.LAYOUT_TYPE_GRID:
                            case BundleConstants.LAYOUT_TYPE_CAROUSEL:
                                holder.rlArrows.setVisibility(GONE);
                                holder.llCentredContent.setVisibility(GONE);
                                holder.llImageCentredContent.setVisibility(VISIBLE);
                                holder.llPages.setVisibility(GONE);
                                holder.llFullDescription.setVisibility(GONE);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_SIX:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvDescription.setVisibility(GONE);
                        holder.rlArrows.setVisibility(GONE);
                        holder.llFullDescription.setVisibility(VISIBLE);
                        holder.ivPageGridCell.setVisibility(VISIBLE);

                        holder.tvFullDescription.setMaxLines(2);

                        switch (liveSearchResultsModel.getAppearance().getTemplate().getType())
                        {
                            case BundleConstants.LAYOUT_TYPE_GRID:
                                holder.rlArrows.setVisibility(GONE);
                                holder.llCentredContent.setVisibility(VISIBLE);
                                holder.llImageCentredContent.setVisibility(VISIBLE);
                                holder.llPages.setVisibility(GONE);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_SEVEN:
                    case BundleConstants.L_NINE:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);

                        holder.tvFullDescription.setMaxLines(2);
                        holder.tvFullDescriptionCentredContent.setMaxLines(2);

                        switch (liveSearchResultsModel.getAppearance().getTemplate().getType())
                        {
                            case BundleConstants.LAYOUT_TYPE_GRID:
                            case BundleConstants.LAYOUT_TYPE_CAROUSEL:
                                holder.rlArrows.setVisibility(GONE);
                                holder.llCentredContent.setVisibility(VISIBLE);
                                holder.llImageCentredContent.setVisibility(VISIBLE);
                                holder.llPages.setVisibility(GONE);
                                break;
                        }
                    }
                    break;
                    case BundleConstants.L_EIGHT:
                    {
                        holder.llParentTitle.setVisibility(VISIBLE);
                        holder.tvFullDescription.setMaxLines(2);
                        holder.tvFullDescriptionCentredContent.setMaxLines(2);

                        switch (liveSearchResultsModel.getAppearance().getTemplate().getType())
                        {
                            case BundleConstants.LAYOUT_TYPE_GRID:
                            case BundleConstants.LAYOUT_TYPE_CAROUSEL:
                                holder.rlArrows.setVisibility(GONE);
                                holder.llCentredContent.setVisibility(VISIBLE);
                                holder.llImageCentredContent.setVisibility(VISIBLE);
                                holder.tvTitleCentredContent.setVisibility(GONE);
                                holder.llPages.setVisibility(GONE);
                                break;
                        }
                    }
                    break;
                }

                switch (liveSearchResultsModel.getSys_content_type())
                {
                    case BundleConstants.FAQ:
                    {
                        imageUrl = liveSearchResultsModel.getImageUrl();

                        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getFaq_answer())
                                && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getFaq_question()))
                        {
                            String heading = unescapeHtml4(liveSearchResultsModel.getFaq_question());
                            heading = StringUtils.unescapeHtml3(heading);
                            heading = MarkdownUtil.processMarkDown(heading);
                            CharSequence sequence = Html.fromHtml(heading.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvTitle , heading), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                            String description = unescapeHtml4(liveSearchResultsModel.getFaq_answer());
                            description = StringUtils.unescapeHtml3(description);
                            description = MarkdownUtil.processMarkDown(description);
                            CharSequence sequenceDes = Html.fromHtml(description.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvDescription , description), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilderDes = new SpannableStringBuilder(sequenceDes);

                            if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.FAQ_QUESTION))
                            {
                                holder.tvTitle.setText(strBuilder);
                                holder.tvDescription.setText(strBuilderDes);
                                holder.tvFullDescription.setText(strBuilderDes);

                                holder.tvTitleCentredContent.setText(strBuilder);
                                holder.tvDescriptionCentredContent.setText(strBuilderDes);
                                holder.tvFullDescriptionCentredContent.setText(strBuilderDes);
                            }
                            else
                            {
                                holder.tvTitle.setText(strBuilderDes);
                                holder.tvDescription.setText(strBuilder);
                                holder.tvFullDescription.setText(strBuilder);

                                holder.tvTitleCentredContent.setText(strBuilderDes);
                                holder.tvDescriptionCentredContent.setText(strBuilder);
                                holder.tvFullDescriptionCentredContent.setText(strBuilder);

                                if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.L_TWO) ||
                                        liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.L_EIGHT))
                                {
                                    holder.tvDescription.setText(strBuilderDes);
                                    holder.tvFullDescription.setText(strBuilderDes);
                                    holder.tvDescriptionCentredContent.setText(strBuilderDes);
                                    holder.tvFullDescriptionCentredContent.setText(strBuilderDes);
                                }


                            }
                        }
                    }
                    break;
                    case BundleConstants.WEB :
                    {
                        imageUrl = liveSearchResultsModel.getPage_image_url();

                        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getPage_title())
                                && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getPage_preview()))
                        {
                            String heading = unescapeHtml4(liveSearchResultsModel.getPage_title());
                            heading = StringUtils.unescapeHtml3(heading);
                            heading = MarkdownUtil.processMarkDown(heading);
                            CharSequence sequence = Html.fromHtml(heading.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvTitle , heading), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                            String description = unescapeHtml4(liveSearchResultsModel.getPage_preview());
                            description = StringUtils.unescapeHtml3(description);
                            description = MarkdownUtil.processMarkDown(description);
                            CharSequence sequenceDes = Html.fromHtml(description.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvDescription , description), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilderDes = new SpannableStringBuilder(sequenceDes);

                            if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.PAGE_TITLE))
                            {
                                holder.tvTitle.setText(strBuilder);
                                holder.tvDescription.setText(strBuilderDes);
                                holder.tvFullDescription.setText(strBuilderDes);

                                holder.tvTitleCentredContent.setText(strBuilder);
                                holder.tvDescriptionCentredContent.setText(strBuilderDes);
                                holder.tvFullDescriptionCentredContent.setText(strBuilderDes);

                            }
                            else
                            {
                                holder.tvTitle.setText(strBuilderDes);
                                holder.tvDescription.setText(strBuilder);
                                holder.tvFullDescription.setText(strBuilder);

                                holder.tvTitleCentredContent.setText(strBuilderDes);
                                holder.tvDescriptionCentredContent.setText(strBuilder);
                                holder.tvFullDescriptionCentredContent.setText(strBuilder);
                            }
                        }
                    }
                    break;
                    case BundleConstants.FILE:
                    {
                        imageUrl = liveSearchResultsModel.getFile_image_url();

                        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getFile_title())
                                && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getFile_preview()))
                        {
                            String heading = unescapeHtml4(liveSearchResultsModel.getFile_title());
                            heading = StringUtils.unescapeHtml3(heading);
                            heading = MarkdownUtil.processMarkDown(heading);
                            CharSequence sequence = Html.fromHtml(heading.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvTitle , heading), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                            String description = unescapeHtml4(liveSearchResultsModel.getFile_preview());
                            description = StringUtils.unescapeHtml3(description);
                            description = MarkdownUtil.processMarkDown(description);
                            CharSequence sequenceDes = Html.fromHtml(description.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvDescription , description), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilderDes = new SpannableStringBuilder(sequenceDes);

                            if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.FILE_TITLE))
                            {
                                holder.tvTitle.setText(strBuilder);
                                holder.tvDescription.setText(strBuilderDes);
                                holder.tvFullDescription.setText(strBuilderDes);

                                holder.tvTitleCentredContent.setText(strBuilder);
                                holder.tvDescriptionCentredContent.setText(strBuilderDes);
                                holder.tvFullDescriptionCentredContent.setText(strBuilderDes);

                            }
                            else
                            {
                                holder.tvTitle.setText(strBuilderDes);
                                holder.tvDescription.setText(strBuilder);
                                holder.tvFullDescription.setText(strBuilder);

                                holder.tvTitleCentredContent.setText(strBuilderDes);
                                holder.tvDescriptionCentredContent.setText(strBuilder);
                                holder.tvFullDescriptionCentredContent.setText(strBuilder);
                            }
                        }
                    }
                    break;
                    case BundleConstants.DATA:
                    {
                        imageUrl = liveSearchResultsModel.getImageUrl();

                        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getTitle())
                                && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getDescription()))
                        {
                            String heading = unescapeHtml4(liveSearchResultsModel.getTitle());
                            heading = StringUtils.unescapeHtml3(heading);
                            heading = MarkdownUtil.processMarkDown(heading);
                            CharSequence sequence = Html.fromHtml(heading.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvTitle , heading), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                            String description = unescapeHtml4(liveSearchResultsModel.getDescription());
                            description = StringUtils.unescapeHtml3(description);
                            description = MarkdownUtil.processMarkDown(description);
                            CharSequence sequenceDes = Html.fromHtml(description.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvDescription , description), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilderDes = new SpannableStringBuilder(sequenceDes);

                            if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.TITLE))
                            {
                                holder.tvTitle.setText(strBuilder);
                                holder.tvDescription.setText(strBuilderDes);
                                holder.tvFullDescription.setText(strBuilderDes);

                                holder.tvTitleCentredContent.setText(strBuilder);
                                holder.tvDescriptionCentredContent.setText(strBuilderDes);
                                holder.tvFullDescriptionCentredContent.setText(strBuilderDes);

                            }
                            else
                            {
                                holder.tvTitle.setText(strBuilderDes);
                                holder.tvDescription.setText(strBuilder);
                                holder.tvFullDescription.setText(strBuilder);

                                holder.tvTitleCentredContent.setText(strBuilderDes);
                                holder.tvDescriptionCentredContent.setText(strBuilder);
                                holder.tvFullDescriptionCentredContent.setText(strBuilder);
                            }
                        }

                        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getQuestion())
                                && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getAnswer()))
                        {
                            String heading = unescapeHtml4(liveSearchResultsModel.getQuestion());
                            heading = StringUtils.unescapeHtml3(heading);
                            heading = MarkdownUtil.processMarkDown(heading);
                            CharSequence sequence = Html.fromHtml(heading.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvTitle , heading), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                            String description = unescapeHtml4(liveSearchResultsModel.getAnswer());
                            description = StringUtils.unescapeHtml3(description);
                            description = MarkdownUtil.processMarkDown(description);
                            CharSequence sequenceDes = Html.fromHtml(description.replace("\n", "<br />"),
                                    new MarkdownImageTagHandler(context, holder.tvDescription , description), new MarkdownTagHandler());
                            SpannableStringBuilder strBuilderDes = new SpannableStringBuilder(sequenceDes);

                            if(liveSearchResultsModel.getAppearance().getTemplate().getMapping().getHeading().equalsIgnoreCase(BundleConstants.QUESTION))
                            {
                                holder.tvTitle.setText(strBuilder);
                                holder.tvDescription.setText(strBuilderDes);
                                holder.tvFullDescription.setText(strBuilderDes);

                                holder.tvTitleCentredContent.setText(strBuilder);
                                holder.tvDescriptionCentredContent.setText(strBuilderDes);
                                holder.tvFullDescriptionCentredContent.setText(strBuilderDes);

                            }
                            else
                            {
                                holder.tvTitle.setText(strBuilderDes);
                                holder.tvDescription.setText(strBuilder);
                                holder.tvFullDescription.setText(strBuilder);

                                holder.tvTitleCentredContent.setText(strBuilderDes);
                                holder.tvDescriptionCentredContent.setText(strBuilder);
                                holder.tvFullDescriptionCentredContent.setText(strBuilder);
                            }
                        }
                    }
                    break;
                    default:
                    {
                        holder.llPages.setVisibility(View.GONE);
                        holder.llTask.setVisibility(View.VISIBLE);
                        holder.rlArrows.setVisibility(GONE);

                        if(liveSearchResultsModel.getAppearance().getTemplate().getLayout() != null)
                        {
                            if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getTextAlignment().equalsIgnoreCase(BundleConstants.CENTER))
                                holder.tvTaskName.setGravity(Gravity.CENTER);
                        }

                        if(!StringUtils.isNullOrEmpty(liveSearchResultsModel.getName()))
                            holder.tvTaskName.setText(liveSearchResultsModel.getName());
                    }
                    break;
                }

                if(liveSearchResultsModel.getAppearance().getTemplate().getLayout() != null)
                {
                    if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getTextAlignment().equalsIgnoreCase(BundleConstants.CENTER))
                        holder.tvTitle.setGravity(Gravity.CENTER);
                }

                if (!StringUtils.isNullOrEmpty(imageUrl))
                {
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
                            .into(new DrawableImageViewTarget(holder.ivPageGridCell));
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE))
                            .error(R.mipmap.imageplaceholder_left)
                            .into(new DrawableImageViewTarget(holder.ivListFourCell));
                }
            }
        }

        holder.tvPageTitle.setVisibility(View.GONE);

        if(this.from == 1)
        {
            if(position == 0)
            {
                holder.tvPageTitle.setVisibility(VISIBLE);

                if(liveSearchResultsModel.getSys_content_type() != null)
                    holder.tvPageTitle.setText(liveSearchResultsModel.getSys_content_type());
            }

            if ((position - 1) >= 0)
            {
                if(liveSearchResultsModel.getSys_content_type() != null)
                {
                    if (!liveSearchResultsModel.getSys_content_type().equalsIgnoreCase(model.get(position - 1).getSys_content_type())) {
                        holder.tvPageTitle.setVisibility(View.VISIBLE);
                        holder.tvPageTitle.setText(liveSearchResultsModel.getSys_content_type());
                    }
                }
            }
        }

        holder.llTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(composeFooterInterface != null && !StringUtils.isNullOrEmpty(liveSearchResultsModel.getText()))
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    try
                    {
                        LinkedBotNLModel linkedBotNLModel = new LinkedBotNLModel(liveSearchResultsModel.getChildBotName(), liveSearchResultsModel.getText(), true);
                        editor.putString(BundleConstants.LINKED_BOT_NL_META, new Gson().toJson(linkedBotNLModel));
                        editor.commit();
                    }
                    catch (Exception e)
                    {
                        editor.putString(BundleConstants.LINKED_BOT_NL_META, "");
                        editor.commit();

                        e.printStackTrace();
                    }

                    composeFooterInterface.onSendClick(liveSearchResultsModel.getText(), /*StringUtils.stringToBase64(*/liveSearchResultsModel.getText()/*)*/, false);
                }
            }
        });



        container.addView(carouselItemLayout);
        return carouselItemLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle, tvTaskName, tvTitleCentredContent, tvDescriptionCentredContent, tvFullDescriptionCentredContent;
        ImageView ivPagesCell, ivPageGridCell, ivSuggestedPage, ivTaskCell, ivCenteredContent, ivListFourCell;
        LinearLayout llPages, llTask, llImageCentredContent, llCentredContent, llFullDescription, llParentPages, llParentTitle;
        ImageButton ibResults, ibResults2;
        RelativeLayout rlArrows;


        public ViewHolder(View itemView) {
            super(itemView);
            this.ivPagesCell = (ImageView) itemView.findViewById(R.id.ivPagesCell);
            this.ivPageGridCell = (ImageView) itemView.findViewById(R.id.ivPageGridCell);
            this.ivListFourCell = (ImageView) itemView.findViewById(R.id.ivListFourCell);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            this.tvFullDescription = (TextView) itemView.findViewById(R.id.tvFullDescription);
            this.tvPageTitle = (TextView) itemView.findViewById(R.id.tvPageTitle);
            this.ivSuggestedPage = (ImageView) itemView.findViewById(R.id.ivSuggestedPage);
            this.llPages = (LinearLayout) itemView.findViewById(R.id.llPages);
            this.ivTaskCell = (ImageView) itemView.findViewById(R.id.ivTaskCell);
            this.tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            this.llTask = (LinearLayout) itemView.findViewById(R.id.llTask);
            this.llCentredContent = (LinearLayout) itemView.findViewById(R.id.llCentredContent);
            this.llImageCentredContent = (LinearLayout) itemView.findViewById(R.id.llImageCentredContent);
            this.tvTitleCentredContent = (TextView) itemView.findViewById(R.id.tvTitleCentredContent);
            this.tvDescriptionCentredContent = (TextView) itemView.findViewById(R.id.tvDescriptionCentredContent);
            this.tvFullDescriptionCentredContent = (TextView) itemView.findViewById(R.id.tvFullDescriptionCentredContent);
            this.ivCenteredContent = (ImageView) itemView.findViewById(R.id.ivCenteredContent);
            this.ibResults = (ImageButton) itemView.findViewById(R.id.ibResults);
            this.ibResults2 = (ImageButton) itemView.findViewById(R.id.ibResults2);
            this.rlArrows = (RelativeLayout) itemView.findViewById(R.id.rlArrows);
            this.llFullDescription = (LinearLayout) itemView.findViewById(R.id.llFullDescription);
            this.llParentPages = (LinearLayout) itemView.findViewById(R.id.llParentPages);
            this.llParentTitle = (LinearLayout) itemView.findViewById(R.id.llParentTitle);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
