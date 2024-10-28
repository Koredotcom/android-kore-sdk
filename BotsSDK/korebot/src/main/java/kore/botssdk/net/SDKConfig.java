package kore.botssdk.net;

import android.view.View;

import androidx.annotation.NonNull;

public class SDKConfig {
    private static boolean isMinimized = false;

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

}
