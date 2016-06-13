package kore.botssdk.fragment;

import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;

import kore.botssdk.net.BotRestService;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BaseSpiceFragment extends Fragment{

    SpiceManager spiceManager = new SpiceManager(BotRestService.class);

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
