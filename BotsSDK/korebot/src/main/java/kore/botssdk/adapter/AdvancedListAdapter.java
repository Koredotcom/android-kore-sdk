package kore.botssdk.adapter;

import static android.view.View.GONE;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.AdvancedListModel;
import kore.botssdk.models.HeaderOptionsModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class AdvancedListAdapter extends BaseAdapter {

    private String LOG_TAG = BotListTemplateAdapter.class.getSimpleName();
    private ArrayList<AdvancedListModel> botListModelArrayList = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private LayoutInflater ownLayoutInflator;
    private Context context;
    private RoundedCornersTransform roundedCornersTransform;
    private ListView parentListView;
    private int count;
    private PayloadInner payloadInner;

    public AdvancedListAdapter(Context context, ListView parentListView) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
    }

    @Override
    public int getCount() {
        if (count != 0) {
            return botListModelArrayList.size() > count ? count : botListModelArrayList.size();
        }
        else
        {
            return botListModelArrayList.size();
        }
    }

    @Override
    public int getViewTypeCount() {
        if (count != 0) {
            return botListModelArrayList.size() > count ? count : botListModelArrayList.size();
        }
        else
        {
            return botListModelArrayList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public AdvancedListModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return botListModelArrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ownLayoutInflator.inflate(R.layout.advancedlist_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        AdvancedListModel botListModel = getItem(position);

        if(!StringUtils.isNullOrEmpty(botListModel.getIcon()))
        {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            try
            {
                String imageData;
                imageData = botListModel.getIcon();
                if (imageData.contains(","))
                {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.botListItemImage.setImageBitmap(decodedByte);
                }
                else
                {
                    Picasso.get().load(botListModel.getIcon()).transform(roundedCornersTransform).into(holder.botListItemImage);
                }
            } catch (Exception e) {
                holder.botListItemImage.setVisibility(GONE);
            }
        }

        if(botListModel.getHeaderOptions() != null && botListModel.getHeaderOptions().size() > 0)
        {
            holder.rlAction.setVisibility(View.VISIBLE);

            HeaderOptionsModel headerOptionsModel = botListModel.getHeaderOptions().get(0);

            if(!StringUtils.isNullOrEmpty(headerOptionsModel.getContenttype()))
            {
                if(headerOptionsModel.getContenttype().equalsIgnoreCase(BundleConstants.ICON))
                {
                    holder.ivAction.setVisibility(View.VISIBLE);
                    try {
                        String imageData;
                        imageData = botListModel.getHeaderOptions().get(0).getIcon();
                        if (imageData.contains(","))
                        {
                            imageData = imageData.substring(imageData.indexOf(",") + 1);
                            byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            holder.ivAction.setImageBitmap(decodedByte);
                        }
                        else
                        {
                            Picasso.get().load(botListModel.getIcon()).transform(roundedCornersTransform).into(holder.ivAction);
                        }
                    }
                    catch (Exception ex)
                    {
                        holder.ivAction.setVisibility(GONE);
                    }
                }
                else if(headerOptionsModel.getContenttype().equalsIgnoreCase(BundleConstants.BUTTON)
                        && !StringUtils.isNullOrEmpty(headerOptionsModel.getTitle()))
                {
                    holder.tvAction.setVisibility(View.VISIBLE);

                    String textualContent = unescapeHtml4(headerOptionsModel.getTitle().trim());
                    textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                    textualContent = MarkdownUtil.processMarkDown(textualContent);
                    CharSequence sequence = Html.fromHtml(textualContent.replace("\n", "<br />"),
                            new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                    holder.tvAction.setText(strBuilder);
                    holder.tvAction.setMovementMethod(null);

                    if(headerOptionsModel.getButtonStyles() != null)
                    {
                        GradientDrawable gradientDrawable = (GradientDrawable)holder.botListItemRoot.findViewById(R.id.tvAction).getBackground();
                        gradientDrawable.setCornerRadius(3 * dp1);
                        if(!StringUtils.isNullOrEmpty(headerOptionsModel.getButtonStyles().getBorderColor()))
                        {
                            if(!headerOptionsModel.getButtonStyles().getBorderColor().contains("#"))
                                gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor("#"+headerOptionsModel.getButtonStyles().getBorderColor()));
                            else
                                gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor(headerOptionsModel.getButtonStyles().getBorderColor()));
                        }
                        gradientDrawable.setColor(Color.parseColor(headerOptionsModel.getButtonStyles().getBackgroundColor()));

                        holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getButtonStyles().getColor()));
                    }

                    holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);

                    holder.tvAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(composeFooterInterface != null)
                                composeFooterInterface.onSendClick( holder.tvAction.getText().toString(), headerOptionsModel.getPayload(), true);
                        }
                    });
                }
                else if(!StringUtils.isNullOrEmpty(headerOptionsModel.getValue()))
                {
                    holder.tvAction.setVisibility(View.VISIBLE);

                    String textualContent = unescapeHtml4(headerOptionsModel.getValue().trim());
                    textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                    textualContent = MarkdownUtil.processMarkDown(textualContent);
                    CharSequence sequence = Html.fromHtml(textualContent.replace("\n", "<br />"),
                            new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                    holder.tvAction.setText(strBuilder);
                    holder.tvAction.setMovementMethod(null);

                    GradientDrawable gradientDrawable = (GradientDrawable)holder.botListItemRoot.findViewById(R.id.tvAction).getBackground();
                    gradientDrawable.setStroke(0, null);

                    if(headerOptionsModel.getStyles() != null)
                    {
                        holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getStyles().getColor()));
                    }

                    holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);
                }
            }
            else if(!StringUtils.isNullOrEmpty(headerOptionsModel.getType()))
            {
                if(headerOptionsModel.getType().equalsIgnoreCase(BundleConstants.ICON))
                {
                    holder.ivAction.setVisibility(View.VISIBLE);
                    try {
                        String imageData;
                        imageData = botListModel.getHeaderOptions().get(0).getIcon();
                        if (imageData.contains(","))
                        {
                            imageData = imageData.substring(imageData.indexOf(",") + 1);
                            byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            holder.ivAction.setImageBitmap(decodedByte);
                        }
                        else
                        {
                            Picasso.get().load(botListModel.getIcon()).transform(roundedCornersTransform).into(holder.ivAction);
                        }
                    }
                    catch (Exception ex)
                    {
                        holder.ivAction.setVisibility(GONE);
                    }
                }
                else if(headerOptionsModel.getType().equalsIgnoreCase(BundleConstants.BUTTON)
                        && !StringUtils.isNullOrEmpty(headerOptionsModel.getTitle()))
                {
                    holder.tvAction.setVisibility(View.VISIBLE);

                    String textualContent = unescapeHtml4(headerOptionsModel.getTitle().trim());
                    textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                    textualContent = MarkdownUtil.processMarkDown(textualContent);
                    CharSequence sequence = Html.fromHtml(textualContent.replace("\n", "<br />"),
                            new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                    holder.tvAction.setText(strBuilder);
                    holder.tvAction.setMovementMethod(null);

                    if(headerOptionsModel.getButtonStyles() != null)
                    {
                        GradientDrawable gradientDrawable = (GradientDrawable)holder.botListItemRoot.findViewById(R.id.tvAction).getBackground();
                        gradientDrawable.setCornerRadius(3 * dp1);
                        if(!StringUtils.isNullOrEmpty(headerOptionsModel.getButtonStyles().getBorderColor()))
                        {
                            if(!headerOptionsModel.getButtonStyles().getBorderColor().contains("#"))
                                gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor("#"+headerOptionsModel.getButtonStyles().getBorderColor()));
                            else
                                gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor(headerOptionsModel.getButtonStyles().getBorderColor()));
                        }
                        gradientDrawable.setColor(Color.parseColor(headerOptionsModel.getButtonStyles().getBackgroundColor()));

                        holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getButtonStyles().getColor()));
                    }

                    holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);

                }
                else if(!StringUtils.isNullOrEmpty(headerOptionsModel.getValue()))
                {
                    holder.tvAction.setVisibility(View.VISIBLE);

                    String textualContent = unescapeHtml4(headerOptionsModel.getValue().trim());
                    textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                    textualContent = MarkdownUtil.processMarkDown(textualContent);
                    CharSequence sequence = Html.fromHtml(textualContent.replace("\n", "<br />"),
                            new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                    holder.tvAction.setText(strBuilder);
                    holder.tvAction.setMovementMethod(null);
                    holder.tvAction.setBackground(null);

                    if(headerOptionsModel.getStyles() != null)
                    {
                        holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getStyles().getColor()));
                    }

                    holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);
                }
            }
        }

        LayerDrawable layerDrawable = (LayerDrawable) holder.botListItemRoot.getContext().getResources().getDrawable(R.drawable.advanced_cell_bg);

        if(botListModel.getElementStyles() != null)
        {
            String border[] = botListModel.getElementStyles().getBorderWidth().split(" ");

            if(border.length > 3)
            {
                String border0 = border[0].replaceAll("[^0-9]", "");
                String border1 = border[1].replaceAll("[^0-9]", "");
                String border2 = border[2].replaceAll("[^0-9]", "");
                String border3 = border[3].replaceAll("[^0-9]", "");
                layerDrawable.setLayerInset(1, Integer.parseInt(border0) * (int)dp1, Integer.parseInt(border1)* (int)dp1, Integer.parseInt(border3)* (int)dp1, Integer.parseInt(border2)* (int)dp1);
            }

            if(!StringUtils.isNullOrEmpty(botListModel.getElementStyles().getBorder()))
            {
                String color[] = botListModel.getElementStyles().getBorder().split(" ");
                GradientDrawable backgroundColor = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.item_bottom_stroke);
                backgroundColor.setColor(Color.parseColor(color[1]));
                GradientDrawable bagColor = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.item_navbar_background);

                if(!StringUtils.isNullOrEmpty(botListModel.getElementStyles().getBorderRadius()))
                {
                    String radius = botListModel.getElementStyles().getBorderRadius().replaceAll("[^0-9]", "");

                    if(!StringUtils.isNullOrEmpty(radius)) {
                        backgroundColor.setCornerRadius((Integer.parseInt(radius) * 2));
                        bagColor.setCornerRadius((Integer.parseInt(radius) * 2));
                    }
                }
            }
        }

        holder.botListItemRoot.setBackground(layerDrawable);

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        if(!StringUtils.isNullOrEmpty(botListModel.getDescription())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getDescription());

            if(botListModel.getDescriptionStyles() != null)
            {
                holder.botListItemSubtitle.setTextColor(Color.parseColor(botListModel.getDescriptionStyles().getColor()));
            }
        }
