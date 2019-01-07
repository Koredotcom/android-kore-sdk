package kore.botssdk.activity;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import kore.botssdk.R;

/**
 * Created by Ramachandra Pradeep on 27-Mar-18.
 */

public class BotAppCompactActivity extends AppCompatActivity {

    protected final String LOG_TAG = getClass().getSimpleName();
    private ProgressDialog mProgressDialog;


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
