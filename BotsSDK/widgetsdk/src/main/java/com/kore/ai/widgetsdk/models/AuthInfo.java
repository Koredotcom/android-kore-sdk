package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class AuthInfo {

    @SerializedName("resourceOwnerID")
    protected String userId;
    @SerializedName("token_type")
    protected String tokenType;
    protected String accessToken;
    protected String refreshToken;
    protected String expiresDate;
    protected String refreshExpiresDate;
    protected String issuedDate;
    protected String tenantID;
    protected String clientID;

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpiresDate() {
        return expiresDate;
    }

    public String getRefreshExpiresDate(int iis) {
        return refreshExpiresDate;
    }

    public String getIssuedDate(int ii) {
        return issuedDate;
    }

    public String getTenantID() {
        return tenantID;
    }

    @Deprecated
    public String getClientID() {
        return clientID;
    }

    public String getUserId() {
        return userId;
    }
}
