package kore.botssdk.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.application.AppControl;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class TextMediaLayout extends MediaLayout {

    TextView botContentTextView;

    public static final int TEXTVIEW_ID = 1980081;
    public static final int LIST_ID = 1980045;
    public static final int TEXT_MEDIA_LAYOUT_ID = 73733614;
    public static final int CAROUSEL_VIEW_ID = 1980053;
    public static final int BUTTON_VIEW_ID = 1980098;
    public static final int PIECHART_VIEW_ID = 19800123;
    public static final int TABLE_VIEW_ID = 19800345;
    public static final int LINECHART_VIEW_ID = 19800335;
    public static final int BARCHART_VIEW_ID = 19800355;
    public static final int STACK_BARCHAT_VIEW_ID = 19800375;
    public static final int MINI_TABLE_VIEW_ID = 19800365;

    private float restrictedLayoutWidth;

    public static int GRAVITY_LEFT = 0;
    public static int GRAVITY_RIGHT = 1;
    public int gravity = 0;

    public static int MATCH_PARENT = 0;
    public static int WRAP_CONTENT = 1;
    public int widthStyle = 0;

    float dp1;
    private Context mContext;
    final String TEXT_COLOR = "#000000";
    private int linkTextColor;
    private Typeface medium, regular;


    public TextMediaLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public TextMediaLayout(Context context, int linkTextColor) {
        super(context);
        this.mContext = context;
        this.linkTextColor = linkTextColor;
        init();
    }

    private void init() {
        medium = Typeface.create("sans-serif-medium", Typeface.NORMAL);
        regular = Typeface.create("sans-serif", Typeface.NORMAL);
        if (!isInEditMode()) {
            dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        }

        //Add a textView
        botContentTextView = new LinkifyTextView(getContext());


        RelativeLayout.LayoutParams txtVwParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        botContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
        botContentTextView.setText("");
        botContentTextView.setLayoutParams(txtVwParams);
        botContentTextView.setSingleLine(false);
        botContentTextView.setClickable(false);
        botContentTextView.setAutoLinkMask(Linkify.ALL);
        botContentTextView.setId(TEXTVIEW_ID);
        botContentTextView.setPadding(0, 0, 0, 0);
        botContentTextView.setLinkTextColor(linkTextColor);
        // KaFontUtils.setCustomTypeface(botContentTextView,KaFontUtils.ROBOTO_REGULAR, getContext());
        botContentTextView.setFocusable(false);
        botContentTextView.setClickable(false);
        botContentTextView.setLongClickable(false);
        addView(botContentTextView);

    }

    public void startup(String messageBody, int... dimens) {
        populateText(messageBody);
    }

    public void populateText(String textualContent) {
        if (textualContent != null && !textualContent.isEmpty()) {
            textualContent = unescapeHtml4(textualContent.trim());
            if(gravity != GRAVITY_LEFT) {
                textualContent = "\"" + textualContent + "\"";
            }
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(textualContent);
            URLSpan[] urls = strBuilder.getSpans(0, textualContent.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            botContentTextView.setText(strBuilder);
            botContentTextView.setMovementMethod(null);
            botContentTextView.setVisibility(VISIBLE);
        } else {
            botContentTextView.setText("");
            botContentTextView.setVisibility(GONE);
        }

    }

    public void setGravityAndTypeFace(){
        if (gravity == GRAVITY_LEFT) {
            //   botContentTextView.setGravity(Gravity.START);
            botContentTextView.setTypeface(medium);
        } else {
            // botContentTextView.setGravity(Gravity.END);
            botContentTextView.setTypeface(regular,Typeface.ITALIC);
         }
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GenericWebViewActivity.class);
                intent.putExtra("url", span.getURL());
                intent.putExtra("header", getResources().getString(R.string.app_name));
                getContext().startActivity(intent);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public TextView getBotContentTextView() {
        return botContentTextView;
    }

    public void setRestrictedLayoutWidth(float restrictedLayoutWidth) {
        this.restrictedLayoutWidth = restrictedLayoutWidth;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childWidthSpec, childHeightSpec;
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int containerWidth = 0;

        final int count = getChildCount();
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childHeight = 0, childWidth = 0;

            switch (child.getId()) {
                case TEXTVIEW_ID:
                    childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.UNSPECIFIED);
                    MeasureUtils.measure(child, childWidthSpec, wrapSpec);
                    childWidth = child.getMeasuredWidth();
                    if (childWidth > restrictedLayoutWidth) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.AT_MOST);
                        MeasureUtils.measure(child, childWidthSpec, wrapSpec);
                    }
                    childHeight = child.getMeasuredHeight();
                    break;
                default:

            }

            totalHeight += childHeight;
            containerWidth = (containerWidth < child.getMeasuredWidth()) ? child.getMeasuredWidth() : containerWidth;
        }

        if (widthStyle == MATCH_PARENT) {
            containerWidth = parentWidth;
        } else if (widthStyle == WRAP_CONTENT) {
            containerWidth = containerWidth;
        }

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(containerWidth, MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();

        //walk through each child, and arrange it from left to right
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop);
                childTop += child.getMeasuredHeight();
            }
        }
    }

    public int getLinkTextColor() {
        return linkTextColor;
    }

    public void setLinkTextColor(int linkTextColor) {
        this.linkTextColor = linkTextColor;
    }
}
