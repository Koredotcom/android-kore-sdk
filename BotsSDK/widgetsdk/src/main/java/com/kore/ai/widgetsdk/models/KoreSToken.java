package com.kore.ai.widgetsdk.models;

/**
 * Created by Ramachandra Pradeep on 11-Dec-18.
 */

public class KoreSToken {
    private String sToken;

    public int getRESP_CODE() {
        return RESP_CODE;
    }

    public void setRESP_CODE(int RESP_CODE) {
        this.RESP_CODE = RESP_CODE;
    }

    private int RESP_CODE;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    /**
     * @return
     * The sToken
     */
    public String getSToken() {
        return sToken;
    }

    /**
     * @param sToken
     * The sToken
     */
    public void setSToken(String sToken) {
        this.sToken = sToken;
    }
}
