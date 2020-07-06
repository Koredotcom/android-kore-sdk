package com.kore.ai.widgetsdk.events;


public class KaAuthErrorCodes {
    public static final int REFRESH_TOKEN_CODE = 41;
    public static final int ERR_LOGIN_SUSPENDED_USER = 45;
    public static final int PASSWORD_POLICY_CHANGED_CODE = 46;
    public static final int PASSWORD_EXPIRED_CODE = 47;
    public static final int ADMIN_PASSWORD_RESET_CODE = 48;
    public static final int MANAGED_BY_ENTERPRISE_CODE = 49;
    public static final int ADMIN_SSO_ENABLED_CODE = 50;
    public static final int ADMIN_SSO_DISABLED_CODE = 51;
    public static final int ADMIN_ACCOUNT_SUSPENDED_CODE = 52;
    public static final int ADMIN_KILLED_SESSION_CODE = 53;
    public static final int USAGE_POLICY_CHANGED = 54;
    public static final int ACCOUNT_DELETED = 55;
    //public static final int USER_LOG_OFF = 57;
    public static final int PERMISSIONS_CHANGED = 61;
    public static final int MDM_AUTHENTICATION_REQUIRED = 250;
    public static final int MDM_UPDATED = 77;

    public static final int COMPULSORY_LOGOUT = 1221;//arbitrary

    public static final String INSUFFICIENT_SEATS_IN_SPACE = "InsufficientSeats";
    public static final String SPACE_LIMIT_REACHED = "FreeTeamsLimitReached";

    public static final int OUT_OF_NW_ZONE = 60;
    public static final int CANT_ACCESS_ACCT_IN_NW = 61;
    public static final int SIGN_UP_ADMIN = 238;
}
