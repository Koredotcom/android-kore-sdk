package kore.botssdk.net;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;

import kore.botssdk.fragment.content.BaseContentFragment;
import kore.botssdk.fragment.footer.BaseFooterFragment;
import kore.botssdk.fragment.header.BaseHeaderFragment;

public class SDKConfig {
    private static boolean isMinimized = false;
    private static final HashMap<String, BaseHeaderFragment> customHeaders = new HashMap<>();
    private static BaseFooterFragment customFooterFragment = null;
    private static BaseContentFragment customContentFragment = null;

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

    public static void isWebHook(boolean isWebHook) {
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

    public static void setIsMinimized(boolean isMinimize) {
        isMinimized = isMinimize;
    }

    public static boolean isMinimized() {
        return isMinimized;
    }

    public static BaseHeaderFragment getCustomHeaderFragment(String size) {
        return customHeaders.get(size);
    }

    public static void setCustomHeaderFragment(String size, BaseHeaderFragment fragment) {
        customHeaders.put(size, fragment);
    }

    public static void setCustomFooterFragment(BaseFooterFragment fragment){
        customFooterFragment = fragment;
    }

    public static BaseFooterFragment getCustomFooterFragment() {
        return customFooterFragment;
    }

    public static BaseContentFragment getCustomContentFragment() {
        return customContentFragment;
    }

    public static void setCustomContentFragment(BaseContentFragment customContentFragment) {
        SDKConfig.customContentFragment = customContentFragment;
    }
}
