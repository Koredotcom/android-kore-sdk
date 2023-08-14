package kore.botssdk.net;

import java.util.ArrayList;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

/**
 * Data structure to maintain pool of requested messages.
 */
public class BotRequestPool {
    private static final ArrayList<String> botRequestStringArrayList = new ArrayList<>();

    /**
     * Get the pool of requests
     * @return
     */
    public static ArrayList<String> getBotRequestStringArrayList() {
        return botRequestStringArrayList;
    }

    /**
     * Determine whether request pool is empty
     * @return
     */
    public static boolean isPoolEmpty() {
        return botRequestStringArrayList.isEmpty();
    }
}
