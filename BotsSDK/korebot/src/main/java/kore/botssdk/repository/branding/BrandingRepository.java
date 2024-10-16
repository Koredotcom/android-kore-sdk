package kore.botssdk.repository.branding;

import android.content.Context;

import com.google.gson.Gson;

import io.reactivex.annotations.NonNull;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.models.BotActiveThemeModel;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.utils.LogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("UnKnownNullness")
public class BrandingRepository {
    BotChatViewListener botChatView;
    Context context;

    public BrandingRepository(Context context, BotChatViewListener chatView) {
        this.botChatView = chatView;
        this.context = context;
    }

    Gson gson = new Gson();
    private String resp;

    public void getBrandingDetails(String botId, String botToken) {
        Call<BotActiveThemeModel> getBankingConfigService = BrandingRestBuilder.getRestAPI().getBrandingNewDetails(botId, "bearer " + botToken, "published", "1", "en_US", botId);
        getBankingConfigService.enqueue(new Callback<BotActiveThemeModel>() {
            @Override
            public void onResponse(@NonNull Call<BotActiveThemeModel> call, @NonNull Response<BotActiveThemeModel> response) {
                if (response.isSuccessful()) {
                    BotActiveThemeModel botActiveThemeModel = response.body();

                    if (botActiveThemeModel != null && botActiveThemeModel.getV3() != null) {
                        BotBrandingModel botOptionsModel = botActiveThemeModel.getV3();
                        botChatView.onBrandingDetails(botOptionsModel);
//                        setButtonBranding(botOptionsModel);
//
//                        if (botOptionsModel != null) {
//                            if (botOptionsModel.getChat_bubble() != null && !StringUtils.isNullOrEmpty(botOptionsModel.getChat_bubble().getStyle())) {
//                                sharedPreferences.edit().putString(BundleConstants.BUBBLE_STYLE, botOptionsModel.getChat_bubble().getStyle()).apply();
//                            }
//
//                            if (botOptionsModel.getBody() != null && !StringUtils.isNullOrEmpty(botOptionsModel.getBody().getBubble_style())) {
//                                sharedPreferences.edit().putString(BundleConstants.BUBBLE_STYLE, botOptionsModel.getBody().getBubble_style()).apply();
//                            }
//
//                            if (botOptionsModel.getGeneral() != null && botOptionsModel.getGeneral().getColors() != null && botOptionsModel.getGeneral().getColors().isUseColorPaletteOnly()) {
//                                botOptionsModel.getHeader().setBg_color(botOptionsModel.getGeneral().getColors().getSecondary());
//                                botOptionsModel.getFooter().setBg_color(botOptionsModel.getGeneral().getColors().getSecondary());
//                                botOptionsModel.getFooter().getCompose_bar().setOutline_color(botOptionsModel.getGeneral().getColors().getPrimary());
//                                botOptionsModel.getFooter().getCompose_bar().setInline_color(botOptionsModel.getGeneral().getColors().getSecondary_text());
//                                botOptionsModel.getHeader().getTitle().setColor(botOptionsModel.getGeneral().getColors().getPrimary());
//                                botOptionsModel.getHeader().getSub_title().setColor(botOptionsModel.getGeneral().getColors().getPrimary());
//                            }
//
//                            if (botOptionsModel.getWelcome_screen() != null && isWelcomeVisible) {
//                                if (botOptionsModel.getWelcome_screen().isShow()) showWelcomeDialog();
//                            }
//
//                            if (botOptionsModel.getOverride_kore_config() != null && botOptionsModel.getOverride_kore_config().isEnable()) {
//                                SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = botOptionsModel.getOverride_kore_config().isEmoji_short_cut();
//                                SDKConfiguration.OverrideKoreConfig.typing_indicator_timeout = botOptionsModel.getOverride_kore_config().getTyping_indicator_timeout();
//                                if (botOptionsModel.getOverride_kore_config().getHistory() != null) {
//                                    SDKConfiguration.OverrideKoreConfig.history_enable = botOptionsModel.getOverride_kore_config().getHistory().isEnable();
//                                    if (botOptionsModel.getOverride_kore_config().getHistory().getRecent() != null)
//                                        SDKConfiguration.OverrideKoreConfig.history_batch_size = botOptionsModel.getOverride_kore_config().getHistory().getRecent().getBatch_size();
//                                    if (botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll() != null) {
//                                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_enable = botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll().isEnable();
//                                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size = botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll().getBatch_size();
//                                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_loading_label = botOptionsModel.getOverride_kore_config().getHistory().getPaginated_scroll().getLoading_label();
//                                    }
//                                }
//                            }
//
//                            if (composeFooterFragment != null) {
//                                composeFooterFragment.setBotBrandingModel(botOptionsModel);
//                            }
//
//                            if (botContentFragment != null) {
//                                botContentFragment.setBotBrandingModel(botOptionsModel);
//                            }

//                        }
                    }
                }
//                closeProgressDialogue();
//                rlChatWindow.setVisibility(VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<BotActiveThemeModel> call, @NonNull Throwable t) {
                LogUtils.e("getBrandingDetails", t.toString());

//                closeProgressDialogue();
//                rlChatWindow.setVisibility(VISIBLE);
            }
        });
    }

//    public BrandingModel getBrandingDataFromTxt() {
//        BotActiveThemeModel botActiveThemeModel;
//        try {
//            InputStream is = context.getResources().openRawResource(R.raw.branding_response);
//            Reader reader = new InputStreamReader(is);
//            botActiveThemeModel = gson.fromJson(reader, BotActiveThemeModel.class);
//            if (botActiveThemeModel != null) {
//                BrandingModel brandingModel = new BrandingModel();
//                brandingModel.setBotchatBgColor(botActiveThemeModel.getBotMessage().getBubbleColor());
//                brandingModel.setBotchatTextColor(botActiveThemeModel.getBotMessage().getFontColor());
//                brandingModel.setUserchatBgColor(botActiveThemeModel.getUserMessage().getBubbleColor());
//                brandingModel.setUserchatTextColor(botActiveThemeModel.getUserMessage().getFontColor());
//
//                brandingModel.setButtonActiveBgColor(botActiveThemeModel.getButtons().getDefaultButtonColor());
//                brandingModel.setButtonActiveTextColor(botActiveThemeModel.getButtons().getDefaultFontColor());
//
//                brandingModel.setButtonInactiveBgColor(botActiveThemeModel.getButtons().getOnHoverButtonColor());
//                brandingModel.setButtonInactiveTextColor(botActiveThemeModel.getButtons().getOnHoverFontColor());
//                brandingModel.setButtonBorderColor(botActiveThemeModel.getButtons().getBorderColor());
//
//                brandingModel.setBotName(SDKConfiguration.Client.bot_name);
//                brandingModel.setWidgetBodyColor(botActiveThemeModel.getWidgetBody().getBackgroundColor());
//                brandingModel.setWidgetTextColor(botActiveThemeModel.getWidgetHeader().getFontColor());
//                brandingModel.setWidgetHeaderColor(botActiveThemeModel.getWidgetHeader().getBackgroundColor());
//                brandingModel.setWidgetFooterColor(botActiveThemeModel.getWidgetFooter().getBackgroundColor());
//                brandingModel.setWidgetFooterBorderColor(botActiveThemeModel.getWidgetFooter().getBorderColor());
//                brandingModel.setWidgetFooterHintColor(botActiveThemeModel.getWidgetFooter().getPlaceHolder());
//                return brandingModel;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void onEvent(BrandingModel brandingModel) {
//        SharedPreferences.Editor editor = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
//        editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingModel.getBotchatBgColor());
//        editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingModel.getBotchatTextColor());
//        editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, brandingModel.getUserchatBgColor());
//        editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, brandingModel.getUserchatTextColor());
//        editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, brandingModel.getButtonActiveBgColor());
//        editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, brandingModel.getButtonActiveTextColor());
//        editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, brandingModel.getButtonInactiveBgColor());
//        editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, brandingModel.getButtonInactiveTextColor());
//        editor.putString(BotResponse.WIDGET_BG_COLOR, brandingModel.getWidgetBodyColor());
//        editor.putString(BotResponse.WIDGET_TXT_COLOR, brandingModel.getWidgetTextColor());
//        editor.putString(BotResponse.WIDGET_BORDER_COLOR, brandingModel.getWidgetBorderColor());
//        editor.putString(BotResponse.BUTTON_BORDER_COLOR, brandingModel.getButtonBorderColor());
//        editor.putString(BotResponse.WIDGET_DIVIDER_COLOR, brandingModel.getWidgetDividerColor());
//        editor.putString(BotResponse.BUBBLE_STYLE, brandingModel.getChatBubbleStyle());
//        editor.apply();
//
//        SDKConfiguration.BubbleColors.quickReplyColor = brandingModel.getButtonActiveBgColor();
//        SDKConfiguration.BubbleColors.quickReplyTextColor = brandingModel.getButtonActiveTextColor();
//        SDKConfiguration.BubbleColors.quickBorderColor = brandingModel.getButtonBorderColor();
//
//        botChatView.onBrandingDetails(brandingModel);
//    }
}
