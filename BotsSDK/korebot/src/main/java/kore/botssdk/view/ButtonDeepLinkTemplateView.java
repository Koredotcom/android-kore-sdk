package kore.botssdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ButtonLinkAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.RadioListListner;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;

public class ButtonDeepLinkTemplateView extends LinearLayout implements RadioListListner
{
    private View view;
    private RecyclerView lvButtonLink;
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int dp1;
    private PayloadInner payloadInner;

    public ButtonDeepLinkTemplateView(Context context) {
        super(context);
        init(context);
    }

    public ButtonDeepLinkTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ButtonDeepLinkTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        view =  LayoutInflater.from(getContext()).inflate(R.layout.button_deep_link_view, this, true);
        lvButtonLink = view.findViewById(R.id.lvButtonLink);

        dp1 = (int) AppControl.getInstance().getDimensionUtil().density;
//        lvButtonLink.addItemDecoration(new VerticalSpaceItemDecoration(10 * dp1));
        KaFontUtils.applyCustomFont(getContext(), view);
    }

    public void populateButtonDeepLinkView(PayloadInner payloadInn, boolean isLastItem)
    {
        if (payloadInn != null && payloadInn.getButtons() != null && payloadInn.getButtons().size() > 0)
        {
            this.payloadInner = payloadInn;
            ArrayList<BotButtonModel> buttonLinkTemplateModels = payloadInner.getButtons();

            if(buttonLinkTemplateModels.size() == 3 && isLastItem)
            {
                for (int i = 0; i < buttonLinkTemplateModels.size(); i++)
                {
                    if(i == buttonLinkTemplateModels.size()-1)
                    {
                        BotButtonModel quickReplyTemplate = buttonLinkTemplateModels.get(i);
                        buttonLinkTemplateModels.remove(i);
                        buttonLinkTemplateModels.add(1, quickReplyTemplate);
                    }
                }
            }

            switch (getQuickRepliesType(payloadInner.getButtons()))
            {
                case 1:
                    staggeredGridLayoutManager = new StaggeredGridLayoutManager((buttonLinkTemplateModels.size()/2) + (buttonLinkTemplateModels.size() % 2), LinearLayoutManager.HORIZONTAL);
                    lvButtonLink.setLayoutManager(staggeredGridLayoutManager);
                    break;
                case 2:
                case 3:
                    staggeredGridLayoutManager = new StaggeredGridLayoutManager(buttonLinkTemplateModels.size(), LinearLayoutManager.HORIZONTAL);
                    lvButtonLink.setLayoutManager(staggeredGridLayoutManager);
                    break;
            }

            lvButtonLink.setAdapter(new ButtonLinkAdapter(getContext(), buttonLinkTemplateModels, invokeGenericWebViewInterface, payloadInner.getCheckedPosition(), composeFooterInterface, ButtonDeepLinkTemplateView.this));
        }
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private int getQuickRepliesType(ArrayList<BotButtonModel> quickReplyTemplates)
    {
        int length = getLongestString(quickReplyTemplates);

        if(length > 40)
            return 3;
        else if(length > 16)
            return 2;
        else
            return 1;
    }

    public static int getLongestString(ArrayList<BotButtonModel> quickReplyTemplates) {
        int maxLength = 0;
        for (BotButtonModel s : quickReplyTemplates) {
            if (s.getTitle().length() > maxLength) {
                maxLength = s.getTitle().length();
            }
        }
        return maxLength;
    }

    @Override
    public void radioItemClicked(int position) {
        if(payloadInner != null)
            payloadInner.setCheckedPosition(position);
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
