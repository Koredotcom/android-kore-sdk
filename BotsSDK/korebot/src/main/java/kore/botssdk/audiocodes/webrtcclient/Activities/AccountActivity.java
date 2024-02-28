package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.audiocodes.mv.webrtcsdk.sip.enums.Transport;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.oauth.OAuthManager;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.audiocodes.webrtcclient.Structure.SipAccount;


public class AccountActivity extends BaseAppCompatActivity {

    private static final String TAG = "AccountActivity";

    private boolean oAuthEnable;

    private EditText secondCall, transferCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_activity);

        initGui();
    }

    private void initGui() {
        SipAccount sipAccount = Prefs.getSipAccount();


        EditText userNameEditText = (EditText) findViewById(R.id.account_editText_username);
        //userNameEditText.setText(sipAccount.getUsername());
        EditText passwordEditText = (EditText) findViewById(R.id.account_editText_password);
        EditText displayNameEditText = (EditText) findViewById(R.id.account_editText_displayname);
        EditText domainEditText = (EditText) findViewById(R.id.account_editText_domain);
        EditText sipAddressEditText = (EditText) findViewById(R.id.account_editText_sipaddress);
        EditText portEditText = (EditText) findViewById(R.id.account_editText_port);
        Spinner transportSpinner = (Spinner) findViewById(R.id.account_editText_transport);

        transportSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Transport.values()));
//        EditText turnServerEditText = (EditText)findViewById(R.id.login_editText_turnserver);
//        EditText stunserverEditText = (EditText)findViewById(R.id.login_editText_stunserver);

        Button loginButton = (Button) findViewById(R.id.account_button_login);

        secondCall = (EditText) findViewById(R.id.account_editText_second_call);
        transferCall = (EditText) findViewById(R.id.account_editText_transfer_call);
        secondCall.setText(Prefs.getSecondCall());
        transferCall.setText(Prefs.getTransferCall());

        TextView statusTextView = (TextView) findViewById(R.id.account_textview_status);
        String statusStr = getString(R.string.account_textview_status_prefix) + " " +
                (ACManager.getInstance().isRegisterState() ? getString(R.string.account_textview_status_connected) : getString(R.string.account_textview_status_disconnected));
        if (sipAccount != null && sipAccount.getUsername() != null)
        {
            try {
                statusStr+=", "+sipAccount.getUsername().toString();
            } catch (Exception e) {
                Log.e(TAG, "err: "+e);
            }
        }
        statusTextView.setText(statusStr);
        //boolean test = true;
        // if (test)
        //{
        userNameEditText.setText(sipAccount.getUsername());
        passwordEditText.setText(sipAccount.getPassword());
        displayNameEditText.setText(sipAccount.getDisplayName());
        domainEditText.setText(sipAccount.getDomain());
        sipAddressEditText.setText(sipAccount.getProxy());

        for (int i = 0; i < transportSpinner.getAdapter().getCount(); i++) {
            if (transportSpinner.getItemAtPosition(i).toString().toLowerCase().equals(sipAccount.getTransport().toString().toLowerCase())) {
                transportSpinner.setSelection(i);
            }
        }

        //userNameEditText.setText(sipAccount.getUsername());
        //userNameEditText.setText(sipAccount.getUsername());
        portEditText.setText(String.valueOf(sipAccount.getPort()));


        EditText oauthUrlEditText = (EditText)findViewById(R.id.login_editText_oauth_url);
        EditText oauthRealmEditText = (EditText)findViewById(R.id.login_editText_oauth_realm);
        EditText oauthClientIdEditText = (EditText)findViewById(R.id.login_editText_oauth_clientid);

        if(OAuthManager.getInstance().isEnabled()){
            oAuthEnable = true;
            LinearLayout layout = (LinearLayout) findViewById(R.id.oauth_layout);
            layout.setVisibility(View.VISIBLE);
            oauthUrlEditText.setText(OAuthManager.getInstance().getURL());
            oauthRealmEditText.setText(OAuthManager.getInstance().getRealm());
            oauthClientIdEditText.setText(OAuthManager.getInstance().getClientId());
            passwordEditText.setText("");
        }

        CheckBox autologin = (CheckBox) findViewById(R.id.autologin_checkBox);
        autologin.setChecked(Prefs.getAutoLogin());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (validateField(userNameEditText) && validateField(passwordEditText)) {

                    sipAccount.setUsername(userName);
                    if (!oAuthEnable) {
                        sipAccount.setPassword(password);
                    } else {
                        sipAccount.setPassword("no password");
                    }
                    //if(validateField(displayNameEditText)&&validateField(domainEditText)&&validateField(sipAddressEditText)&&validateField(turnServerEditText)&&validateField(stunserverEditText))
                    if (validateField(displayNameEditText) && validateField(domainEditText) && validateField(sipAddressEditText) && validateField(portEditText)) {
                        sipAccount.setDisplayName(displayNameEditText.getText().toString().trim());
                        sipAccount.setDomain(domainEditText.getText().toString().trim());
                        sipAccount.setProxy(sipAddressEditText.getText().toString().trim());
                        sipAccount.setTransport(AppUtils.getTransport(transportSpinner.getSelectedItem().toString().trim()));

                        int portNumber;
                        try {
                            portNumber = Integer.valueOf(portEditText.getText().toString().trim());
                        } catch (Exception e) {
                            //use default port number;
                            portNumber = Integer.valueOf(getString(R.string.sip_account_port_default));
                        }
                        sipAccount.setPort(portNumber);
                    }

                }

                Log.d(TAG, "start logout");
                Prefs.setSipAccount(sipAccount);
                handleOauth(oauthUrlEditText, oauthRealmEditText, oauthClientIdEditText,
                            userName, password, autologin.isChecked());
            }
        });
    }

    private void handleOauth(EditText oauthUrlEditText, EditText oauthRealmEditText, EditText oauthClientIdEditText, String userName, String password,
                             boolean autologin){
        boolean disconnectCall = Prefs.getDisconnectBrokenConnection();
        if(oAuthEnable){
            OAuthManager.getInstance().setURL(oauthUrlEditText.getText().toString());
            OAuthManager.getInstance().setRealm(oauthRealmEditText.getText().toString());
            OAuthManager.getInstance().setClientId(oauthClientIdEditText.getText().toString());
            OAuthManager.getInstance().authorize(userName, password, new OAuthManager.LoginCallback() {
                @Override
                public void onAuthorize(boolean success) {
                    if(success){
                        openNextScreen(autologin, disconnectCall);
                    }
                    else{
                        Toast.makeText(BotApplication.getGlobalContext(),getString(R.string.oauth_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            openNextScreen(autologin, disconnectCall);
        }
    }

    private boolean validateField(EditText editText) {
        if (editText != null && editText.getText() != null && !editText.getText().toString().trim().equals("")) {
            return true;
        }
        return false;
    }

    private void openNextScreen(boolean autologin, boolean disconnectCall) {
        Prefs.setSecondCall(secondCall.getText().toString());
        Prefs.setTransferCall(transferCall.getText().toString());
        Prefs.setFirstLogin(false);
        //start login and open app main screen
        ACManager.getInstance().startLogin(autologin, disconnectCall);
        startNextActivity(MainActivity.class);
        finish();
    }

}
