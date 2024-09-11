package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotBeneficiaryTemplateAdapter;
import kore.botssdk.dialogs.ListActionSheetFragment;
import kore.botssdk.listener.ListClickableListener;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotBeneficiaryModel;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotListViewMoreDataModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.view.AutoExpandListView;

public class BeneficiaryTemplateHolder extends BaseViewHolder implements ListClickableListener {

    private final AutoExpandListView autoExpandListView;
    private final TextView botCustomListViewButton;
    private final LinearLayout botCustomListRoot;
    private PayloadInner payloadInner;

    public static BeneficiaryTemplateHolder getInstance(ViewGroup parent) {
        return new BeneficiaryTemplateHolder(createView(R.layout.bot_custom_list_view_template, parent));
    }

    private BeneficiaryTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());

        botCustomListRoot = itemView.findViewById(R.id.botCustomListRoot);
        autoExpandListView = itemView.findViewById(R.id.botCustomListView);
        botCustomListViewButton = itemView.findViewById(R.id.botCustomListViewButton);
        KaFontUtils.applyCustomFont(itemView.getContext(), itemView);

        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        String quickWidgetColor = SDKConfiguration.BubbleColors.quickReplyColor;
        String quickReplyFontColor = SDKConfiguration.BubbleColors.quickReplyTextColor;
        String fillColor = SDKConfiguration.BubbleColors.quickReplyColor;

        fillColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, fillColor);
        quickWidgetColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, quickWidgetColor);
        quickReplyFontColor = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, quickReplyFontColor);

        botCustomListViewButton.setTextColor(Color.parseColor(fillColor));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        BotListViewMoreDataModel botListViewMoreDataModel = payloadInner.getMoreData();
        ArrayList<BotBeneficiaryModel> botListModelArrayList = payloadInner.getBotBeneficiaryModels();
        ArrayList<BotButtonModel> botButtonModelArrayList = payloadInner.getButtons();
        int moreCount = payloadInner.getMoreCount();

        if (botListViewMoreDataModel != null)
            LogUtils.e("More Data", botListViewMoreDataModel.getTab1().toString());

        if (botListModelArrayList != null && botListModelArrayList.size() > 0) {
            this.payloadInner = payloadInner;
            itemView.setAlpha((payloadInner.isIs_end() ? 0.4f : 1.0f));

            BotBeneficiaryTemplateAdapter botListTemplateAdapter = null;
            if (moreCount != 0 && botListModelArrayList.size() > moreCount)
                botListTemplateAdapter = new BotBeneficiaryTemplateAdapter(itemView.getContext(), moreCount);
            else
                botListTemplateAdapter = new BotBeneficiaryTemplateAdapter(itemView.getContext(), botListModelArrayList.size());

            autoExpandListView.setAdapter(botListTemplateAdapter);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            botListTemplateAdapter.setListClickableInterface(BeneficiaryTemplateHolder.this);
            botListTemplateAdapter.setBotListModelArrayList(botListModelArrayList);
            botListTemplateAdapter.setListClickable(payloadInner.isIs_end());
            botListTemplateAdapter.notifyDataSetChanged();

            botCustomListRoot.setVisibility(VISIBLE);
            if (botButtonModelArrayList != null && botButtonModelArrayList.size() > 0) {
                botCustomListViewButton.setText(Html.fromHtml(botButtonModelArrayList.get(0).getTitle()));
                botCustomListViewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListActionSheetFragment bottomSheetDialog = new ListActionSheetFragment();
                        bottomSheetDialog.setIsFromFullView(false);
                        bottomSheetDialog.setSkillName("skillName", "trigger");
//                        bottomSheetDialog.setData(botListModelArrayList);
                        bottomSheetDialog.setHeaderVisible(true);
                        bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                        bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                        bottomSheetDialog.show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), "add_tags");
                    }
                });


                botCustomListViewButton.setVisibility(botListModelArrayList.size() > moreCount ? VISIBLE : GONE);
            } else {
                if (moreCount != 0) {
                    botCustomListViewButton.setVisibility(botListModelArrayList.size() > moreCount ? VISIBLE : GONE);
                    botCustomListViewButton.setText(Html.fromHtml(itemView.getResources().getString(R.string.show_more)));
                }

            }
        } else {
            botCustomListRoot.setVisibility(GONE);
            botCustomListViewButton.setVisibility(GONE);
        }
    }

    @Override
    public void listClicked(boolean isListClicked) {
        if (payloadInner != null)
            payloadInner.setIs_end(true);
    }
}