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
    private static boolean isShowActionBar = true;
    private static boolean isUpdateStatusBarColor = false;

    public static void setCustomTemplateView(@NonNull String templateName, @NonNull View templateView) {
        SDKConfiguration.setCustomTemplateView(templateName, templateView);
    }

    public static void setCustomTemplateViewHolder(@NonNull String templateName, @NonNull Class<?> templateViewHolder) {
        SDKConfiguration.setCustomTemplateViewHolder(templateName, templateViewHolder);
    }

    public static void initialize(String botId, String botName, String clientId, String clientName, String identity, String jwtToken, String serverUrl, String brandingUrl, String jwtServerUrl) {
        SDKConfiguration.Client.setBot_id(botId);
        SDKConfiguration.Client.setBot_name(botName);
        SDKConfiguration.Client.setClient_id(clientId);
        SDKConfiguration.Client.setClient_secret(clientName);
        SDKConfiguration.Client.setIdentity(identity);
        SDKConfiguration.JWTServer.setJwt_token(jwtToken);
        SDKConfiguration.Server.setServerUrl(serverUrl);
        SDKConfiguration.Server.setBrandingUrl(brandingUrl);
        SDKConfiguration.JWTServer.setJwtServerUrl(jwtServerUrl);
    }

    public static void setWidgetBotConfig(String botId, String botName, String clientId, String clientName, String identity, String serverUrl, String jwtTokenUrl) {
        com.kore.ai.widgetsdk.net.SDKConfiguration.Client.setBot_id(botId);
        com.kore.ai.widgetsdk.net.SDKConfiguration.Client.setBot_name(botName);
        com.kore.ai.widgetsdk.net.SDKConfiguration.Client.setClient_id(clientId);
        com.kore.ai.widgetsdk.net.SDKConfiguration.Client.setClient_secret(clientName);
        com.kore.ai.widgetsdk.net.SDKConfiguration.Client.setIdentity(identity);
        com.kore.ai.widgetsdk.net.SDKConfiguration.Server.setServerUrl(serverUrl);
        com.kore.ai.widgetsdk.net.SDKConfiguration.JWTServer.setJwtServerUrl(jwtTokenUrl);
    }

    public static void isWebHook(boolean isWebHook) {
        SDKConfiguration.Client.isWebHook = isWebHook;
    }

    public static void enableWidgetPanel(boolean enable) {
        SDKConfiguration.Client.enablePanel = enable;
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

    public static void setCustomFooterFragment(BaseFooterFragment fragment) {
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

    public static void setIsShowIcon(boolean isShow) {
        SDKConfiguration.BubbleColors.showIcon = isShow;
    }

    public static void setIsShowIconTop(boolean isShow) {
        SDKConfiguration.OverrideKoreConfig.showIconTop = isShow;
    }

    public static void setIsTimeStampsRequired(boolean isRequired) {
        SDKConfiguration.setTimeStampsRequired(isRequired);
    }

    public static void setQueryParams(HashMap<String, Object> queryParams) {
        SDKConfiguration.Server.queryParams = queryParams;
    }

    public static void setCustomData(RestResponse.BotCustomData customData) {
        SDKConfiguration.Server.customData = customData;
    }

    public static boolean isIsShowActionBar() {
        return isShowActionBar;
    }

    public static void setIsShowActionBar(boolean isShow) {
        isShowActionBar = isShow;
    }

    public static void setIsUpdateStatusBarColor(boolean isUpdate) {
        isUpdateStatusBarColor = isUpdate;
    }

    public static boolean isUpdateStatusBarColor() {
        return isUpdateStatusBarColor;
    }
}
