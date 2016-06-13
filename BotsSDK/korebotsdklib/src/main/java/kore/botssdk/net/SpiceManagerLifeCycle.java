package kore.botssdk.net;

import android.content.Context;

/**
 * Created by Pradeep Mahato on 13-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface SpiceManagerLifeCycle {
    /**
     * Provides spiceManager's present state of connection
     *
     * @return whether spiceManager is presently connected
     */
    boolean isConnected();

    /**
     * To start Spice Manager
     *
     * @param context
     */
    void start(Context context);

    /**
     * To stop Spice Manager
     */
    void stop();
}
