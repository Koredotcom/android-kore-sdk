package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.adapter.ArticleListAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class ArticleTemplateView extends LinearLayout {

    String LOG_TAG = BotListTemplateView.class.getSimpleName();

    float dp1, layoutItemHeight = 0;
    AutoExpandListView autoExpandListView;
    TextView botCustomListViewButton, botListViewTitle;
    LinearLayout botCustomListRoot;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public ArticleTemplateView(Context context) {
        super(context);
        init();
    }

    public ArticleTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArticleTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.article_list_view, this, true);
        botCustomListRoot = (LinearLayout) findViewById(R.id.botCustomListRoot);
        autoExpandListView = (AutoExpandListView) findViewById(R.id.botCustomListView);
        botCustomListViewButton = (TextView) findViewById(R.id.botCustomListViewButton);
        botListViewTitle = (TextView) findViewById(R.id.botListViewTitle);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        layoutItemHeight = getResources().getDimension(R.dimen.list_item_view_height);

    }

    public void populateArticleListTemplateView(PayloadInner payloadInner) {
        if (payloadInner != null) {
            if (!StringUtils.isNullOrEmpty(payloadInner.getTitle())) {
                botListViewTitle.setVisibility(VISIBLE);
                botListViewTitle.setText(payloadInner.getTitle());
            }

            if (payloadInner.getArticleListModels() != null && payloadInner.getArticleListModels().size() > 0) {
                ArticleListAdapter botListTemplateAdapter;
                if (autoExpandListView.getAdapter() == null) {
                    botListTemplateAdapter = new ArticleListAdapter(getContext(), autoExpandListView);
                    autoExpandListView.setAdapter(botListTemplateAdapter);
                    botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
                    botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                } else {
                    botListTemplateAdapter = (ArticleListAdapter) autoExpandListView.getAdapter();
                }
                botListTemplateAdapter.dispalyCount(payloadInner.getListItemDisplayCount());
                botListTemplateAdapter.setPayloadInner(payloadInner);
                botListTemplateAdapter.setBotListModelArrayList(payloadInner.getArticleListModels());
                botListTemplateAdapter.notifyDataSetChanged();
                botCustomListRoot.setVisibility(VISIBLE);
//            if(botButtonModelArrayList != null && botButtonModelArrayList.size() > 0) {
//                botCustomListViewButton.setText(botButtonModelArrayList.get(0).getTitle());
//                botCustomListViewButton.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
//                            BotButtonModel botButtonModel = botButtonModelArrayList.get(0);
//                            if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botButtonModel.getType())) {
//                                invokeGenericWebViewInterface.invokeGenericWebView(botButtonModel.getUrl());
//                            } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botButtonModel.getType())) {
//                                String payload = botButtonModel.getPayload();
//                                String message = botCustomListViewButton.getText().toString();
//                                composeFooterInterface.onSendClick(message, payload, false);
//                            }
//                        }
//                    }
//                });
//                botCustomListViewButton.setVisibility(botListModelArrayList.size() > 3 ? VISIBLE : GONE);
//            }
            } else {
                botCustomListRoot.setVisibility(GONE);
                botCustomListViewButton.setVisibility(GONE);
            }
        }
    }

    //    public void setRestrictedMaxHeight(float restrictedMaxHeight) {
//        this.restrictedMaxHeight = restrictedMaxHeight;
//    }
//
    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
        post(() -> {
            LinearLayout.LayoutParams params = (LayoutParams) botCustomListRoot.getLayoutParams();
            params.width = (int) this.restrictedMaxWidth;
            setLayoutParams(params);
        });
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
