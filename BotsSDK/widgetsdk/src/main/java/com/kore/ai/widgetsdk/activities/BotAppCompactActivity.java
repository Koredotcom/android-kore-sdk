package com.kore.ai.widgetsdk.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kora.ai.widgetsdk.R;

/**
 * Created by Ramachandra Pradeep on 27-Mar-18.
 */

public class BotAppCompactActivity extends AppCompatActivity {

    protected final String LOG_TAG = getClass().getSimpleName();
    private ProgressDialog mProgressDialog;

   // SpiceManager spiceManager = new SpiceManager(BotRestService.class);
    public void finish() {
       /* if(this.spiceManager.isStarted()) {
            this.spiceManager.shouldStop();
        }*/
        super.finish();
    }

    protected void onCreate(Bundle data) {
        super.onCreate(data);
        /*if(!this.spiceManager.isStarted()) {
            this.spiceManager.start(getApplicationContext());
        }*/
    }

    /*public SpiceManager getSpiceManager() {
        return this.spiceManager;
    }*/


    protected void showProgress(String msg, boolean isCancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            dismissProgress();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setContentView(R.layout.progress_indicator);
        ((TextView)mProgressDialog.findViewById(R.id.loadingText)).setText(TextUtils.isEmpty(msg)? "please wait" : msg);
        mProgressDialog.show();
    }

    protected void dismissProgress() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

}
