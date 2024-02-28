package kore.botssdk.audiocodes.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class OAuthManager {

    protected static final String TAG = "OAUTH";

    private boolean active;

    private EventsCallback eventsCallback;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editorSP;
    private static final String PREFS_KEY = "oauth_prefs";
    private static final String REFRESH_TOKEN_KEY = "refreshToken";
    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String URL_KEY = "url";
    private static final String REALM_KEY = "realm";
    private static final String CLINET_ID_KEY = "clientId";

    private static OAuthManager instance;
    public static synchronized OAuthManager getInstance() {
        if (instance == null) {
            instance = new OAuthManager();
        }
        return instance;
    }

    public boolean isEnabled(){
        return getValue(ACCESS_TOKEN_KEY) != null;
    }

    public void initialize(Context context, EventsCallback eventsCallback){
        Log.d(TAG, "initialize");
        this.eventsCallback = eventsCallback;
        sharedPreferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        editorSP = sharedPreferences.edit();
        checkToken();
    }

    public void setClientId(String clientId){
        putValue(CLINET_ID_KEY, clientId);
    }

    public void setURL(String url){
        putValue(URL_KEY, url);
    }

    public void setRealm(String realm){
        putValue(REALM_KEY, realm);
    }

    public void authorize(String userName, String password, LoginCallback loginCallback) {
        if(userName != null && password != null){
            putValue(REFRESH_TOKEN_KEY, null);
            Log.d(TAG, "authorize");
            new AuthorizeTask(userName, password, loginCallback).execute();
        }
        else{
            Log.d(TAG, "userName || password null");
        }
    }

    private class AuthorizeTask extends AsyncTask<Void, Void, Void> {

        private String userName, password;
        private LoginCallback loginCallback;
        private JSONObject jsonObject;

        public AuthorizeTask(String userName, String password, LoginCallback loginCallback) {
            this.userName = userName;
            this.password = password;
            this.loginCallback = loginCallback;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handleResults(jsonObject, loginCallback);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HttpURLConnection connection = HttpManager.getConnection(makeURL());
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                StringBuilder body = new StringBuilder();
                body.append("grant_type=").append("password")
                        .append("&username=").append(userName)
                        .append("&password=").append(password)
                        .append("&client_id=").append(getValue(CLINET_ID_KEY));
                HttpManager.setBody(connection, body.toString());
                jsonObject = HttpManager.make(connection);
            } catch (Throwable e) {
                Log.e(TAG, "AuthorizeTask", e);
            }
            return null;
        }
    }

    private class RefreshTokenTask extends AsyncTask<Void, Void, Void> {

        private JSONObject jsonObject;

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handleResults(jsonObject, null);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HttpURLConnection connection = HttpManager.getConnection(makeURL());
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                StringBuilder body = new StringBuilder();
                body.append("grant_type=").append("refresh_token")
                        .append("&refresh_token=").append(getValue(REFRESH_TOKEN_KEY))
                        .append("&client_id=").append(getValue(CLINET_ID_KEY));
                HttpManager.setBody(connection, body.toString());
                jsonObject = HttpManager.make(connection);
            } catch (Throwable e) {
                Log.e(TAG, "RefreshTokenTask", e);
            }
            return null;
        }
    }

    private String makeURL(){
        String server = getValue(URL_KEY);
        String realm = getValue(REALM_KEY);
        StringBuilder url = new StringBuilder();
        url.append(server).append("realms/").append(realm).append("/protocol/openid-connect/token");
        return url.toString();
    }

    private void handleResults(JSONObject jsonObject, LoginCallback loginCallback){
        try {
            if(jsonObject != null && jsonObject.has("access_token")){
                Log.d(TAG, "success!");
                String access_token = jsonObject.getString("access_token");
                String expires_in = jsonObject.getString("expires_in");
                String refresh_expires_in = jsonObject.getString("refresh_expires_in");
                String refresh_token = jsonObject.getString("refresh_token");
                putValue(ACCESS_TOKEN_KEY, access_token);
                putValue(REFRESH_TOKEN_KEY, refresh_token);
                long tokenExpries = Long.parseLong(expires_in);
                setRefreshTokenAlarm(tokenExpries);
                active = true;
                eventsCallback.onUpdateToken(access_token);
                if(loginCallback != null) {
                    loginCallback.onAuthorize(true);
                }
            }
            else{
                Log.d(TAG, "fail!");
                if(loginCallback != null) {
                    loginCallback.onAuthorize(false);
                }
                else{
                    putValue(REFRESH_TOKEN_KEY, null);
                    eventsCallback.onRelogin();
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error", e);
            if(loginCallback != null) {
                loginCallback.onAuthorize(false);
            }
        }
    }

    protected void refreshToke(){
        Log.d(TAG, "refreshToke");
        new RefreshTokenTask().execute();
    }

    private void checkToken(){
        if(isEnabled()){
           // eventsCallback.onUpdateToken(getValue(ACCESS_TOKEN_KEY));
            Log.d(TAG, "token available");
            if(!active){
                refreshToke();
            }
        }
        else{
            Log.d(TAG, "no token available");
        }
    }

    protected void setRefreshTokenAlarm(long refreshTokenExpries) {
        long time = (long) (refreshTokenExpries * 0.9);
        OAuthReceiver.setRefreshTokenAlarm(time);
    }

    protected void setAccessTokenAlarm(long accessTokenExpries){
        long time = (long) (accessTokenExpries * 0.9);
        OAuthReceiver.setRefreshTokenAlarm(time);
    }

    private String getValue(String key){
        key = key.toLowerCase();
        String value = sharedPreferences.getString(key, null);
        return value;
    }

    private void putValue(String key, String value){
        key = key.toLowerCase();
        editorSP.putString(key, value);
        editorSP.commit();
    }

    public interface LoginCallback {
        public void onAuthorize(boolean success);
    }

    public interface EventsCallback {
        public void onRelogin();
        public void onUpdateToken(String token);
    }

    public String getURL(){
        return getValue(URL_KEY);
    }

    public String getRealm(){
        return getValue(REALM_KEY);
    }

    public String getClientId(){
        return getValue(CLINET_ID_KEY);
    }
}
