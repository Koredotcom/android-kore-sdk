package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterUpdate;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ComposeFooterFragment extends BaseSpiceFragment implements ComposeFooterUpdate {

    String LOG_TAG = ComposeFooterFragment.class.getName();

    EditText composeFooterEditTxt;
    Button composeFooterSendBtn;
    boolean isDisabled, isFirstTime;
    ComposeFooterInterface composeFooterInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.compose_footer_layout, null);

        findViews(view);

        isDisabled = true;
        isFirstTime = true;
        updateUI();
        setListener();

        return view;
    }

    private void findViews(View view) {
        composeFooterEditTxt = (EditText) view.findViewById(R.id.composeFooterEditTxt);
        composeFooterSendBtn = (Button) view.findViewById(R.id.composeFooterSendBtn);
    }

    private void updateUI() {
        composeFooterSendBtn.setEnabled(!isDisabled && !isFirstTime);
    }

    private void setListener() {
        if (isDisabled && isFirstTime) {
            composeFooterSendBtn.setOnClickListener(null);
        } else {
            composeFooterSendBtn.setOnClickListener(composeFooterSendBtOnClickListener);
        }
    }

    View.OnClickListener composeFooterSendBtOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = composeFooterEditTxt.getText().toString().trim();
            if (!msg.isEmpty()) {
                composeFooterEditTxt.setText("");
                sendMessageText(msg);
            }
        }
    };

    private void sendMessageText(String message) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message);
        } else {
            Log.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public void enableSendButton() {
        isDisabled = false;
        isFirstTime = false;

        updateUI();
        setListener();
    }

    public interface ComposeFooterInterface {
        void onSendClick(String message);
    }

}
