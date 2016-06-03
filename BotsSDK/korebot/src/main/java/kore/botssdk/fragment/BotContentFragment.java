package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import kore.botssdk.R;
import kore.botssdk.adapter.BotsChatAdapter;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.websocket.KorePresenceWrapper;
import kore.botssdk.websocket.PresenceConnectionListener;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotContentFragment extends BaseSpiceFragment implements PresenceConnectionListener {

    ListView botsBubblesListView;
    BotsChatAdapter botsChatAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        setupAdapter();
        KorePresenceWrapper.getInstance().setPresenceConnectionListener(this);
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
        CustomToast.showToast(getActivity(), message);
        convertToPojoAndRefreshTheList(message);
    }

    @Override
    public void onDisconnected(String reason) {
        CustomToast.showToast(getActivity(), "onDisconnected. Reason is " + reason);
    }

    private void convertToPojoAndRefreshTheList(String message) {

        Gson gson = new Gson();
        BotResponse botResponse = gson.fromJson(message, BotResponse.class);
        if (botResponse.getMessage() != null) {
            botsChatAdapter.addBaseBotMessage(botResponse);
        }

    }

    public void onEvent(BotRequest botRequest) {
        if (botRequest.getMessage() != null) {
            botsChatAdapter.addBaseBotMessage(botRequest);
        }
    }

}
