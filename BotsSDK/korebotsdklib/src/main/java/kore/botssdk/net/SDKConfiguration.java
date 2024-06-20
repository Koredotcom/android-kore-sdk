package kore.botssdk.net;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

/**
 * This class is for defining properties
 */
@SuppressLint("UnknownNullness")
public class SDKConfiguration {

    public static final String APP_REQ_COLOR = "#3942f6";
    /**
     * bot init text  and related settings
     */
    private static boolean TRIGGER_INIT_MESSAGE = false;
    private static String INIT_MESSAGE = "Welpro";
    private static boolean TIME_STAMPS_REQUIRED = true;
    private static final boolean APPLY_FONT_STYLE = true;
    protected static HashMap<String, View> hsh = new HashMap<>();

    public static boolean isTriggerInitMessage() {
        return TRIGGER_INIT_MESSAGE;
    }

    public static void setTriggerInitMessage(boolean triggerInitMessage) {
        TRIGGER_INIT_MESSAGE = triggerInitMessage;
    }

    public static String getInitMessage() {
        return INIT_MESSAGE;
    }

    public static void setInitMessage(String initMessage) {
        INIT_MESSAGE = initMessage;
    }


    public static boolean isApplyFontStyle() {
        return APPLY_FONT_STYLE;
    }

    //JWTServer related configurations
    public static class JWTServer {
        static final String JWT_SERVER_URL = "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/";
    }

    //Server related configurations
    public static class Server {
        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = false;
        public static String SERVER_URL = "https://bots.kore.ai/";
        public static final String Branding_SERVER_URL = "https://bots.kore.ai/";
        public static final String koreAPIUrl = "https://bots.kore.ai/";
    }

    public static class Client {
        public static String client_id = "cs-a8239f06-c548-5ac3-b9c4-3129f2fc5b89";
        public static String client_secret = "+oTGPYWMjQ8g4kixEJwh7p0nD4bd+sVcru/YdXxNN/k=";
        public static String bot_id = "st-27172eec-8b47-5bc5-bd1b-842a99e92d29";
        public static String identity = "example@kore.com";
        public static String bot_name = "Giga";

        public static final String tenant_id = "605da1dbb5f6f00badadb665";
        public static final boolean enablePanel = false;
        public static final boolean enable_ack_delivery = false;
        public static final boolean isWebHook = false;
        public static final String webHook_client_id = "cs-ab324147-4c82-5eb5-b73e-42cf8d8340f8";//"cs-96c4747a-bb79-58b0-9dca-0dcf6c6148cf";//"cs-dc0f84ac-4751-5293-b254-6a0a382ab08c";//"cs-a269ad0a-45ec-5b41-9950-18571e42a6a4";//"cs-5649368e-68bb-555a-8803-935f223db585";
        public static final String webHook_client_secret = "kD9HrB5CPeneebDZFXpRmUxamx55NfVsx0t4nVr78v8=";//"qc4c+FOpEo88m27BgECpcS/oC/CKDWa8u70ll0qr4MM=";//"MiFzNLLWTQZddj1HOmdM4iyePhQ+gED4jdUg88Ujh1Y=";//"kmZ7ck9wRxSVV2dNNwi2P3UZI3qacJgu7JL9AmZapS4=";//"AHSubkG09DRdcz9xlzxUXfrxyRx9V0Yhd+6SnXtjYe4=";
        public static final String webHook_identity = "sudheer.jampana@kore.com";
        public static final String webHook_bot_id = "st-fd0f5024-2318-56fe-8354-555e1786133e";//"st-2e4c9eaf-070c-5b86-8020-add76f37e3a2";//"st-05303785-9992-526c-a83c-be3252fd478e";//"st-caecd28f-64ed-5224-a612-7a3d3a870aed";//"st-cc32974e-c7a2-52d1-83bf-c3dc2b2a9db3";
        public static boolean timeStampBottom = false;
    }

