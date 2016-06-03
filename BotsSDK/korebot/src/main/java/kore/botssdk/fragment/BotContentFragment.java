package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kore.botssdk.R;
import kore.botssdk.adapter.BotsChatAdapter;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.websocket.KorePresenceWrapper;
import kore.botssdk.websocket.PresenceConnectionListener;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotContentFragment extends BaseSpiceFragment implements PresenceConnectionListener {

    ListView botsBubblesListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        setupAdapter();
        KorePresenceWrapper.getInstance().setPresenceConnectionListener(this);
        return view;
    }

    private void findViews(View view) {
        botsBubblesListView = (ListView) view.findViewById(R.id.botsBubblesListView);
    }

    private void setupAdapter() {
        BotsChatAdapter botsChatAdapter = new BotsChatAdapter(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
    }

    @Override
    public void onConnected(String message) {
        CustomToast.showToast(getActivity(), "onConnected");
    }

    @Override
    public void onDisconnected(String reason) {
        CustomToast.showToast(getActivity(), "onDisconnected");
    }
}
