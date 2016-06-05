package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.greenrobot.event.EventBus;
import kore.botssdk.R;
import kore.botssdk.models.BotRequest;
import kore.botssdk.net.BotRequestPool;
import kore.botssdk.net.KoreRestResponse;
import kore.botssdk.websocket.KorePresenceWrapper;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class ComposeFooterFragment extends BaseSpiceFragment {

    String LOG_TAG = ComposeFooterFragment.class.getName();

    EditText composeFooterEditTxt;
    Button composeFooterSendBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.compose_footer_layout, null);

        findViews(view);

        composeFooterSendBtn.setOnClickListener(composeFooterSendBtOnClickListener);
        return view;
    }

    private void findViews(View view) {
        composeFooterEditTxt = (EditText) view.findViewById(R.id.composeFooterEditTxt);
        composeFooterSendBtn = (Button) view.findViewById(R.id.composeFooterSendBtn);
    }

    View.OnClickListener composeFooterSendBtOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = composeFooterEditTxt.getText().toString().trim();
            if (!msg.isEmpty()) {
                composeFooterEditTxt.setText("");
                prepareTheJsonObject(msg);
            }
        }
    };

    private void prepareTheJsonObject(String message) {

        KoreRestResponse.BotMessage botMessage = new KoreRestResponse.BotMessage(message);

        KoreRestResponse.BotPayLoad botPayLoad = new KoreRestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        Log.d(LOG_TAG, "Payload : " + jsonPayload);
        BotRequestPool.getBotRequestStringArrayList().add(jsonPayload);
        KorePresenceWrapper.getInstance().sendMessage();

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        EventBus.getDefault().post(botRequest);
    }

}
