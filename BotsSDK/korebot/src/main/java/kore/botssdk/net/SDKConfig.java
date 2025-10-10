package kore.botssdk.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;

import kore.botssdk.fragment.content.BaseContentFragment;
import kore.botssdk.fragment.footer.BaseFooterFragment;
import kore.botssdk.fragment.header.BaseHeaderFragment;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingModel;

public class SDKConfig {
    private static BaseContentFragment customContentFragment = null;
    private static BaseFooterFragment customFooterFragment = null;
    private static BaseHeaderFragment customHeaderFragment = null;
    private static boolean isShowHeader = true;
    private static boolean isUpdateStatusBarColor = false;
    private static boolean isShowActionBar = true;

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

    public static void isWebHook(boolean isWebHook) {
        SDKConfiguration.Client.isWebHook = isWebHook;
    }

    public static void addCustomContentFragment(BaseContentFragment contentFragment) {
        customContentFragment = contentFragment;
    }

    public static BaseContentFragment getCustomContentFragment() {
        return customContentFragment;
    }

    public static void addCustomFooterFragment(BaseFooterFragment fragment) {
        customFooterFragment = fragment;
    }

    public static BaseFooterFragment getCustomFooterFragment() {
        return customFooterFragment;
    }

    public static BaseHeaderFragment getCustomHeaderFragment() {
        return customHeaderFragment;
    }

    public static void addCustomHeaderFragment(BaseHeaderFragment customHeaderFragment) {
        SDKConfig.customHeaderFragment = customHeaderFragment;
    }

    public static boolean isShowHeader() {
        return isShowHeader;
    }

    public static void setIsShowHeader(boolean isShowHeader) {
        SDKConfig.isShowHeader = isShowHeader;
    }

    public static void setIsShowIcon(boolean isShow) {
        SDKConfiguration.BubbleColors.showIcon = isShow;
    }

    public static void setLocalBranding(boolean enable, BrandingModel branding) {
        if (enable) {
            SDKConfiguration.BubbleColors.enableLocalBranding = true;
            SDKConfiguration.BubbleColors.localBranding = branding;
        }
    }

    public static boolean isIsShowActionBar() {
        return isShowActionBar;
    }

    public static void setIsShowActionBar(boolean isShow) {
        isShowActionBar = isShow;
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

    public static void disconnectBotSession(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_RECONNECT", false);
        editor.putInt("HISTORY_COUNT", 0);
        editor.apply();
    }

    public static void setIsUpdateStatusBarColor(boolean isUpdate) {
        isUpdateStatusBarColor = isUpdate;
    }

    public static boolean isUpdateStatusBarColor() {
        return isUpdateStatusBarColor;
    }
}
