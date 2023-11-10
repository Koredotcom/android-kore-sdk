package kore.botssdk.adapter;

import static android.view.View.GONE;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.ArticleListModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.Widget;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class ArticleListAdapter extends BaseAdapter {

    private String LOG_TAG = BotListTemplateAdapter.class.getSimpleName();
    private ArrayList<ArticleListModel> botListModelArrayList = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private LayoutInflater ownLayoutInflator;
    private Context context;
    private RoundedCornersTransform roundedCornersTransform;
    private ListView parentListView;
    private int count;
    private PayloadInner payloadInner;

    public ArticleListAdapter(Context context, ListView parentListView) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
    }

    @Override
    public int getCount() {
        if (count != 0) {
            return botListModelArrayList.size() > count ? count : botListModelArrayList.size();
        } else {
            return botListModelArrayList.size();
        }
    }

    @Override
    public ArticleListModel getItem(int position) {
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
            convertView = ownLayoutInflator.inflate(R.layout.article_list_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        ArticleListModel botListModel = getItem(position);

        if (!StringUtils.isNullOrEmpty(botListModel.getIcon())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            try {
                String imageData;
                imageData = botListModel.getIcon();
                if (imageData.contains(",")) {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.botListItemImage.setImageBitmap(decodedByte);
                    if (decodedByte == null) holder.botListItemImage.setVisibility(GONE);
                } else {
                    Picasso.get().load(botListModel.getIcon()).transform(roundedCornersTransform).into(holder.botListItemImage);
                }
            } catch (Exception e) {
                holder.botListItemImage.setVisibility(GONE);
            }
        }

        holder.botListItemTitle.setTag(botListModel);

        if (!StringUtils.isNullOrEmpty(botListModel.getTitle())) {
            String textualContent = unescapeHtml4(botListModel.getTitle().trim());
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
            textualContent = MarkdownUtil.processMarkDown(textualContent);
            CharSequence sequence = Html.fromHtml(textualContent.replace("\n", "<br />"),
                    new MarkdownImageTagHandler(context, holder.tvAction, textualContent), new MarkdownTagHandler());
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

            holder.botListItemTitle.setText(strBuilder);
            holder.botListItemTitle.setMovementMethod(null);
        }

        if (!StringUtils.isNullOrEmpty(botListModel.getDescription())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getDescription());
        }

        if (!StringUtils.isNullOrEmpty(botListModel.getCreatedOn()) && !StringUtils.isNullOrEmpty(botListModel.getUpdatedOn())) {
            holder.llCreatedOn.setVisibility(View.VISIBLE);
            holder.tvCreatedUpdatedOn.setText(botListModel.getCreatedOn() + "\n" + botListModel.getUpdatedOn());
        }

        if (botListModel.getButton() == null) {
            holder.botListItemButton.setVisibility(View.GONE);
        } else {
            holder.botListItemButton.setVisibility(View.VISIBLE);
            holder.botListItemButton.setText(botListModel.getButton().getTitle());
            holder.botListItemButton.setTag(botListModel.getButton());

            holder.botListItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        Widget.Button botListElementButton = (Widget.Button) v.getTag();
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListElementButton.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botListElementButton.getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_URL.equalsIgnoreCase(botListElementButton.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botListElementButton.getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botListElementButton.getType())) {
                            String listElementButtonPayload = botListElementButton.getTitle();
                            String listElementButtonTitle = botListElementButton.getTitle();
                            composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload, false);
                        }
                    }
                }
            });
        }
//        holder.botListItemRoot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
//                    int position = parentListView.getPositionForView(v);
//                    ArticleListModel _botListModel = getItem(position);
//                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
//                        if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getType())) {
//                            String listElementButtonPayload = _botListModel.getPayload();
//                            String listElementButtonTitle = _botListModel.getTitle();
//                            composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload,false);
//                        }
//                    }
//                }
//            }
//        });
    }

    public void dispalyCount(int count) {
        this.count = count;
    }

    public void setPayloadInner(PayloadInner payloadInner) {
        this.payloadInner = payloadInner;
    }

    public void setBotListModelArrayList(ArrayList<ArticleListModel> botListModelArrayList) {
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

        holder.botListItemRoot = view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = (ImageView) view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = (TextView) view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = (TextView) view.findViewById(R.id.bot_list_item_subtitle);
        holder.botListItemButton = (Button) view.findViewById(R.id.bot_list_item_button);
        holder.ivAction = (ImageView) view.findViewById(R.id.ivAction);
        holder.tvAction = (TextView) view.findViewById(R.id.tvAction);
        holder.rlAction = (RelativeLayout) view.findViewById(R.id.rlAction);
        holder.tvCreatedUpdatedOn = (TextView) view.findViewById(R.id.tvCreatedUpdatedOn);
        holder.llCreatedOn = (LinearLayout) view.findViewById(R.id.llCreatedOn);

        view.setTag(holder);
    }

    private static class ViewHolder {
        RelativeLayout botListItemRoot, rlAction;
        ImageView botListItemImage, ivAction;
        TextView botListItemTitle, tvAction;
        TextView botListItemSubtitle, tvCreatedUpdatedOn;
        Button botListItemButton;
        LinearLayout llCreatedOn;
    }
}
