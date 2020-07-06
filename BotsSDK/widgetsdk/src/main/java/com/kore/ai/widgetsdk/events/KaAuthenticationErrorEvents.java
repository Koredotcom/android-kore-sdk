package com.kore.ai.widgetsdk.events;


public class KaAuthenticationErrorEvents {

    public static class OnEntAdminSettingsChanged {
        public String errMsg;
        public int errCode;

        public OnEntAdminSettingsChanged(String errMsg, int errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }

    public static class OnEntAdminLimitations {
        public String errMsg;
        public String errCode;

        public OnEntAdminLimitations(String errMsg, String errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }

    public static class LogoutEvent {
        public LogoutEvent(){}
    }

    public static class PassWordExpiredNotificationEvent {

        String expDate;
        public PassWordExpiredNotificationEvent(String expDate){
            this.expDate = expDate;
        }

        public String getExpDate() {
            return expDate;
        }
    }

    public static class PasswordChangeEvent {
        public PasswordChangeEvent(){};
    }

    public static class NetWorkIPError{
        public String errMsg;
        public int errCode;

        public NetWorkIPError(String errMsg, int errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }
    public static class SignUpAdminError{
        public String errMsg;
        public int errCode;

        public SignUpAdminError(String errMsg, int errCode){
            this.errMsg = errMsg;
            this.errCode = errCode;
        }
    }
}

