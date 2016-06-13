package kore.botssdk.net;

import java.util.ArrayList;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotRequestPool {

    private static ArrayList<String> botRequestStringArrayList = new ArrayList<>();

    public static ArrayList<String> getBotRequestStringArrayList() {
        return botRequestStringArrayList;
    }

    public static boolean isPoolEmpty() {
        return botRequestStringArrayList.isEmpty();
    }
}
