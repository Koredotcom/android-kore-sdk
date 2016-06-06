package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.greenrobot.event.EventBus;
import kore.botssdk.R;
import kore.botssdk.adapter.BotsChatAdapter;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.BotRequestPool;
import kore.botssdk.utils.BotRequestController;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.websocket.SocketWrapper;
import kore.botssdk.websocket.SocketConnectionListener;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotContentFragment extends BaseSpiceFragment implements SocketConnectionListener {

    ListView botsBubblesListView;
    BotsChatAdapter botsChatAdapter;
    String LOG_TAG = BotContentFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        setupAdapter();
        SocketWrapper.getInstance().setSocketConnectionListener(this);
        EventBus.getDefault().register(this);
        return view;
    }

    private void findViews(View view) {
        botsBubblesListView = (ListView) view.findViewById(R.id.botsBubblesListView);
    }

    private void setupAdapter() {
        botsChatAdapter = new BotsChatAdapter(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
    }

    @Override
    public void onConnected(String message) {
        Log.d(LOG_TAG, message);
        convertToPojoAndRefreshTheList(message);
        if (!BotRequestPool.isPoolEmpty()) {
            //If the pool is not empty then start sending messages....
            BotRequestController.getInstance().startSendingMessage();
        }
    }

    @Override
    public void onDisconnected(String reason) {
        CustomToast.showToast(getActivity(), "onDisconnected. Reason is " + reason);
    }

    private void convertToPojoAndRefreshTheList(String message) {

        Gson gson = new Gson();
        try {
            BotResponse botResponse = gson.fromJson(message, BotResponse.class);
            if (botResponse.getMessage() != null) {
                botsChatAdapter.addBaseBotMessage(botResponse);
                scrollToBottom();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }

    public void onEvent(BotRequest botRequest) {
        if (botRequest.getMessage() != null) {
            botsChatAdapter.addBaseBotMessage(botRequest);
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        final int count = botsChatAdapter.getCount();
        botsBubblesListView.post(new Runnable() {
            @Override
            public void run() {
                botsBubblesListView.setSelection(count - 1);
            }
        });
    }

}
