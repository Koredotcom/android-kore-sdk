package com.kore.ai.widgetsdk.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.applicationcontrol.ACMEngine;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.utils.AppUtils;
import com.kore.ai.widgetsdk.utils.KaPermissionsHelper;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.SharedPreferenceUtils;
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.Utils;
import com.kore.ai.widgetsdk.views.SyncingDialog;

import java.net.UnknownHostException;

import okhttp3.ResponseBody;


public abstract class KaAppCompatActivity extends AppCompatActivity{

    private String TAG = "KoreAppCompatActivity";
    AlertDialog alertDialog;

    protected final String LOG_TAG = getClass().getSimpleName();

    private ProgressDialog mProgressDialog;
//    private ProgressDialog mLoader;
    private static SyncingDialog mLoader;
    protected Toolbar toolbar;
    protected int DP1;
    protected static final int ELEVATION_VALUE_EIGHT = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if database context is null then simply restart the applicatio
        super.onCreate(savedInstanceState);
        initACL();
        init();
    }

    private void init() {
        if(UserDataManager.getAuthData() != null && UserDataManager.getAuthData().accessToken!=null)
            KaUtility.fetchAppUpgradeStatus(this, Utils.ah(UserDataManager.getAuthData().accessToken));
    }

    /*
    This was moved into a separate method to handle the case of Share into kore when the app is not in background. In that case the KoreControl
    was being called for DP1 and by that time it wasn't initialized. So moved it into a diff method and calling it after Authorized in case of MDM.
     */
    private void initACL(){
        if(ACMEngine.appControl == null && UserDataManager.getUserData() != null) {
            ACMEngine.processACMList(SharedPreferenceUtils.getInstance(KaAppCompatActivity.this).getAppControlList(UserDataManager.getUserData().getId()));
        }
    }


    protected void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.app_name));
   /*     try {
            Field field = toolbar.getClass().getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            TextView titleView = (TextView) field.get(toolbar);

            titleView.setTag(KaFontUtils.ROBOTO_BOLD);
            KaFontUtils.applyCustomFont(this, titleView);

        } catch (Exception exception) {
            //  KoreLogger.errorLog(this.getClass().getSimpleName(), exception.getMessage(), exception);
        }*/

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
    }

    public Toolbar getToolBar() {
        return toolbar;
    }

    protected  void onStart(){
        super.onStart();
    }
    protected  void onStop(){
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                AppUtils.showHideVirtualKeyboard(this,null,false);
                return super.onOptionsItemSelected(item);
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void showProgress(String msg, boolean isCancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing() || isDestroyed()) {
//            dismissProgress();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setContentView(R.layout.ka_progress_indicator);
    //   ((TextView)mProgressDialog.findViewById(R.id.title)).setText(TextUtils.isEmpty(msg)? "please wait" : msg);
//        mProgressDialog.show();
    }

    protected void dismissProgress() {
        if (!isDestroyed()) {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                return;
            }
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

    }

    protected void showLoader() {
        if(mLoader == null)
            mLoader = new SyncingDialog(this);
        if (mLoader != null && mLoader.isShowing() || isDestroyed()) {
//            dismissProgress();
            return;
        }

        /*mLoader = ProgressDialog.show(this, getResources().getString(R.string.app_name), "");
        mLoader.setCancelable(false);
        mLoader.setContentView(R.layout.ka_loading_indicator);
        ((ShimmerFrameLayout)mLoader.findViewById(R.id.shimmer_view)).startShimmer();*/

        try {
            if(!isFinishing() && !isDestroyed())
                mLoader.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }
//        mProgressDialog.show();
    }

    protected void dismissLoader() {
        if (!isDestroyed()) {
            if (mLoader == null || !mLoader.isShowing()) {
                return;
            }
//            ((ShimmerFrameLayout)mLoader.findViewById(R.id.shimmer_view)).stopShimmer();
            mLoader.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
    }

    @Override
    public ActionMode startSupportActionMode(final ActionMode.Callback callback) {
        // Fix for bug https://code.google.com/p/android/issues/detail?id=159527
        final ActionMode mode = super.startSupportActionMode(callback);
        if (mode != null) {
            mode.invalidate();
        }
        return mode;
    }

    protected final void showToast(String message){
        if(!message.equals("INVALID_ACCESS_TOKEN"))
            ToastUtils.showToast(this,message);
    }
    protected final void showToast(String msg, int length) {
        ToastUtils.showToast(this, msg, length);
    }

    /**
     * Centralized method to show toast message
     *
     * @param msg     Message to be displayed
     * @param length  Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     * @param gravity Anchor point
     * @param xOffset x-offset
     * @param yOffset y-0ffset
     */
    protected final void showToast(String msg, int length, int gravity, int xOffset, int yOffset) {
        ToastUtils.showToast(this, msg, length, gravity, xOffset, yOffset);
    }

    protected void showMessageOnSnackBar(String msg) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        showMessageOnSnackBar(msg,viewGroup);
    }
    protected void showMessageOnSnackBar(String msg,View view) {
        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    private Gson gson = new Gson();
    private String getHeader(String code) {
        switch (code.trim()) {
            case "QuestionAlreadyExist":
                return "Duplicate Question";
            case "InvalidQuestion":
                return "Trigger question is too generic";
            default:
                return getResources().getString(R.string.app_name);
        }
    }
    protected void showAlert(String msg, String code, final boolean finish) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getHeader(code))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(finish) {
                            finish();
                        }
                    }
                }).create().show();
    }
    public void showErrorToast(ResponseBody responseBody) {
        KaRestResponse.KoreStringErrorResponse errorLists;
        try {
            errorLists = gson.fromJson(responseBody.charStream(), KaRestResponse.KoreStringErrorResponse.class);
            showToast(errorLists.errors.get(0).msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showErrorDialog(ResponseBody responseBody,boolean finish) {
        KaRestResponse.KoreStringErrorResponse errorLists;
        try {
            errorLists = gson.fromJson(responseBody.charStream(), KaRestResponse.KoreStringErrorResponse.class);
            showAlert(errorLists.errors.get(0).msg,errorLists.errors.get(0).code,finish);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showErrorToast(Throwable e) {

        if (e instanceof UnknownHostException) {
            showToast("No Internet Connection. Please check your internet connection and try again.");
        }

    }

    public void showPermissionsDialog(String message){
        final Activity activity = this;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(message);
        alertBuilder.setIcon(null);
        alertBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                KaPermissionsHelper.startInstalledAppDetailsActivity(activity);
                dialog.dismiss();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.setTitle("Grant Permissions");
        alertBuilder.show();
    }
    void showPopUp(String header,String message){
        if(alertDialog == null || !alertDialog.isShowing()) {
            if(alertDialog == null) {
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            alertDialog.setTitle(header);
            alertDialog.setMessage(message);
            alertDialog.show();
        }
    }


}
