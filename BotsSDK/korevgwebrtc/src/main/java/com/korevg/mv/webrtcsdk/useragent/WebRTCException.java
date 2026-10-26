/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.useragent;

public class WebRTCException
extends Exception {
    public WebRTCExceptionCode code = WebRTCExceptionCode.UNKNOWN;

    public WebRTCException() {
        this.code = WebRTCExceptionCode.UNKNOWN;
    }

    public WebRTCException(WebRTCExceptionCode code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        switch (this.code) {
            case ACCOUNT_NOT_SET: {
                return "Account not set";
            }
            case USER_CREDENTIALS_NOT_SET: {
                return "User credentials not set";
            }
            case SERVER_CONFIGURATION_NOT_SET: {
                return "Server configuraion not set";
            }
            case UNKNOWN: {
                return "An unknown error has occured";
            }
            case MAX_NR_OF_SESSIONS_REACHED: {
                return "Maximum number of sessions reached";
            }
            case VIDEO_NOT_ENABLED: {
                return "Video is not available for this call";
            }
            case CONFERENCE_2ND_SESSION_NOT_ON_HOLD: {
                return "CONFERENCE_2ND_SESSION_NOT_ON_HOLD";
            }
            case CONFERENCE_1ST_SESSION_NOT_ACTIVE: {
                return "CONFERENCE_1ST_SESSION_NOT_ACTIVE";
            }
        }
        return "No error";
    }

    public static enum WebRTCExceptionCode {
        ACCOUNT_NOT_SET,
        USER_CREDENTIALS_NOT_SET,
        SERVER_CONFIGURATION_NOT_SET,
        LOGGED_OUT,
        UNKNOWN,
        MAX_NR_OF_SESSIONS_REACHED,
        VIDEO_NOT_ENABLED,
        CONFERENCE_2ND_SESSION_NOT_ON_HOLD,
        CONFERENCE_1ST_SESSION_NOT_ACTIVE;

    }
}

