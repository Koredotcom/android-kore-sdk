package com.kore.ai.widgetsdk.room.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Ramachandra Pradeep on 20-Jul-18.
 */

@Entity(tableName = "auth_data")
public class AuthData {

    public static final String TABLE_NAME = "auth_data";
    public static final String USER_ID = "user_id";
    public static final String TOKEN_TYPE = "token_type";
    public static final String ACCESS_TKN= "access_token";
    public static final String REFRESH_TKN = "refresh_token";
    public static final String EXPIRY_DATE = "expires_date";
    public static final String REFRESH_EXPIRES_DATE = "refresh_expires_date";
    public static final String ISSUED_DATE = "issued_date";
    public static final String TENANT_ID = "tenant_id";
    public static final String CLIENT_ID = "client_id";


    @PrimaryKey
    @ColumnInfo(name = USER_ID)
    @NonNull public String resourceOwnerID;

    @ColumnInfo(name = TOKEN_TYPE)
    public String token_type;

    @ColumnInfo(name = ACCESS_TKN)
    public String accessToken;

    @ColumnInfo(name = REFRESH_TKN)
    public String refreshToken;

    @ColumnInfo(name = EXPIRY_DATE)
    public String expiresDate;

    @ColumnInfo(name = REFRESH_EXPIRES_DATE)
    public String refreshExpiresDate;

    @ColumnInfo(name = ISSUED_DATE)
    public String issuedDate;

    @ColumnInfo(name = TENANT_ID)
    public String tenantID;

    @ColumnInfo(name = CLIENT_ID)
    public String clientID;

    public String getTokenType() {
        return token_type;
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
        return resourceOwnerID;
    }
}
