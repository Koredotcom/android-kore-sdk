package kore.botssdk.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import kore.botssdk.R;
import kore.botssdk.utils.ToastUtils;

/**
 * Created by Ramachandra Pradeep on 27-Mar-18.
 */
@SuppressLint("UnknownNullness")
public class BotAppCompactActivity extends AppCompatActivity {

    protected final String LOG_TAG = getClass().getSimpleName();
    private ProgressDialog mProgressDialog;

    // SpiceManager spiceManager = new SpiceManager(BotRestService.class);
    public void finish() {
        super.finish();
    }

    protected void onCreate(Bundle data) {
        super.onCreate(data);
    }

    protected void showProgress(String msg, boolean isCancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setContentView(R.layout.progress_indicator);
        ((TextView) mProgressDialog.findViewById(R.id.loadingText)).setText(TextUtils.isEmpty(msg) ? "please wait" : msg);
        mProgressDialog.show();
    }

    protected void dismissProgress() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    protected final void showToast(String message) {
        if (message != null && !message.equals("INVALID_ACCESS_TOKEN"))
            ToastUtils.showToast(this, message);
    }

    protected final void showToast(String msg, int length) {
        ToastUtils.showToast(this, msg, length);
    }

}
