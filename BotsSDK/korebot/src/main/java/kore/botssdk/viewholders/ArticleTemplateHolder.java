package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.ArticleListAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class ArticleTemplateHolder extends BaseViewHolder {
    public static ArticleTemplateHolder getInstance(ViewGroup parent) {
        return new ArticleTemplateHolder(createView(R.layout.template_article, parent));
    }

    public ArticleTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        TextView tvTitle = itemView.findViewById(R.id.botListViewTitle);
        RecyclerView recyclerView = itemView.findViewById(R.id.recycler_view);

        PayloadInner payloadInner = getPayloadInner(baseBotMessage);

        if (payloadInner == null) return;

        tvTitle.setVisibility(!StringUtils.isNullOrEmpty(payloadInner.getTitle()) ? VISIBLE : GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        ArticleListAdapter adapter = new ArticleListAdapter(payloadInner.getArticleModels(), isLastItem());
        adapter.setComposeFooterInterface(composeFooterInterface);
        adapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        recyclerView.setAdapter(adapter);
    }
}
