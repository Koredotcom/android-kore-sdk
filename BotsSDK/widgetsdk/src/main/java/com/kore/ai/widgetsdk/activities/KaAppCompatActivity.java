package com.kore.ai.widgetsdk.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.applicationcontrol.ACMEngine;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.utils.AppUtils;
import com.kore.ai.widgetsdk.utils.KaUtility;
import com.kore.ai.widgetsdk.utils.SharedPreferenceUtils;
import com.kore.ai.widgetsdk.utils.ToastUtils;
import com.kore.ai.widgetsdk.utils.Utils;

import java.util.Objects;

import okhttp3.ResponseBody;


public abstract class KaAppCompatActivity extends AppCompatActivity{
    AlertDialog alertDialog;
    protected final String LOG_TAG = getClass().getSimpleName();
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            ACMEngine.processACMList(SharedPreferenceUtils.getAppControlList(UserDataManager.getUserData().getId()));
        }
    }


    protected void setupActionBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_black_24dp, getTheme()));
    }

    protected  void onStart(){
        super.onStart();
    }
    protected  void onStop(){
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            AppUtils.showHideVirtualKeyboard(this, null, false);
            return super.onOptionsItemSelected(item);
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


    protected void showProgress() {
        if (alertDialog != null && alertDialog.isShowing() || isDestroyed()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(KaAppCompatActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.ka_progress_indicator);
        alertDialog = builder.create();
        alertDialog.show();
    }

    protected void dismissProgress() {
        if (!isDestroyed()) {
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            alertDialog.dismiss();
            alertDialog = null;
        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
    }

    @Override
    public ActionMode startSupportActionMode(@NonNull final ActionMode.Callback callback) {
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

    private final Gson gson = new Gson();

    public void showErrorToast(ResponseBody responseBody) {
        KaRestResponse.KoreStringErrorResponse errorLists;
        try {
            errorLists = gson.fromJson(responseBody.charStream(), KaRestResponse.KoreStringErrorResponse.class);
            showToast(errorLists.errors.get(0).msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
