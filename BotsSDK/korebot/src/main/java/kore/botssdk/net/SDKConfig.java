package kore.botssdk.net;

import android.view.View;

import androidx.annotation.NonNull;

import kore.botssdk.fragment.content.BaseContentFragment;
import kore.botssdk.fragment.footer.BaseFooterFragment;

public class SDKConfig {
    private static BaseContentFragment customContentFragment = null;
    private static BaseFooterFragment customFooterFragment = null;
    private static boolean isShowHeader = true;

    public static void setCustomTemplateView(@NonNull String templateName, @NonNull View templateView) {
        SDKConfiguration.setCustomTemplateView(templateName, templateView);
    }

    public static void setCustomTemplateViewHolder(@NonNull String templateName, @NonNull Class<?> templateViewHolder) {
        SDKConfiguration.setCustomTemplateViewHolder(templateName, templateViewHolder);
    }

    public static void initialize(String botId, String botName, String clientId, String clientName, String identity, String jwtToken) {
        SDKConfiguration.Client.setBot_id(botId);
        SDKConfiguration.Client.setBot_name(botName);
        SDKConfiguration.Client.setClient_id(clientId);
        SDKConfiguration.Client.setClient_secret(clientName);
        SDKConfiguration.Client.setIdentity(identity);
        SDKConfiguration.JWTServer.setJwt_token(jwtToken);
    }

    public static void isWebHook(boolean isWebHook)
    {
        SDKConfiguration.Client.isWebHook = isWebHook;
    }
    public static void setServerUrl(String url) {
        SDKConfiguration.Server.setServerUrl(url);
    }

    public static void setBrandingUrl(String url) {
        SDKConfiguration.Server.setBrandingUrl(url);
    }

    public static void setJwtTokenUrl(String url) {
        SDKConfiguration.JWTServer.setJwtServerUrl(url);
    }

    public static void  addCustomContentFragment(BaseContentFragment contentFragment) {
        customContentFragment = contentFragment;
    }

    public static BaseContentFragment getCustomContentFragment()  {return customContentFragment;}

    public static void addCustomFooterFragment(BaseFooterFragment fragment) {
        customFooterFragment = fragment;
    }

    public static BaseFooterFragment getCustomFooterFragment() {return customFooterFragment;}

    public static boolean isShowHeader() {
        return isShowHeader;
    }

    public static void setIsShowHeader(boolean isShowHeader) {
        SDKConfig.isShowHeader = isShowHeader;
    }
}
