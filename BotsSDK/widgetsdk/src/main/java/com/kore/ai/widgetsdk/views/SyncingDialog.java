package com.kore.ai.widgetsdk.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.utils.KaFontUtils;

/**
 * Created by Pradeep on 14-Apr-15.
 */
public class SyncingDialog extends Dialog {

    TextView syncDialogLoadingTextView;
    TextView syncDialogPreparingDeviceTextView;
    Context context;

    protected SyncingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public SyncingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public SyncingDialog(Context context) {
        super(context);
        this.context = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);

        findView();

    }

    private void findView(){
        syncDialogLoadingTextView = (TextView) findViewById(R.id.syncDialogLoadingTextView);
        syncDialogPreparingDeviceTextView = (TextView) findViewById(R.id.syncDialogPreparingDeviceTextView);

        KaFontUtils.applyCustomFont(context, syncDialogLoadingTextView);
        KaFontUtils.applyCustomFont(context, syncDialogPreparingDeviceTextView);
    }

    public void populateTextualMessage(String textualMessage){
        syncDialogLoadingTextView.setText(textualMessage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customiseDialogWindowDimen();
    }

    private void customiseDialogWindowDimen() {
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

}
