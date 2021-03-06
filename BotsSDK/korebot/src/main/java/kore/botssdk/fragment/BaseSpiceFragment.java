package kore.botssdk.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;

import kore.botssdk.net.BotLocalService;
import kore.botssdk.net.BotRestService;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BaseSpiceFragment extends Fragment{

    SpiceManager spiceManager = new SpiceManager(BotRestService.class);

    public SpiceManager getSpiceDBManager() {
        return spiceDBManager;
    }

    SpiceManager spiceDBManager = new SpiceManager(BotLocalService.class);
    @Override
    public void onAttach(Context context) {
        if(!spiceManager.isStarted())
            spiceManager.start(getActivity().getApplicationContext());
        if(!spiceDBManager.isStarted())
            spiceDBManager.start(getActivity().getApplicationContext());
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        if(spiceManager.isStarted())
            spiceManager.shouldStop();
        if(spiceDBManager.isStarted())
            spiceDBManager.shouldStop();
        super.onDetach();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
