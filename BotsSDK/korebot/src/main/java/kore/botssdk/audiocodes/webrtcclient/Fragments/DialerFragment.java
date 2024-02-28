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
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Prefs;
import kore.botssdk.audiocodes.webrtcclient.Structure.CallEntry;


public class DialerFragment extends BaseFragment implements FragmentLifecycle {

    private static final String TAG = "DialerFragment";

    private static final int EXIT_MENU = 101;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment_dialer, container, false);

        initGui(rootView);


        return rootView;
        //return inflater.inflate(R.layout.main_fragment_dialer, container, false);
    }

    private void initGui(View rootView) {
        int[] keypadButtonClickListID = {R.id.dialer_button_keypad_1, R.id.dialer_button_keypad_2, R.id.dialer_button_keypad_3,
                R.id.dialer_button_keypad_4, R.id.dialer_button_keypad_5, R.id.dialer_button_keypad_6,
                R.id.dialer_button_keypad_7, R.id.dialer_button_keypad_8, R.id.dialer_button_keypad_9,
                R.id.dialer_button_keypad_hash, R.id.dialer_button_keypad_0, R.id.dialer_button_keypad_asterisk,
                R.id.dialer_button_keypad_back, R.id.dialer_button_call, R.id.dialer_button_video_call};

        int[] keypadButtonLongClickListID = {R.id.dialer_button_keypad_0, R.id.dialer_button_keypad_back};

        EditText callNumberEditText = (EditText) rootView.findViewById(R.id.dialer_editText_call_number);
//        Button keypadButton1 = (Button)rootView.findViewById(R.id.dialer_button_keypad_1);
//        Button keypadButton2 = (Button)rootView.findViewById(R.id.dialer_button_keypad_2);
//        Button keypadButton3 = (Button)rootView.findViewById(R.id.dialer_button_keypad_3);
//        Button keypadButton4 = (Button)rootView.findViewById(R.id.dialer_button_keypad_4);
//        Button keypadButton5 = (Button)rootView.findViewById(R.id.dialer_button_keypad_5);
//        Button keypadButton6 = (Button)rootView.findViewById(R.id.dialer_button_keypad_6);
//        Button keypadButton7 = (Button)rootView.findViewById(R.id.dialer_button_keypad_7);
//        Button keypadButton8 = (Button)rootView.findViewById(R.id.dialer_button_keypad_8);
//        Button keypadButton9 = (Button)rootView.findViewById(R.id.dialer_button_keypad_9);
//        Button keypadButtonHash = (Button)rootView.findViewById(R.id.dialer_button_keypad_hash);
//        Button keypadButton0 = (Button)rootView.findViewById(R.id.dialer_button_keypad_0);
//        Button keypadButtonAsterisk = (Button)rootView.findViewById(R.id.dialer_button_keypad_asterisk);

//        ImageViewWithText keypadButton1 = (ImageViewWithText)rootView.findViewById(R.id.dialer_button_keypad_1);
//        keypadButton1.getValueTextView().setText("1");
//        ImageViewWithText keypadButton2 = (ImageViewWithText)rootView.findViewById(R.id.dialer_button_keypad_2);
//        keypadButton2.getValueTextView().setText("2\nABC");
        View.OnClickListener dialpadClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View clickedView) {
                //String currentNumber=callNumberEditText.getText().toString();
                String currentNumber = "";
//                int cursorPosition=callNumberEditText.getSelectionStart();
//                if(cursorPosition>=callNumberEditText.length())
//                {
//                    cursorPosition=callNumberEditText.length()-1;
//                }

                if (clickedView == null) {
                    return;
                }
                boolean videoCall=false;
                if(clickedView.getId() == R.id.dialer_button_keypad_1)
                    currentNumber += "1";
                else if(clickedView.getId() == R.id.dialer_button_keypad_2)
                    currentNumber += "2";
                else if(clickedView.getId() == R.id.dialer_button_keypad_3)
                    currentNumber += "3";
                else if(clickedView.getId() == R.id.dialer_button_keypad_4)
                    currentNumber += "4";
                else if(clickedView.getId() == R.id.dialer_button_keypad_5)
                    currentNumber += "5";
                else if(clickedView.getId() == R.id.dialer_button_keypad_6)
                    currentNumber += "6";
                else if(clickedView.getId() == R.id.dialer_button_keypad_7)
                    currentNumber += "7";
                else if(clickedView.getId() == R.id.dialer_button_keypad_8)
                    currentNumber += "8";
                else if(clickedView.getId() == R.id.dialer_button_keypad_9)
                    currentNumber += "9";
                else if(clickedView.getId() == R.id.dialer_button_keypad_hash)
                    currentNumber += "#";
                else if(clickedView.getId() == R.id.dialer_button_keypad_0)
                    currentNumber += "0";
                else if(clickedView.getId() == R.id.dialer_button_keypad_asterisk)
                    currentNumber += "*";
                else if(clickedView.getId() == R.id.dialer_button_keypad_back)
                    callNumberEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                else if(clickedView.getId() == R.id.dialer_button_video_call)
                    videoCall=true;
                else if(clickedView.getId() == R.id.dialer_button_call) {
                    callNumberEditText.setText(callNumberEditText.getText().toString().replace("#",""));
                    if(callNumberEditText.getText().toString().equals(""))
                    {
                        List<CallEntry> callEntryList = BotApplication.getDataBase().getEntries(1);
                        if(callEntryList!=null && callEntryList.size()>0)
                        {
                            currentNumber = callEntryList.get(0).getContactNumber();//callNumberEditText.setText(callEntryList.get(0).getContactNumber());
                        }
                    }
                    else {
                        if (!ACManager.getInstance().isRegisterState() && Prefs.getAutoLogin())
                        {
                            Toast.makeText(getContext(), R.string.no_registration, Toast.LENGTH_SHORT).show();;
                        }
                        else {
                            ACManager.getInstance().callNumber(callNumberEditText.getText().toString(), videoCall);
                        }
                    }
                }

                if (currentNumber != null && !currentNumber.equals("")) {
                    updateCallNumber(callNumberEditText, currentNumber);
                }
            }
        };

        View.OnLongClickListener dialpadLongClickListener = new View.OnLongClickListener() {

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
                    if (tempNumber != null) {
                        tempNumber = "";
                        res = true;
                    }
                }

                if (tempNumber != null && res) {
                    updateCallNumber(callNumberEditText, tempNumber);
                }
                return res;
            }
        };

        for (int keypadButtonClickID : keypadButtonClickListID) {
            View view = rootView.findViewById(keypadButtonClickID);
            if (view != null) {
                view.setOnClickListener(dialpadClickListener);
            }
        }

        for (int keypadButtonLongClickID : keypadButtonLongClickListID) {
            View view = rootView.findViewById(keypadButtonLongClickID);
            if (view != null) {
                view.setOnLongClickListener(dialpadLongClickListener);
            }
        }

        boolean debugMode = AppUtils.getStringBoolean(R.string.enable_debug_mode);
        if(debugMode)
        {
            callNumberEditText.setText(getString(R.string.default_dialer_number));
        }
        callNumberEditText.setSelection(callNumberEditText.getText().length());
    }


    private void updateCallNumber(EditText callNumberEditText, String newString) {
        if (callNumberEditText != null && newString != null) {
            int focusLocation = callNumberEditText.getSelectionStart();
            //callNumberEditText.setText(newString);
            callNumberEditText.setText(callNumberEditText.getText().append(newString));
            callNumberEditText.setSelection(focusLocation + newString.length());
            //int focusLocation = callNumberEditText.length();
//            if(callNumberEditText.getText().length()>=focusLocation)
//            {
//                callNumberEditText.setSelection(focusLocation);
//            }
        }
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}