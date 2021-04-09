package kore.botssdk.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotFormTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotFormTemplateModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

public class BotFormTemplateView extends ViewGroup {
    private AutoExpandListView autoExpandListView;
    private View multiSelectLayout;
    private BotFormTemplateAdapter botFormTemplateAdapter;
    private float dp1;
    private TextView tvform_template_title;
    private TextView btfieldButton;
    private SharedPreferences sharedPreferences;
    private String leftbgColor, leftTextColor;
    private GradientDrawable leftDrawable;

    public BotFormTemplateView(Context context) {
        super(context);
        init(context);
    }

    public BotFormTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BotFormTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;


    private void init(Context context) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_templete_view, this, true);
        autoExpandListView = view.findViewById(R.id.multi_select_list);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        KaFontUtils.applyCustomFont(getContext(), view);
        multiSelectLayout = view.findViewById(R.id.multi_select_layout);
        tvform_template_title = view.findViewById(R.id.tvform_template_title);
        btfieldButton = (TextView) view.findViewById(R.id.btfieldButton);
        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;

        sharedPreferences = getSharedPreferences(context);
        leftbgColor= sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#EBEBEB");
        leftTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000");

        leftDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.theme1_left_bubble_bg);
        leftDrawable.setColor(Color.parseColor(leftbgColor));
        leftDrawable.setStroke((int) (1*dp1), Color.parseColor(leftbgColor));
        multiSelectLayout.setBackground(leftDrawable);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) (parentWidth - 28 * dp1), MeasureSpec.EXACTLY);
        MeasureUtils.measure(multiSelectLayout, childWidthSpec, wrapSpec);

        totalHeight += multiSelectLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(multiSelectLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);
    }

    private SharedPreferences getSharedPreferences(Context context)
    {
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void populateData(final PayloadInner payloadInner, boolean isEnabled) {

        if (payloadInner != null) {
            multiSelectLayout.setVisibility(VISIBLE);
            ArrayList<BotFormTemplateModel> items = new ArrayList<>();

            if(payloadInner.getBotFormTemplateModels() != null && payloadInner.getBotFormTemplateModels().size()>0)
                items.addAll(payloadInner.getBotFormTemplateModels());

            tvform_template_title.setText(payloadInner.getHeading());
            botFormTemplateAdapter = new BotFormTemplateAdapter(getContext(), payloadInner.getBotFormTemplateModels());
            botFormTemplateAdapter.setBotFormTemplates(items);
            botFormTemplateAdapter.setEnabled(isEnabled);
            botFormTemplateAdapter.setTextColor(leftTextColor);
            botFormTemplateAdapter.setComposeFooterInterface(composeFooterInterface);
            autoExpandListView.setAdapter(botFormTemplateAdapter);
            botFormTemplateAdapter.notifyDataSetChanged();

            if(!StringUtils.isNullOrEmpty(leftTextColor))
            {
                tvform_template_title.setTextColor(Color.parseColor(leftTextColor));
                btfieldButton.setTextColor(Color.parseColor(leftTextColor));
            }

            if(payloadInner.getFieldButton() != null)
                if(!StringUtils.isNullOrEmpty(payloadInner.getFieldButton().getTitle()))
                    btfieldButton.setText(payloadInner.getFieldButton().getTitle());

            btfieldButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (composeFooterInterface != null && isEnabled)
                    {
                        int listLength = autoExpandListView.getChildCount();
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < listLength; i++)
                        {
                            v = autoExpandListView.getChildAt(i);
                            EditText et = (EditText) v.findViewById(R.id.edtFormInput);
                            sb.append(et.getText().toString());
                        }
//                        StringBuffer sb = new StringBuffer();
//                        sb.append(holder.edtFormInput.getText().toString());
                        composeFooterInterface.onSendClick(getDotMessage(sb.toString()), sb.toString(),false);
                    }
                }
            });


        } else {
            autoExpandListView.setAdapter(null);
            multiSelectLayout.setVisibility(GONE);
        }
    }

    private String getDotMessage(String strPassword)
    {
        String strDots = "";
        for (int i = 0; i< strPassword.length(); i++)
        {
            strDots = strDots+"•";
        }
        return strDots;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        //get the available size of child view
        int childLeft = 0;
        int childTop = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }
}
