package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ListViewTemplateAdapter;
import kore.botssdk.dialogs.ListActionSheetFragment;
import kore.botssdk.dialogs.ListMoreActionSheetFragment;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.BotListModel;
import kore.botssdk.models.BotListViewMoreDataModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.LogUtils;

public class ListViewTemplateHolder extends BaseViewHolder {
    public static ListViewTemplateHolder getInstance(ViewGroup parent) {
        return new ListViewTemplateHolder(createView(R.layout.template_list_view, parent));
    }

    private ListViewTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        LinearLayoutCompat layoutBubble = itemView.findViewById(R.id.layoutBubble);
        initBubbleText(layoutBubble, false);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        BotListViewMoreDataModel botListViewMoreDataModel = payloadInner.getMoreData();
        if (botListViewMoreDataModel != null) LogUtils.e("More Data", botListViewMoreDataModel.getTab1().toString());
        ArrayList<BotListModel> listElements = payloadInner.getListElements();
        ArrayList<BotButtonModel> botButtonModelArrayList = payloadInner.getButtons();
        int moreCount = payloadInner.getMoreCount();
        Context context = itemView.getContext();
        RecyclerView recyclerView = itemView.findViewById(R.id.botCustomListView);
        TextView botCustomListViewButton = itemView.findViewById(R.id.botCustomListViewButton);
        TextView title = itemView.findViewById(R.id.title);
        ViewGroup botCustomListRoot = itemView.findViewById(R.id.botCustomListRoot);
        setResponseText(itemView.findViewById(R.id.layoutBubble), payloadInner.getText(), baseBotMessage.getTimeStamp());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        String heading = payloadInner.getHeading();
        title.setText(heading);
        title.setVisibility(heading != null && !heading.isEmpty() ? VISIBLE : GONE);
        if (listElements != null && !listElements.isEmpty()) {
            int size = moreCount != 0 && listElements.size() > moreCount ? moreCount : listElements.size();
            ListViewTemplateAdapter botListTemplateAdapter = new ListViewTemplateAdapter(context, listElements, isLastItem(), size);
            botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            recyclerView.setAdapter(botListTemplateAdapter);
            if (isLastItem()) botListTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            botListTemplateAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            if (botButtonModelArrayList != null && !botButtonModelArrayList.isEmpty()) {
                botCustomListViewButton.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));
                botCustomListViewButton.setText(Html.fromHtml("<u>" + botButtonModelArrayList.get(0).getTitle() + "</u>"));
                botCustomListViewButton.setOnClickListener(v -> {
                    ListActionSheetFragment bottomSheetDialog = new ListActionSheetFragment();
                    bottomSheetDialog.setIsFromFullView(false);
                    bottomSheetDialog.setSkillName("skillName", "trigger");
                    bottomSheetDialog.setData(botListViewMoreDataModel);
                    bottomSheetDialog.setHeaderVisible(true);
                    if (isLastItem()) bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                    bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    bottomSheetDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "add_tags");
                });

                botCustomListViewButton.setVisibility(listElements.size() > moreCount ? VISIBLE : GONE);
            } else {
                if (moreCount != 0) {
                    botCustomListViewButton.setVisibility(listElements.size() > moreCount ? VISIBLE : GONE);
                    botCustomListViewButton.setText(Html.fromHtml("<u>" + context.getResources().getString(R.string.show_more) + "</u>"));
                }

                botCustomListViewButton.setOnClickListener(v -> {
                    ListMoreActionSheetFragment bottomSheetDialog = new ListMoreActionSheetFragment();
                    bottomSheetDialog.setisFromFullView(false);
                    bottomSheetDialog.setSkillName("skillName", "trigger");
                    bottomSheetDialog.setData(payloadInner.getText(), listElements);
                    if (isLastItem()) bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                    bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    bottomSheetDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "add_tags");
                });
            }
        } else {
            botCustomListRoot.setVisibility(GONE);
            botCustomListViewButton.setVisibility(GONE);
        }
    }
}