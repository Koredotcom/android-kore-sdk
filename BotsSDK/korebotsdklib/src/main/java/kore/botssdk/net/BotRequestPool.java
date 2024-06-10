package kore.botssdk.net;

import java.util.ArrayList;

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
