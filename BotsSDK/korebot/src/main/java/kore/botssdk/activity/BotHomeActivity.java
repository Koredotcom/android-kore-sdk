package kore.botssdk.activity;

import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;

import java.util.UUID;

import kore.botssdk.R;
import kore.botssdk.listener.BotSocketConnectionManager;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class BotHomeActivity extends BotAppCompactActivity implements ProviderInstaller.ProviderInstallListener{
    private Button launchBotBtn;
    EditText etIdentity;
    private static final int ERROR_DIALOG_REQUEST_CODE = 1;
    private boolean retryProviderInstall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_home_activity_layout);
        ProviderInstaller.installIfNeededAsync(this, this);

        findViews();
        setListeners();
    }

    private void findViews() {

        launchBotBtn = findViewById(R.id.launchBotBtn);
        etIdentity = findViewById(R.id.etIdentity);
        launchBotBtn.setText(getResources().getString(R.string.get_started));
        etIdentity.setText(SDKConfiguration.Client.identity);
        if(etIdentity.getText().length() > 0)
            etIdentity.setSelection(etIdentity.getText().toString().length());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.POST_NOTIFICATIONS)) {
                Log.e("Has Permission", "true");
            } else
            {
                KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST,
                        Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO);
            }
        }
    }

    private void setListeners() {
        launchBotBtn.setOnClickListener(launchBotBtnOnClickListener);
    }

    /**
     * START of : Listeners
     */
    final View.OnClickListener launchBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOnline())
            {
                if(!StringUtils.isNullOrEmpty(etIdentity.getText().toString()))
                {
                    if(StringUtils.isValidEmail(etIdentity.getText().toString()))
                    {
                        if(!SDKConfiguration.Client.isWebHook)
                        {
                            SDKConfiguration.Client.identity = UUID.randomUUID().toString();
                            BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithConfig(getApplicationContext(),null);
                            launchBotChatActivity();
                        }
                        else
                        {
                            launchBotChatActivity();
                        }
                    }
                    else
                        Toast.makeText(BotHomeActivity.this, "Please enter a valid Email.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(BotHomeActivity.this, "Please enter your Email.", Toast.LENGTH_SHORT).show();
            } else
                {
                Toast.makeText(BotHomeActivity.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * Launching BotchatActivity where user can interact with bot
     *
     */
    void launchBotChatActivity(){
        Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
        Bundle bundle = new Bundle();
        //This should not be null
        bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
        bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
        intent.putExtras(bundle);

        startActivity(intent);
    }
    @SuppressLint("MissingPermission")
    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }

    /**
     * This method is called if updating fails. The error code indicates
     * whether the error is recoverable.
     */
    @Override
    public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        if (availability.isUserResolvableError(errorCode)) {
            // Recoverable error. Show a dialog prompting the user to
            // install/update/enable Google Play services.
            availability.showErrorDialogFragment(
                    this,
                    errorCode,
                    ERROR_DIALOG_REQUEST_CODE,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // The user chose not to take the recovery action.
                            onProviderInstallerNotAvailable();
                        }
                    });
        } else {
            // Google Play services isn't available.
            onProviderInstallerNotAvailable();
        }
    }

    void onProviderInstallerNotAvailable() {
        // This is reached if the provider can't be updated for some reason.
        // App should consider all HTTP communication to be vulnerable and take
        // appropriate action.
        Log.e(LOG_TAG, "No Installer is Available");
    }

    @Override
    public void onProviderInstalled() {
        Log.e(LOG_TAG, "Installer is Available or installed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ERROR_DIALOG_REQUEST_CODE) {
            // Adding a fragment via GoogleApiAvailability.showErrorDialogFragment
            // before the instance state is restored throws an error. So instead,
            // set a flag here, which causes the fragment to delay until
            // onPostResume.
            retryProviderInstall = true;
        }
    }

    /**
     * On resume, check whether a flag indicates that the provider needs to be
     * reinstalled.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (retryProviderInstall) {
            // It's safe to retry installation.
            ProviderInstaller.installIfNeededAsync(this, this);
        }
        retryProviderInstall = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.POST_NOTIFICATIONS)) {
                Log.e("Has Permission", "true");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