//        if (botListModel.getButtons() == null || botListModel.getButtons().isEmpty()) {
//            holder.botListItemButton.setVisibility(View.GONE);
//        } else {
//            holder.botListItemButton.setVisibility(View.VISIBLE);
//            holder.botListItemButton.setText(botListModel.getButtons().get(0).getTitle());
//            holder.botListItemButton.setTag(botListModel.getButtons().get(0));
//
//            holder.botListItemButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
//                        BotListElementButton botListElementButton = (BotListElementButton) v.getTag();
//                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListElementButton.getType())) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(botListElementButton.getUrl());
//                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botListElementButton.getType())) {
//                            String listElementButtonPayload = botListElementButton.getPayload();
//                            String listElementButtonTitle = botListElementButton.getTitle();
//                            composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload,false);
//                        }
//                    }
//                }
//            });
//        }
        holder.botListItemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    int position = parentListView.getPositionForView(v);
                    AdvancedListModel _botListModel = getItem(position);
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getType())) {
                            String listElementButtonPayload = _botListModel.getPayload();
                            String listElementButtonTitle = _botListModel.getTitle();
                            composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload,false);
                        }
                    }
                }
            }
        });
    }

    public void dispalyCount(int count)
    {
        this.count = count;
    }

    public void setPayloadInner(PayloadInner payloadInner)
    {
        this.payloadInner = payloadInner;
    }

    public void setBotListModelArrayList(ArrayList<AdvancedListModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();

        holder.botListItemRoot = (RelativeLayout) view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = (ImageView) view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = (TextView) view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = (TextView) view.findViewById(R.id.bot_list_item_subtitle);
        holder.botListItemButton = (Button) view.findViewById(R.id.bot_list_item_button);
        holder.ivAction = (ImageView) view.findViewById(R.id.ivAction);
        holder.tvAction = (TextView) view.findViewById(R.id.tvAction);
        holder.rlAction = (RelativeLayout) view.findViewById(R.id.rlAction);

        view.setTag(holder);
    }

    private static class ViewHolder {
        RelativeLayout botListItemRoot, rlAction;
        ImageView botListItemImage, ivAction;
        TextView botListItemTitle, tvAction;
        TextView botListItemSubtitle;
        Button botListItemButton;
    }
}
