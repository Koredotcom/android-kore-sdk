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

import java.util.Date;

import de.greenrobot.event.EventBus;
import kore.botssdk.R;
import kore.botssdk.models.BotRequest;
import kore.botssdk.net.BotRequestPool;
import kore.botssdk.net.RestResponse;
import kore.botssdk.utils.BotRequestController;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.websocket.SocketWrapper;

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

        RestResponse.BotMessage botMessage = new RestResponse.BotMessage(message);

        RestResponse.BotPayLoad botPayLoad = new RestResponse.BotPayLoad();
        botPayLoad.setMessage(botMessage);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(botPayLoad);

        Log.d(LOG_TAG, "Payload : " + jsonPayload);
        BotRequestPool.getBotRequestStringArrayList().add(jsonPayload);
        BotRequestController.getInstance().startSendingMessage();

        BotRequest botRequest = gson.fromJson(jsonPayload, BotRequest.class);
        botRequest.setCreatedOn(DateUtils.isoFormatter.format(new Date()));
        EventBus.getDefault().post(botRequest);
    }

}
