package kore.botssdk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.net.KoreLoginRequest;
import kore.botssdk.net.KoreRestResponse;
import kore.botssdk.utils.Contants;

/**
 * Created by Pradeep Mahato on 26-May-16.
 */
public class MainActivity extends BaseSpiceActivity {

    EditText userNameEdittext;
    EditText userNameEditPassword;
    Button loginBtn;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();
    }

    private void findViews() {
        userNameEdittext = (EditText) findViewById(R.id.userNameEdittext);
        userNameEditPassword = (EditText) findViewById(R.id.userNameEditPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
    }

    private void setListeners() {
        loginBtn.setOnClickListener(onLoginOnClickListener);
    }

    /**
     * Start of : Listeners
     */

    View.OnClickListener onLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!userNameEdittext.getText().toString().isEmpty() &&
                    !userNameEditPassword.getText().toString().isEmpty()) {
                invokeSignInServiceCall(userNameEdittext.getText().toString(), userNameEditPassword.getText().toString());
            } else {
                CustomToast.showToast(getApplicationContext(), "Invalid/empty credentials !!");
            }
        }
    };

    /**
     * End of : Listeners
     */

    /**
     * Start of : Utility Methods
     */

    private boolean saveCredsToPreferences(String userId, String accessToken) {
        boolean savedSuccessfully = false;
        sharedPreferences = getSharedPreferences(Contants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Contants.USER_ID, userId);
        editor.putString(Contants.ACCESS_TOKEN, accessToken);
        savedSuccessfully = editor.commit();

        if (savedSuccessfully) {
            CustomToast.showToast(getApplicationContext(), "Saved to pref");
        } else {
            CustomToast.showToast(getApplicationContext(), "Failed to save to pref");
        }

        return savedSuccessfully;
    }

    private void launchBotHomeActivity() {
        Intent intent = new Intent(getApplicationContext(), BotHomeActivity.class);
        startActivity(intent);
    }

    /**
     * End of : Utility Methods
     */

    /**
     * Start of : Service Calls
     */

    private void invokeSignInServiceCall(String userName, String password) {

        HashMap<String, Object> credMap = new HashMap<String, Object>();
        credMap.put("username", userName);
        credMap.put("password", password);
        credMap.put("scope", "friends");
        credMap.put("client_secret", "1");
        credMap.put("client_id", "1");
        credMap.put("grant_type", "password");

        KoreLoginRequest koreLoginRequest = new KoreLoginRequest(this, credMap);

        getSpiceManager().execute(koreLoginRequest, new RequestListener<KoreRestResponse.KoreLoginResponse>() {
            @Override
            public void onRequestFailure(SpiceException mException) {
                CustomToast.showToast(getApplicationContext(), "onRequestFailure !!");
            }

            @Override
            public void onRequestSuccess(KoreRestResponse.KoreLoginResponse koreLoginResponse) {
                String userId = koreLoginResponse.getUserInfo().getUserId();
                String authToken = koreLoginResponse.getAuthInfo().getAccessToken();

                boolean successfullySaved = saveCredsToPreferences(userId, authToken);

                if (successfullySaved) {
                    launchBotHomeActivity();
                }
            }
        });

    }

    /**
     * End of : Service Calls
     */

}
