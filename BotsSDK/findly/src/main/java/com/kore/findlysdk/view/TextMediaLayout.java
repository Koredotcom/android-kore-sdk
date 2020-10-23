package com.kore.findlysdk.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.kore.findlysdk.R;
import com.kore.findlysdk.activity.GenericWebViewActivity;
import com.kore.findlysdk.events.EntityEditEvent;
import com.kore.findlysdk.events.KoreEventCenter;
import com.kore.findlysdk.events.ProfileColorUpdateEvent;
import com.kore.findlysdk.models.BotResponse;
import com.kore.findlysdk.models.EntityEditModel;
import com.kore.findlysdk.utils.AppControl;
import com.kore.findlysdk.utils.BubbleConstants;
import com.kore.findlysdk.utils.KaFontUtils;
import com.kore.findlysdk.utils.StringUtils;
import com.kore.findlysdk.utils.markdown.MarkdownImageTagHandler;
import com.kore.findlysdk.utils.markdown.MarkdownTagHandler;
import com.kore.findlysdk.utils.markdown.MarkdownUtil;
import com.kore.findlysdk.view.viewUtils.LayoutUtils;
import com.kore.findlysdk.view.viewUtils.MeasureUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class TextMediaLayout extends ViewGroup {

    private TextView botContentTextView;
    private float restrictedLayoutWidth;
    public int gravity = 0;
    public int widthStyle = 0;
    private String chatIcon = "data:image/png;base64,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";
    float dp1;
    private Context mContext;
    //    final String TEXT_COLOR = "#000000";
    private int linkTextColor;
    private Typeface medium, regular;
    private GradientDrawable rightDrawable, leftDrawable;
    private int transparency;
    private SharedPreferences sharedPreferences;
    private boolean isClicable;
    private final String REGEX_CHAR = "%%.*?%%";
    private Gson gson = new Gson();
    private String leftbgColor, leftTextColor, rightbgColor, rightTextColor, themeName;
    public boolean isClicable() {
        return isClicable;
    }//

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

        sharedPreferences = getSharedPreferences();
        leftbgColor= sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff");
        leftTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000");
        rightTextColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, "#000000");
        rightbgColor= sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, "#0078cd");
        themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1);

        //Transparency 15%
        transparency = 0x26000000;
        rightDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.right_bubble_bg);
//        rightDrawable.setColor(Color.parseColor(rightbgColor));
//        rightDrawable.setStroke((int) (1*dp1), Color.parseColor(rightbgColor));

        leftDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.left_bubble_bg);

        if(themeName.equalsIgnoreCase(BotResponse.THEME_NAME_2))
            leftDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.left_bubble_bg);

