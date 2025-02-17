package kore.botssdk.repository.branding;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import io.reactivex.annotations.NonNull;
import kore.botssdk.R;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.models.BotActiveThemeModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingModel;
import kore.botssdk.models.BrandingNewModel;
import kore.botssdk.models.BrandingV3Model;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.StringUtils;
import okhttp3.ResponseBody;
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
    String resp;

    public void getBrandingDetails(String botId, String botToken, String state, String version, String language) {
        Call<ResponseBody> getBankingConfigService = BrandingRestBuilder.getRestAPI().getBrandingNewDetails(botId, "bearer " + botToken, state, version, language, botId);
        getBankingConfigService.enqueue(new Callback<>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<ResponseBody> call, @androidx.annotation.NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        resp = response.body().string();
                        Type avtiveThemeType = new TypeToken<BotActiveThemeModel>() {
                        }.getType();
                        BotActiveThemeModel brandingNewDos = gson.fromJson(resp, avtiveThemeType);
                        if (brandingNewDos != null) {
                            BrandingModel brandingModel = new BrandingModel();
                            brandingModel.setBotchatBgColor(brandingNewDos.getBotMessage().getBubbleColor());
                            brandingModel.setBotchatTextColor(brandingNewDos.getBotMessage().getFontColor());
                            brandingModel.setUserchatBgColor(brandingNewDos.getUserMessage().getBubbleColor());
                            brandingModel.setUserchatTextColor(brandingNewDos.getUserMessage().getFontColor());

                            brandingModel.setButtonActiveBgColor(brandingNewDos.getButtons().getDefaultButtonColor());
                            brandingModel.setButtonActiveTextColor(brandingNewDos.getButtons().getDefaultFontColor());

                            brandingModel.setButtonInactiveBgColor(brandingNewDos.getButtons().getOnHoverButtonColor());
                            brandingModel.setButtonInactiveTextColor(brandingNewDos.getButtons().getOnHoverFontColor());
                            brandingModel.setButtonBorderColor(brandingNewDos.getButtons().getBorderColor());

                            brandingModel.setBotName(SDKConfiguration.Client.bot_name);
                            brandingModel.setWidgetBodyColor(brandingNewDos.getWidgetBody().getBackgroundColor());
                            brandingModel.setWidgetTextColor(brandingNewDos.getWidgetHeader().getFontColor());

                            brandingModel.setWidgetHeaderColor(brandingNewDos.getWidgetHeader().getBackgroundColor());
                            brandingModel.setWidgetFooterColor(brandingNewDos.getWidgetFooter().getBackgroundColor());
                            brandingModel.setWidgetFooterBorderColor(brandingNewDos.getWidgetFooter().getBorderColor());
                            brandingModel.setWidgetFooterHintColor(brandingNewDos.getWidgetFooter().getPlaceHolder());
                            brandingModel.setChatBubbleStyle(brandingNewDos.getGeneralAttributes().getBubbleShape());

                            if (!SDKConfiguration.BubbleColors.enableLocalBranding)
                                onEvent(brandingModel);
                            else onEventLocalBranding(brandingModel, SDKConfiguration.BubbleColors.localBranding);
                        } else {
                            throw new Exception("Something went wrong!");
                        }
                    } catch (Exception e) {
                        try {
                            Type avtiveThemeType = new TypeToken<BrandingNewModel>() {
                            }.getType();
                            BrandingNewModel brandingNewModel = gson.fromJson(resp, avtiveThemeType);
                            if (brandingNewModel != null) {
                                BrandingV3Model brandingNewDos = brandingNewModel.getV3();
                                try {
                                    BrandingModel brandingModel = new BrandingModel();
                                    brandingModel.setBotchatBgColor(brandingNewDos.getBody().getBot_message().getBg_color());
                                    brandingModel.setBotchatTextColor(brandingNewDos.getBody().getBot_message().getColor());

                                    brandingModel.setUserchatBgColor(brandingNewDos.getBody().getUser_message().getBg_color());
                                    brandingModel.setUserchatTextColor(brandingNewDos.getBody().getUser_message().getColor());

                                    brandingModel.setBotName(brandingNewDos.getHeader().getTitle().getName());
                                    brandingModel.setWidgetBodyColor(brandingNewDos.getBody().getBackground().getColor());
                                    brandingModel.setWidgetTextColor((brandingNewDos.getHeader().getTitle().getColor()));
                                    brandingModel.setWidgetHeaderColor(brandingNewDos.getHeader().getBg_color());
                                    brandingModel.setWidgetFooterColor(brandingNewDos.getFooter().getBg_color());
                                    brandingModel.setWidgetFooterBorderColor(brandingNewDos.getFooter().getCompose_bar().getOutline_color());
                                    brandingModel.setWidgetFooterHintColor(brandingNewDos.getFooter().getCompose_bar().getOutline_color());
                                    brandingModel.setWidgetFooterHintText(brandingNewDos.getFooter().getCompose_bar().getPlaceholder());
                                    brandingModel.setChatBubbleStyle(brandingNewDos.getChat_bubble().getStyle());

                                    if (brandingNewDos.getFooter() != null && brandingNewDos.getFooter().getButtons() != null) {
                                        if (brandingNewDos.getFooter().getButtons().getMicrophone() != null)
                                            SDKConfiguration.OverrideKoreConfig.showASRMicroPhone = brandingNewDos.getFooter().getButtons().getMicrophone().isShow();
                                        if (brandingNewDos.getFooter().getButtons().getAttachment() != null)
                                            SDKConfiguration.OverrideKoreConfig.showAttachment = brandingNewDos.getFooter().getButtons().getAttachment().isShow();
                                        if (brandingNewDos.getFooter().getButtons().getSpeaker() != null)
                                            SDKConfiguration.OverrideKoreConfig.showTextToSpeech = brandingNewDos.getFooter().getButtons().getSpeaker().isShow();
                                    }

                                    if (brandingNewDos.getGeneral() != null && brandingNewDos.getGeneral().getColors() != null && brandingNewDos.getGeneral().getColors().isUseColorPaletteOnly()) {
                                        brandingModel.setButtonActiveBgColor(brandingNewDos.getGeneral().getColors().getPrimary());
                                        brandingModel.setButtonActiveTextColor(brandingNewDos.getGeneral().getColors().getPrimary_text());
                                        brandingModel.setButtonInactiveBgColor(brandingNewDos.getGeneral().getColors().getSecondary());
                                        brandingModel.setButtonInactiveTextColor(brandingNewDos.getGeneral().getColors().getSecondary_text());
                                        SDKConfiguration.BubbleColors.quickReplyColor = brandingNewDos.getGeneral().getColors().getPrimary();
                                        SDKConfiguration.BubbleColors.quickReplyTextColor = brandingNewDos.getGeneral().getColors().getPrimary_text();
                                        brandingModel.setBotchatBgColor(brandingNewDos.getGeneral().getColors().getSecondary());
                                        brandingModel.setBotchatTextColor(brandingNewDos.getGeneral().getColors().getPrimary_text());
                                        brandingModel.setUserchatBgColor(brandingNewDos.getGeneral().getColors().getPrimary());
                                        brandingModel.setUserchatTextColor(brandingNewDos.getGeneral().getColors().getSecondary_text());
                                        brandingModel.setWidgetHeaderColor(brandingNewDos.getGeneral().getColors().getSecondary());
                                        brandingModel.setWidgetFooterColor(brandingNewDos.getGeneral().getColors().getSecondary());
                                        brandingModel.setWidgetTextColor((brandingNewDos.getGeneral().getColors().getPrimary_text()));
                                    }


                                    if (brandingNewDos.getOverride_kore_config() != null && brandingNewDos.getOverride_kore_config().isEnable()) {
                                        SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = brandingNewDos.getOverride_kore_config().isEmoji_short_cut();
                                        SDKConfiguration.OverrideKoreConfig.typing_indicator_timeout = brandingNewDos.getOverride_kore_config().getTyping_indicator_timeout();
                                        if (brandingNewDos.getOverride_kore_config().getHistory() != null) {
                                            SDKConfiguration.OverrideKoreConfig.history_enable = brandingNewDos.getOverride_kore_config().getHistory().isEnable();
                                            if (brandingNewDos.getOverride_kore_config().getHistory().getRecent() != null)
                                                SDKConfiguration.OverrideKoreConfig.history_batch_size = brandingNewDos.getOverride_kore_config().getHistory().getRecent().getBatch_size();
                                            if (brandingNewDos.getOverride_kore_config().getHistory().getPaginated_scroll() != null) {
                                                SDKConfiguration.OverrideKoreConfig.paginated_scroll_enable = brandingNewDos.getOverride_kore_config().getHistory().getPaginated_scroll().isEnable();
                                                SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size = brandingNewDos.getOverride_kore_config().getHistory().getPaginated_scroll().getBatch_size();
                                                SDKConfiguration.OverrideKoreConfig.paginated_scroll_loading_label = brandingNewDos.getOverride_kore_config().getHistory().getPaginated_scroll().getLoading_label();
                                            }
                                        }
                                    }

                                    if (!SDKConfiguration.BubbleColors.enableLocalBranding)
                                        onEvent(brandingModel);
                                    else onEventLocalBranding(brandingModel, SDKConfiguration.BubbleColors.localBranding);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                if (!SDKConfiguration.BubbleColors.enableLocalBranding)
                                    onEvent(getBrandingDataFromTxt());
                                else onEventLocalBranding(getBrandingDataFromTxt(), SDKConfiguration.BubbleColors.localBranding);
                            }

                        } catch (Exception ex) {
                            if (!SDKConfiguration.BubbleColors.enableLocalBranding)
                                onEvent(getBrandingDataFromTxt());
                            else onEventLocalBranding(getBrandingDataFromTxt(), SDKConfiguration.BubbleColors.localBranding);
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    if (!SDKConfiguration.BubbleColors.enableLocalBranding)
                        onEvent(getBrandingDataFromTxt());
                    else onEventLocalBranding(getBrandingDataFromTxt(), SDKConfiguration.BubbleColors.localBranding);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (!SDKConfiguration.BubbleColors.enableLocalBranding)
                    onEvent(getBrandingDataFromTxt());
                else onEventLocalBranding(getBrandingDataFromTxt(), SDKConfiguration.BubbleColors.localBranding);
            }
        });

    }

    public BrandingModel getBrandingDataFromTxt() {
        BotActiveThemeModel botActiveThemeModel;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.branding_response);
            Reader reader = new InputStreamReader(is);
            botActiveThemeModel = gson.fromJson(reader, BotActiveThemeModel.class);
            if (botActiveThemeModel != null) {
                BrandingModel brandingModel = new BrandingModel();
                brandingModel.setBotchatBgColor(botActiveThemeModel.getBotMessage().getBubbleColor());
                brandingModel.setBotchatTextColor(botActiveThemeModel.getBotMessage().getFontColor());
                brandingModel.setUserchatBgColor(botActiveThemeModel.getUserMessage().getBubbleColor());
                brandingModel.setUserchatTextColor(botActiveThemeModel.getUserMessage().getFontColor());

                brandingModel.setButtonActiveBgColor(botActiveThemeModel.getButtons().getDefaultButtonColor());
                brandingModel.setButtonActiveTextColor(botActiveThemeModel.getButtons().getDefaultFontColor());

                brandingModel.setButtonInactiveBgColor(botActiveThemeModel.getButtons().getOnHoverButtonColor());
                brandingModel.setButtonInactiveTextColor(botActiveThemeModel.getButtons().getOnHoverFontColor());
                brandingModel.setButtonBorderColor(botActiveThemeModel.getButtons().getBorderColor());

                brandingModel.setBotName(SDKConfiguration.Client.bot_name);
                brandingModel.setWidgetBodyColor(botActiveThemeModel.getWidgetBody().getBackgroundColor());
                brandingModel.setWidgetTextColor(botActiveThemeModel.getWidgetHeader().getFontColor());
                brandingModel.setWidgetHeaderColor(botActiveThemeModel.getWidgetHeader().getBackgroundColor());
                brandingModel.setWidgetFooterColor(botActiveThemeModel.getWidgetFooter().getBackgroundColor());
                brandingModel.setWidgetFooterBorderColor(botActiveThemeModel.getWidgetFooter().getBorderColor());
                brandingModel.setWidgetFooterHintColor(botActiveThemeModel.getWidgetFooter().getPlaceHolder());
                brandingModel.setChatBubbleStyle(botActiveThemeModel.getGeneralAttributes().getBubbleShape());
                return brandingModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public BrandingModel getBrandingLocal(String localBranding) {
        try {
            Type avtiveThemeType = new TypeToken<BotActiveThemeModel>() {
            }.getType();
            BotActiveThemeModel brandingNewDos = gson.fromJson(localBranding, avtiveThemeType);
            if (brandingNewDos != null) {
                BrandingModel brandingModel = new BrandingModel();
                brandingModel.setBotchatBgColor(brandingNewDos.getBotMessage().getBubbleColor());
                brandingModel.setBotchatTextColor(brandingNewDos.getBotMessage().getFontColor());
                brandingModel.setUserchatBgColor(brandingNewDos.getUserMessage().getBubbleColor());
                brandingModel.setUserchatTextColor(brandingNewDos.getUserMessage().getFontColor());

                brandingModel.setButtonActiveBgColor(brandingNewDos.getButtons().getDefaultButtonColor());
                brandingModel.setButtonActiveTextColor(brandingNewDos.getButtons().getDefaultFontColor());

                brandingModel.setButtonInactiveBgColor(brandingNewDos.getButtons().getOnHoverButtonColor());
                brandingModel.setButtonInactiveTextColor(brandingNewDos.getButtons().getOnHoverFontColor());
                brandingModel.setButtonBorderColor(brandingNewDos.getButtons().getBorderColor());

                brandingModel.setBotName(SDKConfiguration.Client.bot_name);
                brandingModel.setWidgetBodyColor(brandingNewDos.getWidgetBody().getBackgroundColor());
                brandingModel.setWidgetTextColor(brandingNewDos.getWidgetHeader().getFontColor());
                brandingModel.setWidgetHeaderColor(brandingNewDos.getWidgetHeader().getBackgroundColor());
                brandingModel.setWidgetFooterColor(brandingNewDos.getWidgetFooter().getBackgroundColor());
                brandingModel.setWidgetFooterBorderColor(brandingNewDos.getWidgetFooter().getBorderColor());
                brandingModel.setWidgetFooterHintColor(brandingNewDos.getWidgetFooter().getPlaceHolder());
                brandingModel.setChatBubbleStyle(brandingNewDos.getGeneralAttributes().getBubbleShape());
                onEvent(brandingModel);
            } else {
                throw new Exception("Something went wrong!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onEvent(BrandingModel brandingModel) {
        SharedPreferences.Editor editor = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingModel.getBotchatBgColor());
        editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingModel.getBotchatTextColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, brandingModel.getUserchatBgColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, brandingModel.getUserchatTextColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, brandingModel.getButtonActiveBgColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, brandingModel.getButtonActiveTextColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, brandingModel.getButtonInactiveBgColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, brandingModel.getButtonInactiveTextColor());
        editor.putString(BotResponse.WIDGET_BG_COLOR, brandingModel.getWidgetBodyColor());
        editor.putString(BotResponse.WIDGET_TXT_COLOR, brandingModel.getWidgetTextColor());
        editor.putString(BotResponse.WIDGET_BORDER_COLOR, brandingModel.getWidgetBorderColor());
        editor.putString(BotResponse.BUTTON_BORDER_COLOR, brandingModel.getButtonBorderColor());
        editor.putString(BotResponse.WIDGET_DIVIDER_COLOR, brandingModel.getWidgetDividerColor());
        editor.putString(BotResponse.BUBBLE_STYLE, brandingModel.getChatBubbleStyle());
        editor.apply();

        SDKConfiguration.BubbleColors.quickReplyColor = brandingModel.getButtonActiveBgColor();
        SDKConfiguration.BubbleColors.quickReplyTextColor = brandingModel.getButtonActiveTextColor();
        SDKConfiguration.BubbleColors.quickBorderColor = brandingModel.getButtonBorderColor();

        botChatView.onBrandingDetails(brandingModel);
    }

    public void onEventLocalBranding(BrandingModel brandingModel, BrandingModel localBranding) {
        SharedPreferences.Editor editor = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, StringUtils.isNotEmpty(localBranding.getBotchatBgColor()) ? localBranding.getBotchatBgColor() : brandingModel.getBotchatBgColor());
        editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, StringUtils.isNotEmpty(localBranding.getBotchatTextColor()) ? localBranding.getBotchatTextColor() : brandingModel.getBotchatTextColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, StringUtils.isNotEmpty(localBranding.getUserchatBgColor()) ? localBranding.getUserchatBgColor() : brandingModel.getUserchatBgColor());
        editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, StringUtils.isNotEmpty(localBranding.getUserchatTextColor()) ? localBranding.getUserchatTextColor() : brandingModel.getUserchatTextColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, StringUtils.isNotEmpty(localBranding.getButtonActiveBgColor()) ? localBranding.getButtonActiveBgColor() : brandingModel.getButtonActiveBgColor());
        editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, StringUtils.isNotEmpty(localBranding.getButtonActiveTextColor()) ? localBranding.getButtonActiveTextColor() : brandingModel.getButtonActiveTextColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, StringUtils.isNotEmpty(localBranding.getButtonInactiveBgColor()) ? localBranding.getButtonInactiveBgColor() : brandingModel.getButtonInactiveBgColor());
        editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, StringUtils.isNotEmpty(localBranding.getButtonInactiveTextColor()) ? localBranding.getButtonInactiveTextColor() : brandingModel.getButtonInactiveTextColor());
        editor.putString(BotResponse.WIDGET_BG_COLOR, StringUtils.isNotEmpty(localBranding.getWidgetBodyColor()) ? localBranding.getWidgetBodyColor() : brandingModel.getWidgetBodyColor());
        editor.putString(BotResponse.WIDGET_TXT_COLOR, StringUtils.isNotEmpty(localBranding.getWidgetTextColor()) ? localBranding.getWidgetTextColor() : brandingModel.getWidgetTextColor());
        editor.putString(BotResponse.WIDGET_BORDER_COLOR, StringUtils.isNotEmpty(localBranding.getWidgetBorderColor()) ? localBranding.getWidgetBorderColor() : brandingModel.getWidgetBorderColor());
        editor.putString(BotResponse.BUTTON_BORDER_COLOR, StringUtils.isNotEmpty(localBranding.getButtonBorderColor()) ? localBranding.getButtonBorderColor() : brandingModel.getButtonBorderColor());
        editor.putString(BotResponse.WIDGET_DIVIDER_COLOR, StringUtils.isNotEmpty(localBranding.getWidgetDividerColor()) ? localBranding.getWidgetDividerColor() : brandingModel.getWidgetDividerColor());
        editor.putString(BotResponse.BUBBLE_STYLE, StringUtils.isNotEmpty(localBranding.getChatBubbleStyle()) ? localBranding.getChatBubbleStyle() : brandingModel.getChatBubbleStyle());
        editor.apply();

        SDKConfiguration.BubbleColors.quickReplyColor = StringUtils.isNotEmpty(localBranding.getButtonActiveBgColor()) ? localBranding.getButtonActiveBgColor() : brandingModel.getButtonActiveBgColor();
        SDKConfiguration.BubbleColors.quickReplyTextColor = StringUtils.isNotEmpty(localBranding.getButtonActiveTextColor()) ? localBranding.getButtonActiveTextColor() : brandingModel.getButtonActiveTextColor();
        SDKConfiguration.BubbleColors.quickBorderColor = StringUtils.isNotEmpty(localBranding.getButtonBorderColor()) ? localBranding.getButtonBorderColor() : brandingModel.getButtonBorderColor();

        brandingModel.setWidgetHeaderColor(StringUtils.isNotEmpty(localBranding.getWidgetHeaderColor()) ? localBranding.getWidgetHeaderColor() : brandingModel.getWidgetHeaderColor());
        brandingModel.setWidgetFooterColor(StringUtils.isNotEmpty(localBranding.getWidgetFooterColor()) ? localBranding.getWidgetFooterColor() : brandingModel.getWidgetFooterColor());
        brandingModel.setWidgetFooterBorderColor(StringUtils.isNotEmpty(localBranding.getWidgetFooterBorderColor()) ? localBranding.getWidgetFooterBorderColor() : brandingModel.getWidgetFooterBorderColor());
        brandingModel.setWidgetFooterHintColor(StringUtils.isNotEmpty(localBranding.getWidgetFooterHintColor()) ? localBranding.getWidgetFooterHintColor() : brandingModel.getWidgetFooterHintColor());

        botChatView.onBrandingDetails(brandingModel);
    }
}
