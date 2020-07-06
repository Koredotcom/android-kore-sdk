package com.kore.ai.widgetsdk.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.utils.AppUtils;
import com.kore.ai.widgetsdk.utils.ToastUtils;


/**
 * Created by Shiva Krishna on 11/17/2017.
 * Name: BaseActivity
 * Description:  This class is to get the global use in the application.
 **/

public abstract class KaBaseFragment extends Fragment {
    protected final String LOG_TAG = this.getClass().getSimpleName();
    private ProgressDialog mProgressDialog;
    protected int DP1;
//	protected KoreFontProvider appFont ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Method to show progress dialog
     *
     * @param msg to display on dialog.
     */
    protected void showProgress(String msg) {
        showProgress(msg, false);

    }

    protected void showProgress(String msg, boolean cancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        if (getActivity() != null) {
            mProgressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.app_name), msg);
            mProgressDialog.setContentView(R.layout.ka_progress_indicator); //Custom progress indicator
            mProgressDialog.setCancelable(cancelable);
        }
    }

    /**
     * Method to show progress dialog with UI blocking
     * Recommended to use where the UI needs to be blocked for a short time to prevent any onClicks.
     */
    protected void showProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        if (getActivity() != null) {
            mProgressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.app_name), "Please wait ..");
            mProgressDialog.setContentView(R.layout.ka_progress_indicator); //Custom progress indicator
            mProgressDialog.setCancelable(false);
        }

    }

    /**
     * Method to dismiss the progress dialog
     */
    protected void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected final void showToast(String msg) {
        ToastUtils.showToast(getActivity(), msg);
    }

    protected final void showToast(String msg, int length) {
        ToastUtils.showToast(getActivity(), msg, length);
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
        ToastUtils.showToast(getActivity(), msg, length, gravity, xOffset, yOffset);
    }

    /**
     * Method to show Alert
     *
     * @param msg Message to be displayed
     */
    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    /**
     * Method to hide the softkeyboard on android
     */
    protected void hideSoftKeyBoard(View view) {
        AppUtils.showHideVirtualKeyboard(getActivity(), view, false);
    }

    protected void hideSoftKeyBoard() {
        AppUtils.toggleVirtualKeyboard(getActivity(), InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Method to hide softkeyboard on touching view
     *
     * @param view
     */
    public void onTouchHideKeyboard(final View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //Activity SignUpActivity=(Activity) v.getContext();
                    hideSoftKeyBoard(view);
                    return false;
                }

            });
        }
        //If view happens to be a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                onTouchHideKeyboard(innerView);
            }
        }
    }

    public void invalidateActionBar(FragmentActivity fragmentActivity) {
        if (fragmentActivity != null) {
            fragmentActivity.supportInvalidateOptionsMenu();
        }
    }

    public Gson gson = new Gson();

    public void showErrorToast(Throwable throwable) {

    }

    private void showUserInteractionPopUpOrToast(String code, String message) {
        switch (code.trim()) {
            case "QuestionAlreadyExist":
                showPopUp("Duplicate Question", message);
                break;
            case "InvalidQuestion":
                showPopUp("Trigger question is too generic", message);
                break;
            default:
                if (message != null && !message.equals("INVALID_ACCESS_TOKEN"))
                    showToast(message);
        }
    }

    void showPopUp(String header, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(header);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}
