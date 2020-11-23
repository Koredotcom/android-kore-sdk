package kore.botssdk.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kore.botssdk.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shiva Krishna on 11/17/2017.
 */

public class AppUtils {

    public static final String URL_REGEX = "(http|ftp|https)://|([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?";//"^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            dp = 1.4f;
        }
        return dp * (metrics.densityDpi / 160f);
    }



    public static Map getMapObject(Object params)
    {

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(params);// obj is your object
        Map<String,Object> result = new Gson().fromJson(json, Map.class);

        return result;
    }
    public static void showHideVirtualKeyboard(Activity activity, View view, boolean show) {
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (show) { //show keyboard
            if (view == null) {
                return;
            }

            imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);

        } else {  // hide keyboard

            View focusView = view == null ? activity.getCurrentFocus() : view;
            if (focusView == null) {
                return;
            }


            imm.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public static void toggleVirtualKeyboard(Activity activity, int showFlags, int hideFlags) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(showFlags, hideFlags);
    }

    public static int getActionBarHeight(AppCompatActivity activity) {
        int actionBarHeight = activity.getSupportActionBar().getHeight();

        if (actionBarHeight != 0) {
            return actionBarHeight;
        }

        final TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());

        if (actionBarHeight == 0) {
            actionBarHeight = 50;
        }

        return actionBarHeight;
    }

    public static boolean hasUrl(String input) {
        StringTokenizer tokens = new StringTokenizer(input, " ");
        while (tokens.hasMoreElements()) {
            String token = tokens.nextToken();
            if (Utils.isWebURL(token)) {
                 return true;
            }
        }

        return false;
    }

    public static boolean hasUrlInDetails(String input){
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(input);//replace with string to compare
        if(m.find()) {
            return true;
        }
        return false;
    }

//    private static FCMManager fcmManager;
    private static Activity mContexts;

//    public static void registerForGCM(final Activity mContext) {
//        mContexts = mContext;
//        if (FCMManager.checkPlayServices(mContext)) {
//            if (fcmManager == null) {
//                fcmManager = new FCMManager(mContext);
//                fcmManager.setListener(new FCMManager.FcmListener() {
//                    @Override
//                    public void onReceivedId(String regId) {
//                        registerPushNotifications(regId);
//                        KoreLogger.debugLog("GCM Log", "Registration id is : " + regId);
//                    }
//                });
//            }
//            fcmManager.registerIfRequired();
//        }
//    }
//    private static void registerForGCM(){
//        registerForGCM(mContexts);
//    }

//    public static void registerPushNotifications(String regId) {
//        if (!regId.isEmpty()) {
//            HashMap<String, Object> credMap = PushNotificationUtils.getPNSMap(regId, mContexts);
//            KoreLogger.debugLog("GCM Log", "cred: " + credMap);
//
//            try {
//                if (UserDataManager.getAuthData() != null) {
//                    Call<ResponseBody> subscribePush = KaRestBuilder.getKaRestAPI().registerForPush(UserDataManager.getUserData().getId(),
//                            kore.botssdk.utils.Utils.ah(UserDataManager.getAuthData().getAccessToken()), credMap);
//                    KaRestAPIHelper.enqueueWithRetry(subscribePush, new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            try {
//                                if (response != null) {
//                                    if (response.isSuccessful()) {
//                                        KoreLogger.debugLog("GCM Log", "success register push notifications");
//                                    } else if (response.code() == 401) {
//                                        KaRestResponse.KoreErrorResponse errorLists = new Gson().fromJson(response.errorBody().charStream(),
//                                                KaRestResponse.KoreErrorResponse.class);
//                                        if (errorLists != null && errorLists.errors != null && errorLists.errors.get(0).code == 41) {
//                                            registerForGCM();
//                                        }
//                                    } else if (response.code() == 403) {
//                                        registerForGCM();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.d("AppUtils", "Failed to register push");
//                        }
//                    });
//
//                }
//            } catch (Exception e) {
//                KoreLogger.debugLog("GCM Log", "Error in register push notifications", e);
//            }
//        } else {
//            registerForGCM();
//        }
//    }
//    public static boolean isMainActivityRunning(Context mContext) {
//        return SharedPreferenceUtils.getInstance(mContext).getKeyValue("active", false);
//    }

    public static void showSnackBar(Activity mActivity,String msg){
        Snackbar snackbar = Snackbar
                .make(mActivity.getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(mActivity.getResources().getColor(R.color.white));
        TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(mActivity.getResources().getColor(R.color.color_485260));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(18);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_dontshare, 0, R.drawable.ic_icon_share, 0);
        snackbar.show();


        /*Snackbar snackbar = Snackbar.make(mActivity.getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbar.getView().setBackgroundColor(mActivity.getResources().getColor(R.color.white));

        TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setText("                    ");
        textView.setGravity(Gravity.CENTER_VERTICAL);

        View snackView = mActivity.getLayoutInflater().inflate(resourceId, null);

        layout.setPadding(0,0,0,0);

        layout.addView(snackView, 0);
        snackbar.show();*/
    }

}