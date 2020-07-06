package com.kore.ai.widgetsdk.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kore.ai.widgetsdk.events.KaAuthErrorCodes;
import com.kore.ai.widgetsdk.events.KaAuthenticationErrorEvents;
import com.kore.ai.widgetsdk.events.KoreEventCenter;
import com.kore.ai.widgetsdk.managers.UserDataManager;
import com.kore.ai.widgetsdk.net.netutils.AuthErrorCodes;
import com.kore.ai.widgetsdk.room.models.AuthData;
import com.kore.ai.widgetsdk.room.models.KORestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

//import com.kore.korelib.events.AccessTokenUpdateEvent;


/**
 * Created by Ramachandra Pradeep on 04-Jan-19.
 */

public class KoraRequestAuthenticator implements Authenticator {
    private Context mContext;
    private String newToken;
    private boolean tokenRefreshed = false;

    public KoraRequestAuthenticator(Context mContext) {
        this.mContext = mContext;
    }


    //Restricting cloning of this class
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException("Clone not supported");
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
//        Headers he = response.request().headers();
//        Log.d("IKIDO","Authenticate called for "+response.request().url());

//        showToast();
//        String oldToken = he.get("Authorization");
//        synchronized (KoraRequestAuthenticator.class) {
//            if (!StringUtils.isNullOrEmpty(newToken) && newToken.equals(oldToken)) {
//                return response.request().newBuilder()
//                        .header("Authorization", newToken)
//                        .build();
//            } else {
//                if(!shouldRefreshAccessToken(new StringBuffer(response.body().string()).toString(),response.code())) return response.request();
//                if(response.code() != 401) return response.request();


                try {
                    /*if(!tokenRefreshed) {
                        String refreshToken = getRefreshTokenFromDB();

                        tokenRefreshed = refreshAccessToken(refreshToken);
                    }

                    Log.d("IKIDO","Hey I am going");
                    if(tokenRefreshed) {
                        return response.request().newBuilder()
                                .header("Authorization", newToken)
                                .build();
                    }else*/ if(response.code() == 401) {
                        KoreEventCenter.post(new KaAuthenticationErrorEvents.OnEntAdminSettingsChanged("Your session will now end. Please re-login again.",
                                KaAuthErrorCodes.COMPULSORY_LOGOUT));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    /*if (e instanceof RefreshTokenExpiredException) {
                        if (e.getMessage().equals("invalid_grant") && ((RefreshTokenExpiredException) e).getErrCode().equals("42")) {
                            KoreEventCenter.post(new KaAuthenticationErrorEvents.OnEntAdminSettingsChanged(mContext.getResources().getString(R.string.admin_usage_policy_changes_msg),
                                    KaAuthErrorCodes.COMPULSORY_LOGOUT));
                        }
                    }*/
                }
//            }
//        }


        return null;
    }
    private boolean refreshAccessToken(String oldToken)throws Exception{
        HashMap<String, Object> userDetails = new HashMap<String, Object>();
        userDetails.put("client_id", "1");
        userDetails.put("client_secret", "1");
        userDetails.put("grant_type", "refresh_token");
        userDetails.put("scope", "friends");
        userDetails.put("refresh_token", oldToken);
        Call<KORestResponse.KOLoginResponse> refreshReq = KaRestBuilder.getKaRestAPI().refreshAccessToken(userDetails);
//        try{
        retrofit2.Response<KORestResponse.KOLoginResponse> resp = refreshReq.execute();
        if(resp.isSuccessful()){
            KORestResponse.KOLoginResponse respBody = resp.body();
            persistAuthInfo(respBody.getAuthData());


            //For updating refresh authInfo in kore service
//                KoreEventCenter.post(new AccessTokenRefreshEvent(authInfoDao));

            //For updating authInfo in other classes
           /* AccessTokenUpdateEvent accessTokenUpdateEvt = new AccessTokenUpdateEvent();
            accessTokenUpdateEvt.setAuthInfo(respBody.getAuthData());
            KoreEventCenter.post(accessTokenUpdateEvt);*/
            newToken =  "bearer " +respBody.getAuthData().getAccessToken();
            return true;
        }else{
            KaRestResponse.KoreStringErrorResponse errorLists = new Gson().fromJson(resp.errorBody().charStream(), KaRestResponse.KoreStringErrorResponse.class);
            String errorCode = errorLists.errors.get(0).code;
            String errorMessage = errorLists.errors.get(0).msg;

            if (errorMessage.equals("invalid_grant") && errorCode.equals("42")) {
                KoreEventCenter.post(new KaAuthenticationErrorEvents.OnEntAdminSettingsChanged("Your session will now end. Please re-login again.",
                        KaAuthErrorCodes.COMPULSORY_LOGOUT));
            }
            return false;
        }
    }
    private String getRefreshTokenFromDB() throws Exception {

        try {
            KORestResponse.KOLoginResponse response = UserDataManager.readUserDataInMainThread(mContext);
            return response.getAuthData().getRefreshToken();
        } catch (Exception e) {
//            KoreLogger.debugLog("KoraRequestInterceptor", "Error while fetching signedin users");
        } finally {
        }

        return null;
    }
    /*private Toast mToastToShow;
    private CountDownTimer toastCountDown;
    public void showToast() {

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                // Set the toast and duration
                int toastDurationInMilliSeconds = 5000;
                mToastToShow = Toast.makeText(mContext, "Refreshing your session, Please wait...", Toast.LENGTH_LONG);
                mToastToShow.setGravity(Gravity.CENTER, 0, 0);
                // Set the countdown to display the toast
                toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 *//*Tick duration*//*) {
                    public void onTick(long millisUntilFinished) {
                        mToastToShow.show();
                    }
                    public void onFinish() {
                        mToastToShow.cancel();
                    }
                };

                // Show the toast and starts the countdown
                mToastToShow.show();
                toastCountDown.start();
            }
        });


    }*/

