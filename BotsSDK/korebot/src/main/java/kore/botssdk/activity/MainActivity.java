package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.utils.BotSharedPreferences;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.net.LoginRequest;
import kore.botssdk.net.RestResponse;

/**
 * Created by Pradeep Mahato on 26-May-16.
 */
public class MainActivity extends BaseSpiceActivity {

    EditText userNameEdittext;
    EditText userNameEditPassword;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();
        if (isAlreadyLoggedIn()) {
            launchBotHomeActivity();
            finish();
        } else {
            saveToPrefAndLaunch();
        }
    }

    private void findViews() {
        userNameEdittext = (EditText) findViewById(R.id.userNameEdittext);
        userNameEditPassword = (EditText) findViewById(R.id.userNameEditPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
    }

    private void setListeners() {
        loginBtn.setOnClickListener(onLoginOnClickListener);
    }

    private boolean isAlreadyLoggedIn() {
        return BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext()) != null;
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


    private void launchBotHomeActivity() {
        Intent intent = new Intent(getApplicationContext(), BotHomeActivity.class);
        startActivity(intent);
    }

    private void saveToPrefAndLaunch() {
        String userId = "u-73364365-f98d-571d-8e8e-022186cde3bc";//koreLoginResponse.getUserInfo().getUserId();
        String authToken = "mO6emCF8p-rOpDR-cJbQCKg9yTFLqxF_nHEe_d6ZcyBtDnDf3DYpPs9RG3qnqZlS";//koreLoginResponse.getAuthInfo().getAccessToken();

        boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(MainActivity.this,userId, authToken);

        if (successfullySaved) {
            launchBotHomeActivity();
        }
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

        LoginRequest loginRequest = new LoginRequest(this, credMap);

        getSpiceManager().execute(loginRequest, new RequestListener<RestResponse.LoginResponse>() {
            @Override
            public void onRequestFailure(SpiceException mException) {
                CustomToast.showToast(getApplicationContext(), "onRequestFailure !!");
            }

            @Override
            public void onRequestSuccess(RestResponse.LoginResponse koreLoginResponse) {
                String userId = koreLoginResponse.getUserInfo().getUserId();
                String authToken = koreLoginResponse.getAuthInfo().getAccessToken();

                boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(MainActivity.this,userId, authToken);

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
