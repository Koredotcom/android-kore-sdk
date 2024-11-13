package kore.botssdk.audiocodes.webrtcclient.Fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import kore.botssdk.R;
import kore.botssdk.activity.BotAppCompactActivity;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.audiocodes.webrtcclient.Structure.CallEntry;

public class DialerFragment extends BaseFragment implements FragmentLifecycle {
    private static final String TAG = "DialerFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_dialer, container, false);
        initGui(rootView);
        return rootView;
    }

    private void initGui(View rootView) {
        int[] keypadButtonClickListID = {R.id.dialer_button_keypad_1, R.id.dialer_button_keypad_2, R.id.dialer_button_keypad_3,
                R.id.dialer_button_keypad_4, R.id.dialer_button_keypad_5, R.id.dialer_button_keypad_6,
                R.id.dialer_button_keypad_7, R.id.dialer_button_keypad_8, R.id.dialer_button_keypad_9,
                R.id.dialer_button_keypad_hash, R.id.dialer_button_keypad_0, R.id.dialer_button_keypad_asterisk,
                R.id.dialer_button_keypad_back, R.id.dialer_button_call, R.id.dialer_button_video_call};

        int[] keypadButtonLongClickListID = {R.id.dialer_button_keypad_0, R.id.dialer_button_keypad_back};

        EditText callNumberEditText = rootView.findViewById(R.id.dialer_editText_call_number);
        View.OnClickListener dialPadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                String currentNumber = "";

                if (clickedView == null) {
                    return;
                }
                boolean videoCall = false;
                if (clickedView.getId() == R.id.dialer_button_keypad_1)
                    currentNumber += "1";
                else if (clickedView.getId() == R.id.dialer_button_keypad_2)
                    currentNumber += "2";
                else if (clickedView.getId() == R.id.dialer_button_keypad_3)
                    currentNumber += "3";
                else if (clickedView.getId() == R.id.dialer_button_keypad_4)
                    currentNumber += "4";
                else if (clickedView.getId() == R.id.dialer_button_keypad_5)
                    currentNumber += "5";
                else if (clickedView.getId() == R.id.dialer_button_keypad_6)
                    currentNumber += "6";
                else if (clickedView.getId() == R.id.dialer_button_keypad_7)
                    currentNumber += "7";
                else if (clickedView.getId() == R.id.dialer_button_keypad_8)
                    currentNumber += "8";
                else if (clickedView.getId() == R.id.dialer_button_keypad_9)
                    currentNumber += "9";
                else if (clickedView.getId() == R.id.dialer_button_keypad_hash)
                    currentNumber += "#";
                else if (clickedView.getId() == R.id.dialer_button_keypad_0)
                    currentNumber += "0";
                else if (clickedView.getId() == R.id.dialer_button_keypad_asterisk)
                    currentNumber += "*";
                else if (clickedView.getId() == R.id.dialer_button_keypad_back)
                    callNumberEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                else if (clickedView.getId() == R.id.dialer_button_video_call)
                    videoCall = true;
                else if (clickedView.getId() == R.id.dialer_button_call) {
                    callNumberEditText.setText(callNumberEditText.getText().toString().replace("#", ""));
                    if (callNumberEditText.getText().toString().isEmpty()) {
                        if (!(requireActivity() instanceof BotAppCompactActivity)) return;
                        List<CallEntry> callEntryList = ((BotAppCompactActivity) requireActivity()).getDataBase().getEntries(1);
                        if (callEntryList != null && !callEntryList.isEmpty()) {
                            currentNumber = callEntryList.get(0).getContactNumber();//callNumberEditText.setText(callEntryList.get(0).getContactNumber());
                        }
                    } else {
                        if (!ACManager.getInstance().isRegisterState() && Prefs.getAutoLogin(requireContext())) {
                            Toast.makeText(getContext(), R.string.no_registration, Toast.LENGTH_SHORT).show();
                        } else {
                            ACManager.getInstance().callNumber(callNumberEditText.getText().toString(), videoCall);
                        }
                    }
                }

                if (currentNumber != null && !currentNumber.equals("")) {
                    updateCallNumber(callNumberEditText, currentNumber);
                }
            }
        };

        View.OnLongClickListener dialPadLongClickListener = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View clickedView) {

                String tempNumber = callNumberEditText.getText().toString();
                boolean res = false;
                if (clickedView == null) {
                    return res;
                }
                int id = clickedView.getId();
                if (id == R.id.dialer_button_keypad_0) {
                    tempNumber += "+";
                    res = true;
                } else if (id == R.id.dialer_button_keypad_back) {
                    tempNumber = "";
                    res = true;
                }

                if (res) {
                    updateCallNumber(callNumberEditText, tempNumber);
                }
                return res;
            }
        };

        for (int keypadButtonClickID : keypadButtonClickListID) {
            View view = rootView.findViewById(keypadButtonClickID);
            if (view != null) {
                view.setOnClickListener(dialPadClickListener);
            }
        }

        for (int keypadButtonLongClickID : keypadButtonLongClickListID) {
            View view = rootView.findViewById(keypadButtonLongClickID);
            if (view != null) {
                view.setOnLongClickListener(dialPadLongClickListener);
            }
        }

        boolean debugMode = AppUtils.getStringBoolean(requireContext(), R.string.enable_debug_mode);
        if (debugMode) {
            callNumberEditText.setText(getString(R.string.default_dialer_number));
        }
        callNumberEditText.setSelection(callNumberEditText.getText().length());
    }


    private void updateCallNumber(EditText callNumberEditText, String newString) {
        if (callNumberEditText != null && newString != null) {
            int focusLocation = callNumberEditText.getSelectionStart();
            callNumberEditText.setText(callNumberEditText.getText().append(newString));
            callNumberEditText.setSelection(focusLocation + newString.length());
        }
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
    }
}