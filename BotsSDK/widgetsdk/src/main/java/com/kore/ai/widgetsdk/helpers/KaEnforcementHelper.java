package com.kore.ai.widgetsdk.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.events.KaAuthErrorCodes;
import com.kore.ai.widgetsdk.events.KaAuthenticationErrorEvents;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.room.models.AuthData;

public class KaEnforcementHelper {

    public static final String TAG = KaEnforcementHelper.class.getSimpleName();

    private static Context context;
    AlertDialog choiceAlertDialog;
    AlertDialog acceptAlertDialog;
    AlertDialog passWordExpiryDialog;
    public static String PACKAGE_NAME;

    public KaEnforcementHelper() {
        KoreEventCenter.register(KaEnforcementHelper.this);
    }

    public static void updateContext(Context mContext){
        context = mContext;
    }

    public void onEventMainThread(KaAuthenticationErrorEvents.OnEntAdminSettingsChanged event) {
//        Context context = KoreAppControl.getInstance().getTopActivityContext();
        /*if (event.errCode == KaAuthErrorCodes.PASSWORD_POLICY_CHANGED_CODE && context instanceof ChangePasswordActivity) {
            return;
        }*/

        if (isAlertDisplayable()) {
            onEntAdminSettingsChanged(event.errMsg, event.errCode);
        }
    }

    public void onEventMainThread(KaAuthenticationErrorEvents.PassWordExpiredNotificationEvent event){
        if (isAlertDisplayable()) {
            showPasswordExpAlertDialog(event.getExpDate());
        }
    }

    private boolean isAlertDisplayable() {
//        Context context = KoreAppControl.getInstance().getTopActivityContext();

        return !(choiceAlertDialog != null && choiceAlertDialog.isShowing() ||
                 acceptAlertDialog != null && acceptAlertDialog.isShowing()  ||
                 passWordExpiryDialog != null && passWordExpiryDialog.isShowing() ||
                  !isUserLoggedIn(context)
        );
    }

    private boolean isUserLoggedIn(Context context) {
        boolean isUserLogin = false;
        try {
            AuthData authData = UserDataManager.getAuthData();
            isUserLogin = authData != null;
        } catch (Exception e) {
//            KoreLogger.errorLog(TAG, "isUserLoggedIn() - Excep: " + e.getMessage(), e);
        }

        return isUserLogin;
    }

    private void onEntAdminSettingsChanged(String errMsg, int errorCode) {
        switch (errorCode) {
            case KaAuthErrorCodes.REFRESH_TOKEN_CODE:
                /*Context mContext = KoreAppControl.getInstance().getTopActivityContext();
                if (mContext != null) {
                    Intent refreshTokenIntent = new Intent(mContext, KoreService.class);
                    refreshTokenIntent.putExtra(KoreService.SERVICE_ID, ServiceIds.SERVICE_REFRESH_TOKEN);
                    mContext.startService(refreshTokenIntent);
                }*/
                break;

            case KaAuthErrorCodes.PASSWORD_POLICY_CHANGED_CODE:
            case KaAuthErrorCodes.PASSWORD_EXPIRED_CODE:
                showChoiceDialogBox(errMsg, errorCode);

                break;

            case KaAuthErrorCodes.ADMIN_PASSWORD_RESET_CODE:
            case KaAuthErrorCodes.MANAGED_BY_ENTERPRISE_CODE:
            case KaAuthErrorCodes.ADMIN_SSO_ENABLED_CODE:
            case KaAuthErrorCodes.ADMIN_SSO_DISABLED_CODE:
            case KaAuthErrorCodes.ADMIN_KILLED_SESSION_CODE:
            case KaAuthErrorCodes.ADMIN_ACCOUNT_SUSPENDED_CODE:
            case KaAuthErrorCodes.USAGE_POLICY_CHANGED:
            case KaAuthErrorCodes.PERMISSIONS_CHANGED:
            case KaAuthErrorCodes.COMPULSORY_LOGOUT:
            case KaAuthErrorCodes.ACCOUNT_DELETED:
                showAcceptDialogBox(errMsg, errorCode);
                break;

            case KaAuthErrorCodes.ERR_LOGIN_SUSPENDED_USER:
                //Don't show any popup for this case, as this is handling from calling part of service call.
                break;
            case KaAuthErrorCodes.MDM_AUTHENTICATION_REQUIRED:
            case KaAuthErrorCodes.MDM_UPDATED:
                showAcceptDialogBox(errMsg, errorCode);
                break;
            default:
//                showAcceptDialogBox(errMsg, errorCode);
                break;
        }
    }

