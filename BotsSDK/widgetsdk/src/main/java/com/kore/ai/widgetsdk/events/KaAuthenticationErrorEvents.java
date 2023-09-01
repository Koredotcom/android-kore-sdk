package com.kore.ai.widgetsdk.events;


public class KaAuthenticationErrorEvents {

    public static class OnEntAdminSettingsChanged {
        public final String errMsg;
        public final int errCode;

        public OnEntAdminSettingsChanged(String errMsg, int errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }

    public static class OnEntAdminLimitations {
        public final String errMsg;
        public final String errCode;

        public OnEntAdminLimitations(String errMsg, String errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }

    public static class LogoutEvent {
        public LogoutEvent(){}
    }

    public static class PassWordExpiredNotificationEvent {

        final String expDate;
        public PassWordExpiredNotificationEvent(String expDate){
            this.expDate = expDate;
        }

        public String getExpDate() {
            return expDate;
        }
    }

    public static class PasswordChangeEvent {
        public PasswordChangeEvent(){}
    }

    public static class NetWorkIPError{
        public final String errMsg;
        public final int errCode;

        public NetWorkIPError(String errMsg, int errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }
    public static class SignUpAdminError{
        public final String errMsg;
        public final int errCode;

        public SignUpAdminError(String errMsg, int errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }
}

