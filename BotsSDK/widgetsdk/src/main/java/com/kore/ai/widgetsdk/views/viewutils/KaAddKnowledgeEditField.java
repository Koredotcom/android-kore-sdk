package com.kore.ai.widgetsdk.views.viewutils;/*
package com.kore.ai.koreassistant.views.viewutils;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.kore.ai.koreassistant.models.MetaDataModel;
import com.kore.ai.koreassistant.views.AttachmentsLayout;
import com.kore.ai.koreassistant.views.LinkPreviewLayout;
import com.kore.ai.koreassistant.views.SpannableEditText;
import com.kore.korelib.models.KoreMedia;

import java.util.HashMap;
import java.util.Objects;

import kore.botssdk.application.AppControl;

*/
/**
 * Created by Shiva Krishna on 1/17/2018.
 *//*




public class KaAddKnowledgeEditField extends SpannableEditText {

    String LOG_TAG = KaAddKnowledgeEditField.class.getSimpleName();

    private Context mContext;
    private float deviceWidth, secureEmailEditTextViewWidth, dp1;

    LayoutInflater layoutInflater;

    public KaAddKnowledgeEditField(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeParameters();
        prepareLayoutParams();
    }

    private void initializeParameters() {
        dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        deviceWidth = AppControl.getInstance().getDimensionUtil().screenWidth;
        layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        secureEmailEditTextViewWidth = deviceWidth - (getPaddingLeft() + getPaddingRight());
    }

    private void prepareLayoutParams() {

    }

    private void resetLayoutParams() {

    }

    @Override
    protected View getViewForObject(MetaDataModel sourceContent) {
        if(Objects.equals(sourceContent.getType(), KoreMedia.MEDIA_TYPE_LINK)) {
            LinkPreviewLayout linkPreviewLayout = new LinkPreviewLayout(getContext());
            HashMap<String, Integer> params = new HashMap<>();
            params.put("width", (int) secureEmailEditTextViewWidth);
            params.put("height", (int) (140 * dp1));
            linkPreviewLayout.setLocationLayoutParams(params);
            linkPreviewLayout.populateLinkMeta(mContext, sourceContent);
            return linkPreviewLayout;
        }else{
            AttachmentsLayout attachmentsLayout = new AttachmentsLayout(getContext());
            HashMap<String, Integer> params = new HashMap<>();
            params.put("width", (int) secureEmailEditTextViewWidth/2-30);
            params.put("height", (int) (140 * dp1));
            attachmentsLayout.setLocationLayoutParams(params);
            attachmentsLayout.populateLinkMeta(mContext, sourceContent);
            return attachmentsLayout;
        }
    }




}*/
