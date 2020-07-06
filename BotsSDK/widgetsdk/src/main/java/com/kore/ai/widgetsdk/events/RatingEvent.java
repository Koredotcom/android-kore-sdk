package com.kore.ai.widgetsdk.events;

import java.util.HashMap;

public class RatingEvent {

    HashMap<String,Object> hashMap;

    public RatingEvent(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }
}