//        leftDrawable.setColor(Color.parseColor(leftbgColor));
//        leftDrawable.setStroke((int) (1*dp1), Color.parseColor(leftbgColor));
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

    private Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private SharedPreferences getSharedPreferences()
    {
        sharedPreferences = mContext.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void onEvent(ProfileColorUpdateEvent event){
//        rightDrawable.setColor(Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+transparency);
//        rightDrawable.setStroke((int) (1*dp1), Color.parseColor(SDKConfiguration.BubbleColors.getProfileColor())+transparency);
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
    /*public void startup(String messageBody) {
        populateText(messageBody);
    }*/

    private String getRemovedEntityEditString(String _str){
        String str = _str.replaceAll(REGEX_CHAR,"");
        str = str.replaceAll("\\s{2,}", " ");
        return str;
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
            EntityEditModel model = gson.fromJson(_payload, EntityEditModel.class);
            String addableText = !StringUtils.isNullOrEmpty(model.getTitle())?model.getTitle().trim():"";

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
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
            textualContent = MarkdownUtil.processMarkDown(textualContent);
            CharSequence sequence = Html.fromHtml(textualContent.replace("\n", "<br />"),
                    new MarkdownImageTagHandler(mContext, botContentTextView, textualContent), new MarkdownTagHandler());
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }

            Pattern pattern = Pattern.compile(REGEX_CHAR);

            ImageSpan pencilImageSpan;
            boolean isPencilSpanClick = false;

            if(textualContent.indexOf("%%{")>0){
                Log.d("!@#$% BEFORE ", textualContent);// munduki%%{} %%
                textualContent = getReqText(textualContent);
                Log.d("!@#$% AFTER ", textualContent);
                strBuilder = new SpannableStringBuilder(textualContent);
            }
            Matcher matcher = pattern.matcher(textualContent);

            while(matcher.find()) {
                pencilImageSpan = new ImageSpan(mContext, R.drawable.pencil_18);
                isPencilSpanClick = true;
                int _start = matcher.start();
                int _end = matcher.end();

                String reqText = textualContent.substring(_start+2, _end-2);
                reqText = reqText.substring(reqText.indexOf("{"));

                Log.d("!@#$% REQ_TEXT while", reqText);

                EntityEditModel model = gson.fromJson(reqText, EntityEditModel.class);
                String addableText = !StringUtils.isNullOrEmpty(model.getTitle())?model.getTitle().trim():"";

                int addableTextLength = 0;
                if(!StringUtils.isNullOrEmpty(addableText)) {
                    addableTextLength = addableText.length();
                }

                boolean isIconNeeded = Boolean.parseBoolean(model.isIcon());
                if(isIconNeeded){
                    strBuilder.setSpan(pencilImageSpan, _start, _end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else{
                    strBuilder.setSpan(new ImageSpan(mContext, R.drawable.transparant_image), _start, _end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                int dashStart = _start - addableTextLength /*- iconLength*/;
                int dashEnd = _start;

                final SpannableStringBuilder finalStrBuilder = strBuilder;
                final String finalReqText = reqText;
                ClickableSpan clickable = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {

                        if(isClicable()) {
                            botContentTextView.setText(getRemovedEntityEditString(finalStrBuilder.toString()));
                            EntityEditModel model = gson.fromJson(finalReqText, EntityEditModel.class);
                            EntityEditEvent event = new EntityEditEvent();
                            event.setScrollUpNeeded(false);
                            event.setMessage(model.getPostback());
                            KoreEventCenter.post(event);
                        }
                    }
                };
                strBuilder.setSpan(clickable, dashStart,dashEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if(leftTextColor != null)
            {
                botContentTextView.setTextColor(Color.parseColor(leftTextColor));
                themeName = getSharedPreferences().getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1);
                leftDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.left_bubble_bg);
                if(themeName.equalsIgnoreCase(BotResponse.THEME_NAME_2))
                    leftDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.left_bubble_bg);

                botContentTextView.setBackground(leftDrawable);
                final String pureBase64Encoded = chatIcon.substring(chatIcon.indexOf(",")  + 1);
                Drawable img = new BitmapDrawable(getResources(), convertBase64ToBitmap(pureBase64Encoded));
                img.setBounds(0, 0, 70, 70);
                botContentTextView.setCompoundDrawables(img, null, null, null);
                botContentTextView.setCompoundDrawablePadding(25);
            }

            if(isPencilSpanClick && !isClicable()){
                botContentTextView.setText(getRemovedEntityEditString(strBuilder.toString()));
            }else{

                botContentTextView.setText(strBuilder);
            }

            if(isPencilSpanClick)
                botContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            else
//                botContentTextView.setMovementMethod(null);
                botContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            botContentTextView.setVisibility(VISIBLE);
        } else {
            botContentTextView.setText("");
            botContentTextView.setVisibility(GONE);
        }
    }

    public void populateTextSenders(String textualContent) {
        if (textualContent != null && !textualContent.isEmpty()) {
            textualContent = unescapeHtml4(textualContent.trim());
            /*if(gravity != BubbleConstants.GRAVITY_LEFT) {
                textualContent = "\"" + textualContent + "\"";
            }*/
            textualContent = StringUtils.unescapeHtml3(textualContent.trim());
            CharSequence sequence = Html.fromHtml(textualContent.replace("\n", "<br />"),
                    new MarkdownImageTagHandler(mContext, botContentTextView, textualContent), new MarkdownTagHandler());
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);

            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }

            if(rightTextColor != null)
                botContentTextView.setTextColor(Color.parseColor(rightTextColor));

            botContentTextView.setText(strBuilder);
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
            botContentTextView.setBackground(leftDrawable);
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
        int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
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
