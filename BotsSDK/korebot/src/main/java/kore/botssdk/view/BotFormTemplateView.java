package kore.botssdk.view;

import android.annotation.SuppressLint;
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

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotFormTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotFormTemplateModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

@SuppressLint("UnknownNullness")
public class BotFormTemplateView extends ViewGroup {
    AutoExpandListView autoExpandListView;
    private View multiSelectLayout;
    private float dp1;
    private TextView tvForm_template_title;
    private TextView btFieldButton;
    private SharedPreferences sharedPreferences;
    private String leftTextColor;

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
    ComposeFooterInterface composeFooterInterface;


    private void init(Context context) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_templete_view, this, true);
        autoExpandListView = view.findViewById(R.id.multi_select_list);
        autoExpandListView.setVerticalScrollBarEnabled(false);
        KaFontUtils.applyCustomFont(getContext(), view);
        multiSelectLayout = view.findViewById(R.id.multi_select_layout);
        tvForm_template_title = view.findViewById(R.id.tvform_template_title);
        btFieldButton = view.findViewById(R.id.btfieldButton);
        dp1 = (int) DimensionUtil.dp1;

        sharedPreferences = getSharedPreferences(context);
        String leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#EBEBEB");
        leftTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000");

        GradientDrawable leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.theme1_left_bubble_bg, getContext().getTheme());
       if(leftDrawable != null) {
           leftDrawable.setColor(Color.parseColor(leftBgColor));
           leftDrawable.setStroke((int) (1 * dp1), Color.parseColor(leftBgColor));
           multiSelectLayout.setBackground(leftDrawable);
       }
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

            tvForm_template_title.setText(payloadInner.getHeading());
            BotFormTemplateAdapter botFormTemplateAdapter = new BotFormTemplateAdapter(getContext(), payloadInner.getBotFormTemplateModels());
            botFormTemplateAdapter.setBotFormTemplates(items);
            botFormTemplateAdapter.setEnabled(isEnabled);
            botFormTemplateAdapter.setTextColor(leftTextColor);
            autoExpandListView.setAdapter(botFormTemplateAdapter);
            botFormTemplateAdapter.notifyDataSetChanged();

            if(!StringUtils.isNullOrEmpty(leftTextColor))
            {
                tvForm_template_title.setTextColor(Color.parseColor(leftTextColor));
                btFieldButton.setTextColor(Color.parseColor(leftTextColor));
            }

            if(payloadInner.getFieldButton() != null)
                if(!StringUtils.isNullOrEmpty(payloadInner.getFieldButton().getTitle()))
                    btFieldButton.setText(payloadInner.getFieldButton().getTitle());

            btFieldButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (composeFooterInterface != null && isEnabled)
                    {
                        int listLength = autoExpandListView.getChildCount();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < listLength; i++)
                        {
                            v = autoExpandListView.getChildAt(i);
                            EditText et = v.findViewById(R.id.edtFormInput);
                            sb.append(et.getText().toString());
                        }
                        composeFooterInterface.onSendClick(getDotMessage(sb.toString()), sb.toString(),false);
                    }
                }
            });


        } else {
            autoExpandListView.setAdapter(null);
            multiSelectLayout.setVisibility(GONE);
        }
    }

    String getDotMessage(String strPassword)
    {
        StringBuilder strDots = new StringBuilder();
        for (int i = 0; i< strPassword.length(); i++)
        {
            strDots.append("•");
        }
        return strDots.toString();
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