    private void showChoiceDialogBox(String errMsg, int errCode) {
        String msg;

//        Context mContext = KoreAppControl.getInstance().getTopActivityContext();

        if(context == null)
            return;

        switch (errCode) {
            case KaAuthErrorCodes.PASSWORD_POLICY_CHANGED_CODE:
                msg = context.getResources().getString(R.string.ka_password_policy_changed_msg);
                break;
            case KaAuthErrorCodes.PASSWORD_EXPIRED_CODE:
                msg = context.getResources().getString(R.string.ka_password_expired_msg);
                break;
            default:
                msg = context.getResources().getString(R.string.ka_default_msg);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.ka_ent_admin_title));
        builder.setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.ka_change_password), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changePassword();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.ka_logout), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutApp();
                        dialog.dismiss();
                    }
                });
        choiceAlertDialog = builder.create();
        choiceAlertDialog.setCancelable(false);
        choiceAlertDialog.setCanceledOnTouchOutside(false);
        choiceAlertDialog.show();
    }

    private void showAcceptDialogBox(final String errMsg, final int errorCode) {
        String title;
        String msg;
        String btn;

//        final Context mContext = KoreAppControl.getInstance().getTopActivityContext();

        if (context == null)
            return;

        msg = context.getResources().getString(R.string.ka_ent_admin_msg);
        title = context.getResources().getString(R.string.ka_ent_admin_title);
        btn = context.getResources().getString(R.string.ka_logout);

        switch (errorCode) {
            case KaAuthErrorCodes.ADMIN_PASSWORD_RESET_CODE:
                msg = context.getResources().getString(R.string.ka_admin_password_reset_msg);
                break;
            case KaAuthErrorCodes.PERMISSIONS_CHANGED:
            case KaAuthErrorCodes.MANAGED_BY_ENTERPRISE_CODE:
                if (errMsg.equals(context.getResources().getString(R.string.ka_permissions_code))) {
                    title="Change in Permissions";
                    msg = context.getResources().getString(R.string.ka_permissions_changed_msg);
                    btn = context.getResources().getString(R.string.ka_ok);
                } else {
                    msg = context.getResources().getString(R.string.ka_licence_changed_msg);
                }
                break;
            case KaAuthErrorCodes.ADMIN_SSO_ENABLED_CODE:
                msg = context.getResources().getString(R.string.ka_admin_sso_enabled_msg);
                break;
            case KaAuthErrorCodes.ADMIN_SSO_DISABLED_CODE:
                msg = context.getResources().getString(R.string.ka_admin_sso_disabled_msg);
                break;
            case KaAuthErrorCodes.ADMIN_KILLED_SESSION_CODE:
                msg = context.getResources().getString(R.string.ka_admin_killed_session_msg);
                break;
            case KaAuthErrorCodes.ADMIN_ACCOUNT_SUSPENDED_CODE:
                msg = context.getResources().getString(R.string.ka_admin_account_suspended_msg);
                btn = context.getResources().getString(R.string.ka_ok);
                break;
            case KaAuthErrorCodes.USAGE_POLICY_CHANGED:
           // case KaAuthErrorCodes.USER_LOG_OFF:
                msg = context.getResources().getString(R.string.ka_admin_usage_policy_changes_msg);
                break;
            case KaAuthErrorCodes.ACCOUNT_DELETED:
                msg = context.getString(R.string.ka_account_deleted);
                btn = context.getResources().getString(R.string.ka_ok);
                break;
            /*case KaAuthErrorCodes.MDM_AUTHENTICATION_REQUIRED:
            case KaAuthErrorCodes.MDM_UPDATED:
                if (errMsg.equals(context.getResources().getString(R.string.ka_mdm_authorization_reqd_err_string)) ||
                        errMsg.equals(context.getResources().getString(R.string.ka_denied_non_mdm)) ) {
                    title = context.getResources().getString(R.string.ka_download_kore_for_good);
                    msg = context.getString(R.string.ka_download_kore_for_good_message);
                    btn = context.getResources().getString(R.string.ka_getApp);

                } else if (errMsg.equals(context.getResources().getString(R.string.ka_mdm_updated_err_string))) {
                    title = context.getResources().getString(R.string.ka_login_again);
                    msg = context.getString(R.string.ka_log_in_description);
                    btn = context.getResources().getString(R.string.ka_ok);
                }
                break;*/

            case KaAuthErrorCodes.COMPULSORY_LOGOUT:
                title = context.getResources().getString(R.string.ka_session_end_title);
                msg = context.getResources().getString(R.string.ka_session_end_msg);
                btn = context.getResources().getString(R.string.ka_ok);
                break;

            default:
                return;
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg)
                .setPositiveButton(btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (errorCode == KaAuthErrorCodes.ACCOUNT_DELETED) {
//                            EntAdminEventPublisher.publishAccountDeletedEvent();
                        }/*else if(errorCode == KaAuthErrorCodes.MDM_AUTHENTICATION_REQUIRED &&( errMsg.equals(context.getResources().getString(R.string.ka_mdm_authorization_reqd_err_string))
                        || errMsg.equals(context.getResources().getString(R.string.ka_denied_non_mdm)))) {
                            final String appPackageName = "com.kore.androidmdm";
                            logoutApp();
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }

                        }*/else {
                            logoutApp();
                        }
                        dialog.dismiss();
                    }
                });

        acceptAlertDialog = builder.create();
        if(errorCode == KaAuthErrorCodes.MDM_AUTHENTICATION_REQUIRED)
            acceptAlertDialog.setCancelable(true);
        else
            acceptAlertDialog.setCancelable(false);
        acceptAlertDialog.setCanceledOnTouchOutside(false);
        if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
            acceptAlertDialog.show();


    }

    private void showPasswordExpAlertDialog(String expDate){

//        Context context = KoreAppControl.getInstance().getTopActivityContext();

        String msg = context.getResources().getString(R.string.ka_password_policy_expire_alert_msg);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.ka_ent_admin_title));
        builder.setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.ka_change_password), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changePassword();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.ka_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        passWordExpiryDialog = builder.create();
        passWordExpiryDialog.setCanceledOnTouchOutside(false);
        passWordExpiryDialog.show();
    }

    private void changePassword() {
        KoreEventCenter.post(new KaAuthenticationErrorEvents.PasswordChangeEvent());
    }

    private void logoutApp() {
        KoreEventCenter.post(new KaAuthenticationErrorEvents.LogoutEvent());
    }

    public void onParentDestroy() {
        KoreEventCenter.unregister(KaEnforcementHelper.this);
    }

    public void pause() {
        KoreEventCenter.unregister(KaEnforcementHelper.this);
    }

    public void resume() {
        KoreEventCenter.register(KaEnforcementHelper.this);
    }


    /*
        Start -- OnEntAdminLimitations Events (team invitations limited on invite/on accept team request)
     */

    public void onEventMainThread(KaAuthenticationErrorEvents.OnEntAdminLimitations event) {
        onEntAdminLimitations(event.errMsg, event.errCode);
    }

    private void onEntAdminLimitations(String errMsg, String errorCode) {
        switch (errorCode) {
            case KaAuthErrorCodes.INSUFFICIENT_SEATS_IN_SPACE:
                showEntAdminLimitationsChoiceDialogBox(errMsg, errorCode);
                break;
            case KaAuthErrorCodes.SPACE_LIMIT_REACHED:
//                Context context = KoreAppControl.getInstance().getTopActivityContext();
                if(context != null) {
                    String errMsgToDisplay = context.getResources().getString(R.string.ka_no_licences_available_msg);
                    Toast.makeText(context, errMsgToDisplay, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void showEntAdminLimitationsChoiceDialogBox(String errMsg, String errCode) {

        String msg = null;

//        Context context = KoreAppControl.getInstance().getTopActivityContext();

        if(context == null)
            return;

        switch (errCode) {
            case KaAuthErrorCodes.INSUFFICIENT_SEATS_IN_SPACE:
                msg = context.getResources().getString(R.string.ka_no_licences_available_msg);
                break;
        }

        if(msg != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.ka_no_licences_available_title));
            builder.setMessage(msg)
                    .setPositiveButton(context.getResources().getString(R.string.ka_no_licence_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            choiceAlertDialog = builder.create();
            choiceAlertDialog.setCancelable(false);
            choiceAlertDialog.setCanceledOnTouchOutside(false);
            choiceAlertDialog.show();
        }
    }
}
