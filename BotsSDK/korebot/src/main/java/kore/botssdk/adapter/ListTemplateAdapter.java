package kore.botssdk.adapter;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotListElementButton;
import kore.botssdk.models.BotListModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.viewUtils.RoundedCornersTransform;

public class ListTemplateAdapter extends RecyclerView.Adapter<ListTemplateAdapter.ViewHolder> {
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    final boolean isEnabled;
    final RoundedCornersTransform roundedCornersTransform = new RoundedCornersTransform();
    final List<BotListModel> botListModels;
    Context context;

    public ListTemplateAdapter(Context context, List<BotListModel> botListModels, boolean isEnabled) {
        this.botListModels = botListModels;
        this.isEnabled = isEnabled;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_list_template_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BotListModel botListModel = getItem(position);
        if (botListModel == null) return;
        if (!StringUtils.isNullOrEmpty(botListModel.getImage_url())) {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            Picasso.get().load(botListModel.getImage_url()).transform(roundedCornersTransform).into(holder.botListItemImage);
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        if (!StringUtils.isNullOrEmpty(botListModel.getSubtitle())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);

            String textualContent = unescapeHtml4(botListModel.getSubtitle());
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
            textualContent = MarkdownUtil.processMarkDown(textualContent);
            CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY, new MarkdownImageTagHandler(context, holder.botListItemSubtitle, textualContent), new MarkdownTagHandler());
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

            holder.botListItemSubtitle.setText(strBuilder);
        }
        if (botListModel.getButtons() == null || botListModel.getButtons().isEmpty()) {
            holder.botListItemButton.setVisibility(View.GONE);
        } else {
            holder.botListItemButton.setVisibility(View.VISIBLE);
            holder.botListItemButton.setText(botListModel.getButtons().get(0).getTitle());
            holder.botListItemButton.setTag(botListModel.getButtons().get(0));

            holder.botListItemButton.setOnClickListener(v -> {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    BotListElementButton botListElementButton = (BotListElementButton) v.getTag();
                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListElementButton.getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(botListElementButton.getUrl());
                    } else if (isEnabled && BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botListElementButton.getType())) {
                        String listElementButtonPayload = botListElementButton.getPayload();
                        String listElementButtonTitle = botListElementButton.getTitle();
                        composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload, false);
                    }
                }
            });
        }
        holder.botListItemRoot.setOnClickListener(v -> {
            if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                if (botListModel.getDefault_action() != null) {
                    if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botListModel.getDefault_action().getType())) {
                        invokeGenericWebViewInterface.invokeGenericWebView(botListModel.getDefault_action().getUrl());
                    } else if (isEnabled && BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botListModel.getDefault_action().getType())) {
                        composeFooterInterface.onSendClick(botListModel.getDefault_action().getTitle(), botListModel.getDefault_action().getPayload(), false);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return botListModels != null ? botListModels.size() : 0;
    }

    private BotListModel getItem(int position) {
        return botListModels != null ? botListModels.get(position) : null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout botListItemRoot;
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView botListItemSubtitle;
        Button botListItemButton;

        public ViewHolder(@NonNull View view) {
            super(view);
            botListItemRoot = view.findViewById(R.id.bot_list_item_root);
            botListItemImage = view.findViewById(R.id.bot_list_item_image);
            botListItemTitle = view.findViewById(R.id.bot_list_item_title);
            botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);
            botListItemButton = view.findViewById(R.id.bot_list_item_button);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}