    private void persistAuthInfo(AuthData authInfo) throws SQLException {
        UserDataManager.updateAuthData(mContext,authInfo,null);
    }
    /*public RefreshTokenExpiredException takeActionOnExpiryOfRefreshToken(ResponseBody responseBody) {
        int intValueOfChar;
//        String targetString = "";
        KaRestResponse.KoreStringErrorResponse errorLists;
        try {
           *//* while ((intValueOfChar = responseBody.byteStream().read()) != -1) {
                targetString += (char) intValueOfChar;
            }*//*
            errorLists = new Gson().fromJson(responseBody.charStream(), KaRestResponse.KoreStringErrorResponse.class);
            String errorCode = errorLists.errors.get(0).code;
            String errorMessage = errorLists.errors.get(0).msg;
            RefreshTokenExpiredException ex = new RefreshTokenExpiredException();
            ex.setErrCode(errorCode);
            return ex;
        } catch (Exception e) {
            e.printStackTrace();
            RefreshTokenExpiredException ex = new RefreshTokenExpiredException();
            ex.setErrCode("-1");
            return ex;
        }
    }*/

    private int processErrorDesc(String respErrDesc) {
        String msg;
        int code = -1;
        try {
            HashMap<String, Object> hshError = (HashMap) toMap(respErrDesc);
            ArrayList<HashMap<String, Object>> subError = (ArrayList<HashMap<String, Object>>) hshError.get("errors");
            msg = (String) subError.get(0).get("msg");
            if (subError.get(0).get("code") instanceof Integer) {
                code = (int) subError.get(0).get("code");
            } else if (subError.get(0).get("code") instanceof String) {
                String sCode = (String) subError.get(0).get("code");
                if (msg.equals("INVALID_ACCESS_TOKEN") && sCode.equals("TOKEN_EXPIRED")) {
                    code = KaAuthErrorCodes.REFRESH_TOKEN_CODE;
                }
            } else {
                code = (int) Double.parseDouble(subError.get(0).get("code").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }
    public static Map<String, Object> toMap(String jsonData) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject object = new JSONObject(jsonData);
        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap(value.toString());
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap(value.toString());
            }
            list.add(value);
        }
        return list;
    }

