package kore.botssdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kore.botssdk.R;
import kore.botssdk.adapter.BotsChatAdapter;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotContentFragment extends BaseSpiceFragment {

    ListView botsBubblesListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_content_layout, null);
        findViews(view);
        setupAdapter();
        return view;
    }

    private void findViews(View view) {
        botsBubblesListView = (ListView) view.findViewById(R.id.botsBubblesListView);
    }

    private void setupAdapter() {
        BotsChatAdapter botsChatAdapter = new BotsChatAdapter(getActivity());
        botsBubblesListView.setAdapter(botsChatAdapter);
    }
}
