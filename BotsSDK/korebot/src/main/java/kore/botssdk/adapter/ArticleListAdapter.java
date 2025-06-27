package kore.botssdk.adapter;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.util.Base64;
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

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.ArticleModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private final ArrayList<ArticleModel> articles;
    private final boolean isEnabled;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public ArticleListAdapter(ArrayList<ArticleModel> articles, boolean isEnabled) {
        this.articles = articles;
        this.isEnabled = isEnabled;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_cell, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArticleModel articleModel = articles.get(position);
        holder.botListItemImage.setVisibility(!StringUtils.isNullOrEmpty(articleModel.getIcon()) ? View.VISIBLE : View.GONE);
        if (!StringUtils.isNullOrEmpty(articleModel.getIcon())) {
            String imageData = articleModel.getIcon();
            if (imageData.contains(",")) {
                imageData = imageData.substring(imageData.indexOf(",") + 1);
                byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.botListItemImage.setImageBitmap(decodedByte);
            } else {
                Picasso.get().load(articleModel.getIcon()).transform(new RoundedCornersTransform()).into(holder.botListItemImage);
            }
        }

        String textualContent = unescapeHtml4(articleModel.getTitle().trim());
        textualContent = StringUtils.unescapeHtml3(textualContent.trim());
        textualContent = MarkdownUtil.processMarkDown(textualContent);
        CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY, new MarkdownImageTagHandler(holder.itemView.getContext(), holder.tvAction, textualContent), new MarkdownTagHandler());
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        holder.botListItemTitle.setText(strBuilder);
        holder.botListItemTitle.setMovementMethod(null);
        holder.botListItemSubtitle.setVisibility(!StringUtils.isNullOrEmpty(articleModel.getDescription()) ? View.VISIBLE : View.GONE);
        holder.botListItemSubtitle.setText(articleModel.getDescription());
        holder.llCreatedOn.setVisibility(!StringUtils.isNullOrEmpty(articleModel.getCreatedOn()) ? View.VISIBLE : View.GONE);
        holder.tvCreatedUpdatedOn.setText(String.join("\n", articleModel.getCreatedOn(), articleModel.getUpdatedOn()));
        holder.botListItemButton.setVisibility(articleModel.getButton() != null ? View.VISIBLE : View.GONE);
        holder.botListItemButton.setText(articleModel.getButton().getTitle());
        holder.botListItemButton.setOnClickListener(view -> {
            if (articleModel.getButton() == null) return;
            String type = articleModel.getButton().getType();
            if (invokeGenericWebViewInterface != null && (type.equalsIgnoreCase(BundleConstants.BUTTON_TYPE_URL) || type.equalsIgnoreCase(BundleConstants.BUTTON_TYPE_WEB_URL))) {
                invokeGenericWebViewInterface.invokeGenericWebView(articleModel.getButton().getUrl());
            } else if (isEnabled && composeFooterInterface != null && type.equalsIgnoreCase(BundleConstants.BUTTON_TYPE_POSTBACK)) {
                composeFooterInterface.onSendClick(articleModel.getButton().getTitle(), articleModel.getButton().getTitle(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles != null ? articles.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView botListItemImage;
        TextView botListItemTitle;
        TextView tvAction;
        TextView botListItemSubtitle;
        TextView tvCreatedUpdatedOn;
        Button botListItemButton;
        LinearLayout llCreatedOn;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            botListItemImage = itemView.findViewById(R.id.bot_list_item_image);
            botListItemTitle = itemView.findViewById(R.id.bot_list_item_title);
            tvAction = itemView.findViewById(R.id.tvAction);
            botListItemSubtitle = itemView.findViewById(R.id.bot_list_item_subtitle);
            tvCreatedUpdatedOn = itemView.findViewById(R.id.tvCreatedUpdatedOn);
            botListItemButton = itemView.findViewById(R.id.bot_list_item_button);
            llCreatedOn = itemView.findViewById(R.id.llCreatedOn);

            KaFontUtils.applyCustomFont(this.itemView.getContext(), this.itemView);
        }
    }
}
