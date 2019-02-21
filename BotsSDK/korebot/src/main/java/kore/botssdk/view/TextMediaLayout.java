package kore.botssdk.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.application.AppControl;
import kore.botssdk.drawables.TopGravityDrawable;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class TextMediaLayout extends ViewGroup {

    TextView botContentTextView;
    ImageView imageView;

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

    private float restrictedLayoutWidth, restrictedLayoutHeight;

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
    private LinearLayout rootLayout;

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.text_media_layout, this, true);
        rootLayout = view.findViewById(R.id.root_layout);
        medium = Typeface.create("sans-serif-medium", Typeface.NORMAL);
        regular = Typeface.create("sans-serif", Typeface.NORMAL);
        if (!isInEditMode()) {
            dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        }

        //Add a textView
        botContentTextView = view.findViewById(R.id.text_view);
        botContentTextView.setAutoLinkMask(Linkify.ALL);
        botContentTextView.setLinkTextColor(linkTextColor);
        botContentTextView.setFocusable(false);
        botContentTextView.setClickable(false);
        botContentTextView.setLongClickable(false);

        imageView = view.findViewById(R.id.image);

    }

    public void startup(String messageBody, int... dimens) {
        populateText(messageBody);
    }

    public void populateText(String textualContent) {
        if (textualContent != null && !textualContent.isEmpty()) {
            textualContent = unescapeHtml4(textualContent.trim());
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(textualContent);
            URLSpan[] urls = strBuilder.getSpans(0, textualContent.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            if(gravity ==GRAVITY_RIGHT){
                imageView.setVisibility(VISIBLE);
            }else{
                imageView.setVisibility(GONE);
            }
            botContentTextView.setText(strBuilder);
            botContentTextView.setMovementMethod(null);
            botContentTextView.setVisibility(VISIBLE);
        } else {
            botContentTextView.setText("");
            botContentTextView.setVisibility(GONE);
            imageView.setVisibility(GONE);
        }

    }

    public void setGravityAndTypeFace(){
        if (gravity == GRAVITY_LEFT) {
            botContentTextView.setTypeface(medium);
            rootLayout.setGravity(Gravity.START);
        } else {
            rootLayout.setGravity(Gravity.END);
            botContentTextView.setTypeface(regular);
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

    public void setRestrictedLayoutHeight(float restrictedLayoutHeight) {
        this.restrictedLayoutHeight = restrictedLayoutHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int totalHeight = getPaddingTop();
        int childWidthSpec;

        childWidthSpec = MeasureSpec.makeMeasureSpec((int) restrictedLayoutWidth, MeasureSpec.EXACTLY);
        MeasureUtils.measure(rootLayout, childWidthSpec, wrapSpec);

        totalHeight += rootLayout.getMeasuredHeight() + getPaddingBottom() + getPaddingTop();

        int parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(rootLayout.getMeasuredWidth(), MeasureSpec.AT_MOST);
        setMeasuredDimension(parentWidthSpec, parentHeightSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int parentWidth = getMeasuredWidth();

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

    public int getLinkTextColor() {
        return linkTextColor;
    }

    public void setLinkTextColor(int linkTextColor) {
        this.linkTextColor = linkTextColor;
    }
}
