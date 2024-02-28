package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.audiocodes.mv.webrtcsdk.session.DTMFOptions;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;


public class GeneralSettingsActivity extends BaseAppCompatActivity {

    private static final String TAG = "GeneralSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.general_settings_activity);

        initGui();
    }

    private void initGui() {

        Spinner dtmfTypeSpinner = (Spinner) findViewById(R.id.general_settings_spinner_dtmf_type);
        Spinner logLevelSpinner = (Spinner) findViewById(R.id.general_settings_spinner_log_level_type);

        CheckBox autoRedirectCheckBox = (CheckBox)findViewById(R.id.general_settings_checkbox_auto_redirect);
        CheckBox redirectCallCheckBox = (CheckBox)findViewById(R.id.general_settings_checkbox_redirect_call);
        EditText redirectCallEditText = (EditText)findViewById(R.id.general_settings_edittext_redirect_call);

        Button saveButton = (Button) findViewById(R.id.general_settings_button_save);


        dtmfTypeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DTMFOptions.DTMFMethod.values()));
        logLevelSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Log.LogLevel.values()));

        try {
            DTMFOptions.DTMFMethod dtmfMethod = Prefs.getDTMFType();
            String currentDTMFType = dtmfMethod.toString();
            Log.d(TAG,"dtmfMethod: "+dtmfMethod+" currentDTMFType: "+currentDTMFType);

            for (int i = 0; i < dtmfTypeSpinner.getAdapter().getCount(); i++) {
                if (dtmfTypeSpinner.getItemAtPosition(i).toString().toLowerCase().equals(currentDTMFType.toLowerCase())) {
                    dtmfTypeSpinner.setSelection(i);
                }
            }

            Log.LogLevel logLevel = Prefs.getLogLevel();
            String currentLogLevel = logLevel.toString();
            Log.d(TAG,"logLevel: "+logLevel+" currentLogLevel: "+currentLogLevel);

            for (int i = 0; i < logLevelSpinner.getAdapter().getCount(); i++) {
                if (logLevelSpinner.getItemAtPosition(i).toString().toLowerCase().equals(currentLogLevel.toLowerCase())) {
                    logLevelSpinner.setSelection(i);
                }
            }
        } catch (Exception e) {
            Log.e(TAG,"error: "+e);
        }

        autoRedirectCheckBox.setChecked(Prefs.isAutoRedirect());
        redirectCallCheckBox.setChecked(Prefs.isRedirectCall());
        redirectCallEditText.setText(Prefs.getRedirectCallUser());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DTMFOptions.DTMFMethod saveDTMFMethod = DTMFOptions.DTMFMethod.values()[dtmfTypeSpinner.getSelectedItemPosition()];
                    Prefs.setDTMFType(saveDTMFMethod);
                    Log.d(TAG,"saveDTMFMethod: "+saveDTMFMethod);

                    Log.LogLevel saveLogLevel = Log.LogLevel.values()[logLevelSpinner.getSelectedItemPosition()];
                    Prefs.setLogLevel(saveLogLevel);
                    Log.setLogLevel(saveLogLevel);
                    Log.d(TAG,"saveLogLevel: "+saveLogLevel);

                    Prefs.setAutoRedirect(autoRedirectCheckBox.isChecked());
                    Prefs.setRedirectCall(redirectCallCheckBox.isChecked());
                    Prefs.setRedirectCallUser(redirectCallEditText.getText().toString());

                    ACManager.getInstance().updateWebRTCConfig();

                } catch (Exception e) {
                    Log.e(TAG,"error: "+e);
                }
                finish();

            }
        });
    }

}
