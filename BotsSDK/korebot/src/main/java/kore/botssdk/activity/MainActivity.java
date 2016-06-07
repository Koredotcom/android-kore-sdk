package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.utils.BotSharedPreferences;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.Contants;
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
    Button anonymousLoginBtn;
    Button normalLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();
        clearPref();
        getSupportActionBar().setSubtitle("Login");
    }

    private void findViews() {
        userNameEdittext = (EditText) findViewById(R.id.userNameEdittext);
        userNameEditPassword = (EditText) findViewById(R.id.userNameEditPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        anonymousLoginBtn = (Button) findViewById(R.id.anonymousLoginBtn);
        normalLoginBtn = (Button) findViewById(R.id.normalLoginBtn);

    }

    private void setListeners() {
        loginBtn.setOnClickListener(onLoginOnClickListener);
        anonymousLoginBtn.setOnClickListener(anonymousLoginBtnOnClickListener);
        normalLoginBtn.setOnClickListener(normalLoginBtnOnClickListener);
    }

    private void clearPref() {
        BotSharedPreferences.clearPreferences(MainActivity.this);
    }

    private boolean isAlreadyLoggedIn() {
        return BotSharedPreferences.getAccessTokenFromPreferences(getApplicationContext()) != null
                && BotSharedPreferences.getUserIdFromPreferences(getApplicationContext()) != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearPref();
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

    View.OnClickListener anonymousLoginBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            launchBotHomeActivity(true);
        }
    };

    View.OnClickListener normalLoginBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAlreadyLoggedIn()) {
                launchBotHomeActivity(false);
                finish();
            } else {
                saveToPrefAndLaunch(false);
            }
        }
    };

    /**
     * End of : Listeners
     */

    /**
     * Start of : Utility Methods
     */


    private void launchBotHomeActivity(boolean isAnonymous) {
        Intent intent = new Intent(getApplicationContext(), BotHomeActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(BundleUtils.LOGIN_MODE, (isAnonymous) ? Contants.ANONYMOUS_FLOW : Contants.NORMAL_FLOW);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void saveToPrefAndLaunch(boolean isAnonymous) {
        String userId = "u-73364365-f98d-571d-8e8e-022186cde3bc";//koreLoginResponse.getUserInfo().getUserId();
        String authToken = "mO6emCF8p-rOpDR-cJbQCKg9yTFLqxF_nHEe_d6ZcyBtDnDf3DYpPs9RG3qnqZlS";//koreLoginResponse.getAuthInfo().getAccessToken();

        boolean successfullySaved = BotSharedPreferences.saveCredsToPreferences(MainActivity.this,userId, authToken);

        if (successfullySaved) {
            launchBotHomeActivity(isAnonymous);
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
                    launchBotHomeActivity(false);
                }
            }
        });

    }

    /**
     * End of : Service Calls
     */

}
