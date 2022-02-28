package com.kore.findlysdk.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

public class LiveSearchAdaper extends BaseAdapter
{
    private ArrayList<LiveSearchResultsModel> model;
    private RoundedCornersTransform roundedCornersTransform;
    private Context context;
    private int from = 0;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    private SharedPreferences sharedPreferences;

    public LiveSearchAdaper(Context context, ArrayList<LiveSearchResultsModel> model, int from, InvokeGenericWebViewInterface invokeGenericWebViewInterface, ComposeFooterInterface composeFooterInterface)
    {
        this.model = model;
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.from = from;
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
        this.composeFooterInterface = composeFooterInterface;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getCount()
    {
        return model.size();
    }

    @Override
    public Object getItem(int i)
    {
        return model.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        final ViewHolder holder;
        String imageUrl = "";

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.live_search_pages_findly_item,  null);
            holder = new ViewHolder();
            holder.ivPagesCell = (ImageView) convertView.findViewById(R.id.ivPagesCell);
            holder.ivPageGridCell = (ImageView) convertView.findViewById(R.id.ivPageGridCell);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            holder.tvFullDescription = (TextView) convertView.findViewById(R.id.tvFullDescription);
            holder.tvPageTitle = (TextView) convertView.findViewById(R.id.tvPageTitle);
            holder.ivSuggestedPage = (ImageView) convertView.findViewById(R.id.ivSuggestedPage);
            holder.llPages = (LinearLayout) convertView.findViewById(R.id.llPages);
            holder.ivTaskCell = (ImageView) convertView.findViewById(R.id.ivTaskCell);
            holder.tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            holder.llTask = (LinearLayout) convertView.findViewById(R.id.llTask);
            holder.llCentredContent = (LinearLayout) convertView.findViewById(R.id.llImageCentredContent);
            holder.llImageCentredContent = (LinearLayout) convertView.findViewById(R.id.llCentredContent);
            holder.tvTitleCentredContent = (TextView) convertView.findViewById(R.id.tvTitleCentredContent);
            holder.tvDescriptionCentredContent = (TextView) convertView.findViewById(R.id.tvDescriptionCentredContent);
            holder.tvFullDescriptionCentredContent = (TextView) convertView.findViewById(R.id.tvFullDescriptionCentredContent);
            holder.ivCenteredContent = (ImageView) convertView.findViewById(R.id.ivCenteredContent);
            holder.llFullDescription = (LinearLayout) convertView.findViewById(R.id.llFullDescription);
            holder.llParentPages = (LinearLayout) convertView.findViewById(R.id.llParentPages);
            holder.ibResults = (ImageButton) convertView.findViewById(R.id.ibResults);
            holder.ibResults2 = (ImageButton) convertView.findViewById(R.id.ibResults2);
            holder.rlArrows = (RelativeLayout) convertView.findViewById(R.id.rlArrows);
            holder.vListDivider = (View) convertView.findViewById(R.id.vListDivider);
            holder.llParentTitle = (LinearLayout) convertView.findViewById(R.id.llParentTitle);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        final LiveSearchResultsModel liveSearchResultsModel = model.get(position);

        holder.ivSuggestedPage.setVisibility(View.GONE);
        holder.llPages.setVisibility(VISIBLE);
        holder.llTask.setVisibility(GONE);
        holder.rlArrows.setVisibility(VISIBLE);

        holder.ibResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liveSearchResultsModel.setExpanded(true);
                notifyDataSetChanged();
            }
        });

        holder.ibResults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liveSearchResultsModel.setExpanded(false);
                notifyDataSetChanged();
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
                holder.vListDivider.setVisibility(GONE);

                if(liveSearchResultsModel.getAppearance().getTemplate().getLayout() != null &&
                    !StringUtils.isNullOrEmpty(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getListType()) &&
                        liveSearchResultsModel.getAppearance().getTemplate().getLayout().getListType().equalsIgnoreCase(BundleConstants.PLAIN))
                    holder.llParentPages.setBackgroundColor(context.getResources().getColor(R.color.white));

                holder.tvTitle.setMaxLines(1);

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

                                if(liveSearchResultsModel.getAppearance().getTemplate().getLayout().getLayoutType().equalsIgnoreCase(BundleConstants.L_TWO))
                                {
                                    holder.tvDescription.setText(strBuilderDes);
                                    holder.tvFullDescription.setText(strBuilderDes);
                                }

                                holder.tvTitleCentredContent.setText(strBuilderDes);
                                holder.tvDescriptionCentredContent.setText(strBuilder);
                                holder.tvFullDescriptionCentredContent.setText(strBuilderDes);
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

                if(liveSearchResultsModel.getExpanded())
                {
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
                            holder.rlArrows.setVisibility(GONE);
                        }
                        break;
                    }

                    holder.ibResults.setVisibility(GONE);
                    holder.ibResults2.setVisibility(VISIBLE);
                }
                else
                {
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
                            holder.tvDescription.setMaxLines(2);

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
                        case BundleConstants.L_SIX:
                        {
                            holder.llParentTitle.setVisibility(VISIBLE);
                            holder.tvDescription.setVisibility(GONE);
                            holder.rlArrows.setVisibility(GONE);
                            holder.llFullDescription.setVisibility(VISIBLE);
                            holder.ivPageGridCell.setVisibility(VISIBLE);

                            holder.tvFullDescription.setMaxLines(2);
                        }
                        break;
                    }

                    holder.ibResults.setVisibility(VISIBLE);
                    holder.ibResults2.setVisibility(GONE);
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

        return convertView;
    }

    public class ViewHolder  {
        TextView tvTitle, tvDescription, tvFullDescription, tvPageTitle, tvTaskName, tvTitleCentredContent, tvDescriptionCentredContent, tvFullDescriptionCentredContent;
        ImageView ivPagesCell, ivPageGridCell, ivSuggestedPage, ivTaskCell, ivCenteredContent;
        LinearLayout llPages, llTask, llImageCentredContent, llCentredContent, llFullDescription, llParentPages, llParentTitle;
        ImageButton ibResults, ibResults2;
        RelativeLayout rlArrows;
        View vListDivider;
    }
}
