package kore.botssdk.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kore.botssdk.R;
import kore.botssdk.activity.GenericWebViewActivity;
import kore.botssdk.application.AppControl;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.events.ProfileColorUpdateEvent;
import kore.botssdk.models.EntityEditModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BubbleConstants;
import kore.botssdk.utils.DashedBorderSpan;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class TextMediaLayout extends MediaLayout {

    private TextView botContentTextView;

    private float restrictedLayoutWidth;
    public int gravity = 0;
    public int widthStyle = 0;

    float dp1;
    private Context mContext;
    final String TEXT_COLOR = "#000000";
    private int linkTextColor;
    private Typeface medium, regular;
    private GradientDrawable rightDrawable;
    private int transparency;

    private boolean isClicable;
    private final String REGEX_CHAR = "%%.*?%%";

    public boolean isClicable() {
        return isClicable;
    }

    public void setClicable(boolean clicable) {
        isClicable = clicable;
    }

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
        medium = KaFontUtils.getCustomTypeface("medium",mContext);
        regular = KaFontUtils.getCustomTypeface("regular",mContext);
        if (!isInEditMode()) {
            dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        }

        //Add a textView
        botContentTextView = new LinkifyTextView(getContext());

        //Transparency 15%
        transparency = 0x26000000;
        rightDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.rounded_rectangle_bubble);
        rightDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+transparency);
        rightDrawable.setStroke((int) (1*dp1), Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+transparency);

        RelativeLayout.LayoutParams txtVwParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        botContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        botContentTextView.setText("");
        botContentTextView.setLayoutParams(txtVwParams);
        botContentTextView.setSingleLine(false);
        botContentTextView.setClickable(false);
        botContentTextView.setAutoLinkMask(Linkify.ALL);
        botContentTextView.setId(BubbleConstants.TEXTVIEW_ID);
        botContentTextView.setPadding(0, 0, 0, 0);
        botContentTextView.setLinkTextColor(linkTextColor);
        // KaFontUtils.setCustomTypeface(botContentTextView,KaFontUtils.ROBOTO_REGULAR, getContext());
        botContentTextView.setFocusable(false);
        botContentTextView.setClickable(false);
        botContentTextView.setLongClickable(false);
        addView(botContentTextView);

    }
    public void onEvent(ProfileColorUpdateEvent event){
        rightDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+transparency);
        rightDrawable.setStroke((int) (1*dp1), Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+transparency);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KoreEventCenter.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KoreEventCenter.unregister(this);
    }
    public void startup(String messageBody, int... dimens) {
        populateText(messageBody);
    }

    private String getRemovedEntityEditString(String _str){
       String str = _str.replaceAll(REGEX_CHAR,"");
      // if(removeAll) {
           str = str.replaceAll("\u270E", "");
     //  }
       str = str.replaceAll("\\s{2,}", " ");
       return str;
    }

    private SpannableString getFilterWithIcon(String stringValue, int icon) {
        Drawable image = ContextCompat.getDrawable(mContext, icon);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        String spString = "  " + stringValue;
        SpannableString spFilterWithIcon = new SpannableString(spString);

        spFilterWithIcon.setSpan(new DashedBorderSpan(mContext.getResources().getDrawable(R.drawable.test_dash),0), 1 ,spString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spFilterWithIcon.setSpan(imageSpan, spString.length()-1, spString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spFilterWithIcon;
    }

    private String getReqText(String str){
        String newT = str;

        Pattern pattern = Pattern.compile(REGEX_CHAR);
        Matcher matcher1 = pattern.matcher(str);

        while(matcher1.find()) {
            int _start = matcher1.start() ;
            int _end = matcher1.end();

            String replaceText = str.substring(_start,_end);
            String _payload = replaceText.substring(2,replaceText.length()-2);
            Log.d("!@#$% getReqText(", _payload);
            _payload = _payload.substring(_payload.indexOf("{"),_payload.length());
            EntityEditModel model = new com.google.gson.Gson().fromJson(_payload, EntityEditModel.class);
            String addableText = model.getTitle();

            boolean isIconNeeded = Boolean.parseBoolean(model.isIcon());
            if(isIconNeeded){
                addableText+=" "+"\u270E";
            }

            newT = newT.replace(replaceText, addableText+replaceText);
        }
        return newT;
    }

    public void populateText(String textualContent) {
        if (textualContent != null && !textualContent.isEmpty()) {

            textualContent = unescapeHtml4(textualContent.trim());
            /*if(gravity != BubbleConstants.GRAVITY_LEFT) {
                textualContent = "\"" + textualContent + "\"";
            }*/
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(textualContent);
            URLSpan[] urls = strBuilder.getSpans(0, textualContent.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }

            Pattern pattern = Pattern.compile(REGEX_CHAR);

            ImageSpan pencilImageSpan;
            boolean isPencilSpanClick = false;

            if(textualContent.indexOf("%%{")>0){
                Log.d("!@#$% BEFORE ", textualContent);
                textualContent = getReqText(textualContent);
                Log.d("!@#$% AFTER ", textualContent);
                strBuilder = new SpannableStringBuilder(textualContent);
            }
            Matcher matcher = pattern.matcher(textualContent);

            while(matcher.find()) {
                botContentTextView.setLineSpacing(1,1.3f);
                pencilImageSpan = new ImageSpan(mContext,R.drawable.transparant_image);
                isPencilSpanClick = true;
                int _start = matcher.start();
                int _end = matcher.end();

                String reqText = textualContent.substring(_start+2, _end-2);
                reqText = reqText.substring(reqText.indexOf("{"),reqText.length());

                Log.d("!@#$% REQ_TEXT while", reqText);

                EntityEditModel model = new com.google.gson.Gson().fromJson(reqText, EntityEditModel.class);
                String addableText = model.getTitle();

                int addableTextLength = 0;
                if(!StringUtils.isNullOrEmpty(addableText)) {
                    addableTextLength = addableText.length();
                }

                strBuilder.setSpan(pencilImageSpan, _start, _end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                int dashStart = _start - addableTextLength - 2;
                int dashEnd = _start;

                strBuilder.setSpan(new DashedBorderSpan(mContext.getResources().getDrawable(R.drawable.test_dash),0),dashStart,dashEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                SpannableStringBuilder finalStrBuilder = strBuilder;
                String finalReqText = reqText;
                ClickableSpan clickable = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {

                        if(isClicable()) {
                            botContentTextView.setText(getRemovedEntityEditString(finalStrBuilder.toString()));
                            EntityEditModel model = new com.google.gson.Gson().fromJson(finalReqText, EntityEditModel.class);

                            EntityEditEvent event = new EntityEditEvent();
                            event.setScrollUpNeeded(false);
                            event.setMessage(model.getPostback());
                            KoreEventCenter.post(event);
                        }
                    }
                };
                strBuilder.setSpan(clickable, dashStart,dashEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if(isPencilSpanClick && !isClicable()){
                botContentTextView.setText(getRemovedEntityEditString(strBuilder.toString()));
            }else{
                botContentTextView.setText(strBuilder);
            }

            if(isPencilSpanClick)
                botContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            else
                botContentTextView.setMovementMethod(null);
            botContentTextView.setVisibility(VISIBLE);
        } else {
            botContentTextView.setText("");
            botContentTextView.setVisibility(GONE);
        }

    }
    public void populateErrorText(String textualContent, String color) {
        if (textualContent != null && !textualContent.isEmpty()) {
            textualContent = unescapeHtml4(textualContent.trim());
            /*if(gravity != BubbleConstants.GRAVITY_LEFT) {
                textualContent = "\"" + textualContent + "\"";
            }*/
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(textualContent);
            URLSpan[] urls = strBuilder.getSpans(0, textualContent.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            botContentTextView.setTextColor(Color.parseColor(color));
            botContentTextView.setText(strBuilder);
            botContentTextView.setMovementMethod(null);
            botContentTextView.setVisibility(VISIBLE);
        } else {
            botContentTextView.setText("");
            botContentTextView.setVisibility(GONE);
        }

    }

    public void setGravityAndTypeFace(){
        if (gravity == BubbleConstants.GRAVITY_LEFT) {
            //   botContentTextView.setGravity(Gravity.START);
            botContentTextView.setTypeface(medium);
            botContentTextView.setBackground(null);
        } else {
            // botContentTextView.setGravity(Gravity.END);
            botContentTextView.setTypeface(regular);
            botContentTextView.setBackground(rightDrawable);
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
                case BubbleConstants.TEXTVIEW_ID:
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

        if (widthStyle == BubbleConstants.MATCH_PARENT) {
            containerWidth = parentWidth;
        } else if (widthStyle == BubbleConstants.WRAP_CONTENT) {
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