    private boolean shouldRefreshAccessToken(String bodyMsg, int respCode){
        boolean srat = true;
            switch (respCode) {
                case 401: {
                    try {
                        String msg;
                        int code = -1;
                        HashMap<Object,Object> hshError = new Gson().fromJson(bodyMsg,HashMap.class);
                        List<LinkedTreeMap<Object,Object>> subError = (List<LinkedTreeMap<Object, Object>>) hshError.get("errors");
                        msg = (String)subError.get(0).get("msg");
                        if(subError.get(0).get("code") instanceof Integer){
                            code = (int)subError.get(0).get("code");
                            srat = true;
                        }else if(subError.get(0).get("code") instanceof String){
                            String sCode = (String)subError.get(0).get("code");
                            if(msg.equals("INVALID_ACCESS_TOKEN") && sCode.equals("TOKEN_EXPIRED")){
                                code = AuthErrorCodes.REFRESH_TOKEN_CODE;
                                srat = true;
                            }
                        }else{
                            code = (int) Double.parseDouble(subError.get(0).get("code").toString());
                            if(code == 41 && msg.equals("INVALID_ACCESS_TOKEN") ){
                                //No need to call here as we are doing in KoreRequestInterceptor
                                code = AuthErrorCodes.REFRESH_TOKEN_CODE;
                                srat = true;
                            }else if(code == 61 && msg.equals("PERMISSIONS_CHANGED")){
                                srat = false;
                                KoreEventCenter.post(new KaAuthenticationErrorEvents.OnEntAdminSettingsChanged(msg, code));
                                break;
                            }
                        }
//                        KoreRestResponse.KoreErrorResponse responseBody = (KoreRestResponse.KoreErrorResponse) retrofitError.getBodyAs(KoreRestResponse.KoreErrorResponse.class);
//                        KoreEventCenter.post(new KaAuthenticationErrorEvents.OnEntAdminSettingsChanged(msg, code));
                    }catch (Exception e){
                        e.printStackTrace();
                        /*try {
                            KoreRestResponse.KoreErrorRespForAC bodyAC = (KoreRestResponse.KoreErrorRespForAC) (retrofitError.getBodyAs(KoreRestResponse.KoreErrorRespForAC.class));
                            if(bodyAC.errors.get(0).getMsg().equals("INVALID_ACCESS_TOKEN") && bodyAC.errors.get(0).getCode().equals("TOKEN_EXPIRED")){
                                Intent refreshTokenIntent = new Intent(context, KoreService.class);
                                refreshTokenIntent.putExtra(KoreService.SERVICE_ID, ServiceIds.SERVICE_REFRESH_TOKEN);
                                context.startService(refreshTokenIntent);
                            }
                        }catch(Exception es){
                        }*/
                    }
                    break;
                }
                case 403: {
                    try {
                        srat = false;
                        KaRestResponse.KoreErrorResponseString responseBody = new Gson().fromJson(bodyMsg,KaRestResponse.KoreErrorResponseString.class);
                        KoreEventCenter.post(new KaAuthenticationErrorEvents.OnEntAdminSettingsChanged(responseBody.getFirstMessage(), Integer.parseInt(responseBody.getFirstErrorCode())));
                    }catch (Exception ex){
                        ex.printStackTrace();
                        //MDM
                        /*try {
                            KaRestResponse.KoreErrorRespForAC bodyAC = (KaRestResponse.KoreErrorRespForAC) (retrofitError.getBodyAs(KaRestResponse.KoreErrorRespForAC.class));
                            int errorCode = Integer.parseInt(bodyAC.errors.get(0).getCode());
                            String errorMsg = bodyAC.errors.get(0).getMsg();
                            if(errorMsg.equals(context.getResources().getString(R.string.mdm_authorization_reqd_err_string)) && errorCode == AuthErrorCodes.MDM_AUTHENTICATION_REQUIRED){
                                KoreEventCenter.post(new AuthenticationErrorEvents.OnEntAdminSettingsChanged(errorMsg,errorCode));
                            }
                        }catch(Exception es){

                        }*/
                    }
                    break;
                }
            }
        return srat;
    }
}