    public static class BubbleColors {
        public static boolean isArabic = false;
        public static String rightBubbleSelected = APP_REQ_COLOR;

        public static void setRightBubbleSelected(String rightBubbleSelected) {
            BubbleColors.rightBubbleSelected = rightBubbleSelected;
        }

        public static void setRightBubbleUnSelected(String rightBubbleUnSelected) {
            BubbleColors.rightBubbleUnSelected = rightBubbleUnSelected;
        }

        public static void setLeftBubbleSelected(String leftBubbleSelected) {
            BubbleColors.leftBubbleSelected = leftBubbleSelected;
        }

        public static void setLeftBubbleUnSelected(String leftBubbleUnSelected) {
            BubbleColors.leftBubbleUnSelected = leftBubbleUnSelected;
        }

        public static void setLeftBubbleTextColor(String leftBubbleTextColor) {
            BubbleColors.leftBubbleTextColor = leftBubbleTextColor;
        }

        public static void setRightBubbleTextColor(String rightBubbleTextColor) {
            BubbleColors.rightBubbleTextColor = rightBubbleTextColor;
        }

        public static void setWhiteColor(String whiteColor) {
            BubbleColors.whiteColor = whiteColor;
        }

        public static void setLeftBubbleBorderColor(String leftBubbleBorderColor) {
            BubbleColors.leftBubbleBorderColor = leftBubbleBorderColor;
        }

        public static void setRightLinkColor(String rightLinkColor) {
            BubbleColors.rightLinkColor = rightLinkColor;
        }

        public static void setLeftLinkColor(String leftLinkColor) {
            BubbleColors.leftLinkColor = leftLinkColor;
        }


        public static String rightBubbleUnSelected = APP_REQ_COLOR;
        public static String leftBubbleSelected = "#D3D3D3";
        public static String leftBubbleUnSelected = "#f8f9f8";
        public static String leftBubbleTextColor = "#404051";
        public static String rightBubbleTextColor = "#161628";//"#757587";
        public static String whiteColor = "#FFFFFF";
        public static String leftBubbleBorderColor = "#eeeef2";
        public static String rightLinkColor = APP_REQ_COLOR;
        public static String leftLinkColor = APP_REQ_COLOR;
        public static final boolean BubbleUI = false;
        public static final boolean showIcon = true;

        public static int getIcon() {
            return icon;
        }

        public static void setIcon(int icon) {
            BubbleColors.icon = icon;
        }

        public static String getIcon_url() {
            return icon_url;
        }

        public static void setIcon_url(String icon_url) {
            BubbleColors.icon_url = icon_url;
        }

        private static int icon = -1;
        private static String icon_url = "";

        public static String getProfileColor() {
            return profileColor;
        }

        public static void setProfileColor(String profileColor) {
            BubbleColors.profileColor = profileColor;
        }

        static String profileColor = APP_REQ_COLOR;

        public static void setQuickReplyColor(String quickReplyColor) {
            BubbleColors.quickReplyColor = quickReplyColor;
        }

        public static String quickReplyColor = "#EEEEF0";
        public static String quickReplyTextColor = "#000000";

    }

    public static boolean isTimeStampsRequired() {
        return TIME_STAMPS_REQUIRED;
    }

    public static void setTimeStampsRequired(boolean timeStampsRequired) {
        TIME_STAMPS_REQUIRED = timeStampsRequired;
    }

    /**
     * don't use relative it is licenced version
     */
    public enum FONT_TYPES {
        ROBOTO, RELATIVE
    }

    private static final FONT_TYPES fontType = FONT_TYPES.ROBOTO;

    public static void setCustomTemplateView(String templateName, View templateView) {
        hsh.put(templateName, templateView);
        Log.e("HashMap Count", hsh.size() + "");
    }

    public static HashMap<String, View> getCustomTemplateView() {
        return hsh;
    }

}
