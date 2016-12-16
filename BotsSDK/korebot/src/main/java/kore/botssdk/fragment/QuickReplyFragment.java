package kore.botssdk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.models.QuickReplyTemplate;

/**
 * Created by Ramachandra Pradeep on 12/8/2016.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class QuickReplyFragment extends BaseSpiceFragment {

    private final String LOG_TAG = QuickReplyFragment.class.getSimpleName();
    private LinearLayout quick_reply_container;
    private float dp1;
//    private String[] arr = {" Hi ","Hello","How are you"," list ","what","when","where","good","discard"};
    private QuickReplyInterface mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quickreply_layout,null);
        dp1 = AppControl.getInstance().getDimensionUtil().dp1;
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        quick_reply_container = (LinearLayout) view.findViewById(R.id.quick_reply_container);
        quick_reply_container.setVisibility(View.GONE);

    }
    public QuickReplyInterface getListener() {
        return mListener;
    }

    public void setListener(QuickReplyInterface mListener) {
        this.mListener = mListener;
    }
    public interface QuickReplyInterface {
        void onQuickReplyItemClicked(String text);
    }
    public void populateQuickReplyViews(ArrayList<QuickReplyTemplate> quickReplies){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int)dp1*5, (int)dp1*5, (int)dp1*5, (int)dp1*5);
        for(QuickReplyTemplate qReply : quickReplies) {
            final TextView txtQuickReply = new TextView(getActivity());
            txtQuickReply.setText(qReply.getTitle());
            txtQuickReply.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            txtQuickReply.setLayoutParams(layoutParams);
            txtQuickReply.setPadding((int) dp1 * 12, (int) dp1 * 8, (int) dp1 * 12, (int) dp1 * 8);
            txtQuickReply.setTextColor(Color.parseColor("#01CDEF"));
            txtQuickReply.setBackgroundResource(R.drawable.quick_reply_textbackground);
            txtQuickReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onQuickReplyItemClicked(txtQuickReply.getText().toString());
                        quick_reply_container.setVisibility(View.GONE);
                    }
                }
            });
            quick_reply_container.addView(txtQuickReply);
        }
        quick_reply_container.setVisibility(View.VISIBLE);
    }
}

