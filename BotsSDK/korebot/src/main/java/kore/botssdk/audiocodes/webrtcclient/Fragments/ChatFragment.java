package kore.botssdk.audiocodes.webrtcclient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.audiocodes.mv.webrtcsdk.im.InstanceMessageStatus;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Callbacks.CallBackHandler;
import kore.botssdk.audiocodes.webrtcclient.General.ACManager;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;

public class ChatFragment extends BaseFragment implements FragmentLifecycle {

    private static final String TAG = "ChatFragment";

    private TextView lastMessageContact;
    private TextView lastMessage;

    private CallBackHandler.ChatCallback chatCallback = new CallBackHandler.ChatCallback() {
        @Override
        public void onNewMessage(String user, String message) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    lastMessageContact.setText(user);
                    lastMessage.setText(message);
                }
            });
        }

        @Override
        public void onMessageStatus(InstanceMessageStatus instanceMessageStatus, long ID){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BotApplication.getGlobalContext(),
                            getString(R.string.chat_message_sent) + ": " + instanceMessageStatus,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment_chat, container, false);
        initGui(rootView);
        CallBackHandler.registerChatCallback(chatCallback);
        return rootView;
    }

    private void initGui(View rootView)
    {
        EditText contact = (EditText) rootView.findViewById(R.id.chat_contact_edittext);
        EditText message = (EditText) rootView.findViewById(R.id.chat_message_edittext);
        lastMessageContact = (TextView) rootView.findViewById(R.id.chat_last_message_contact_textview);
        lastMessage = (TextView) rootView.findViewById(R.id.chat_last_message_textview);
        Button sens = (Button) rootView.findViewById(R.id.chat_send_button);
        sens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = contact.getText().toString();
                String text = message.getText().toString();
                if(!user.equals("") && !text.equals("")){
                    if (!ACManager.getInstance().isRegisterState()) {
                        Toast.makeText(getContext(), R.string.no_registration, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ACManager.getInstance().sendInstantMessage(user, text);
                    }
                }
            }
        });
        if(AppUtils.getStringBoolean(R.string.enable_debug_mode)){
            contact.setText(getString(R.string.sip_default_chat_contact));
            message.setText(getString(R.string.sip_default_chat_message));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CallBackHandler.unregisterChatCallback(chatCallback);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }


}
