package kore.botssdk.repository.branding;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import io.reactivex.annotations.NonNull;
import kore.botssdk.R;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.models.BotActiveThemeModel;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.BrandingRestBuilder;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("UnKnownNullness")
public class BrandingRepository {
    private final BotChatViewListener botChatView;
    private final Context context;
    private final Gson gson = new Gson();
    private final SharedPreferences sharedPreferences;

    public BrandingRepository(Context context, BotChatViewListener chatView) {
        this.botChatView = chatView;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
    }

    public void getBrandingDetails(String botId, String botToken) {
        Call<BotActiveThemeModel> getBankingConfigService = BrandingRestBuilder.getRestAPI().getBrandingNewDetails(botId, "bearer " + botToken, "published", "1", "en_US", botId);
        getBankingConfigService.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BotActiveThemeModel> call, @NonNull Response<BotActiveThemeModel> response) {
                if (response.isSuccessful()) {
                    BotActiveThemeModel botActiveThemeModel = response.body();
                    if (botActiveThemeModel != null && botActiveThemeModel.getV3() != null) {
                        BotBrandingModel botOptionsModel = botActiveThemeModel.getV3();
                        onBrandingDetails(botOptionsModel);
                    } else getBrandingDataFromTxt();
                } else getBrandingDataFromTxt();
            }

            @Override
            public void onFailure(@NonNull Call<BotActiveThemeModel> call, @NonNull Throwable t) {
                LogUtils.e("getBrandingDetails", t.toString());
                getBrandingDataFromTxt();
            }
        });
    }

    private void getBrandingDataFromTxt() {
        BotActiveThemeModel botActiveThemeModel;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.branding_response);
            Reader reader = new InputStreamReader(is);
            botActiveThemeModel = gson.fromJson(reader, BotActiveThemeModel.class);
            if (botActiveThemeModel != null) {
                if (botActiveThemeModel.getV3() != null) {
                    BotBrandingModel botOptionsModel = botActiveThemeModel.getV3();
                    botChatView.onBrandingDetails(botOptionsModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onBrandingDetails(BotBrandingModel botBrandingModel) {
        if (botBrandingModel != null) {
            BotBrandingModel configModel = SDKConfig.getBotBrandingConfigModel() != null ? SDKConfig.getBotBrandingConfigModel() : botBrandingModel;

            if (configModel.getGeneral() != null && configModel.getGeneral().getColors() != null && configModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                configModel.getHeader().setBg_color(configModel.getGeneral().getColors().getSecondary());
                configModel.getFooter().setBg_color(configModel.getGeneral().getColors().getSecondary());
                configModel.getFooter().getCompose_bar().setOutline_color(configModel.getGeneral().getColors().getPrimary());
                configModel.getFooter().getCompose_bar().setInline_color(configModel.getGeneral().getColors().getSecondary_text());
                configModel.getHeader().setAvatar_bg_color(configModel.getGeneral().getColors().getPrimary());
                configModel.getHeader().getTitle().setColor(configModel.getGeneral().getColors().getPrimary_text());
                configModel.getHeader().getSub_title().setColor(configModel.getGeneral().getColors().getPrimary_text());
            }

            botBrandingModel = botBrandingModel.updateWith(configModel);

            if (botBrandingModel.getBody() != null && botBrandingModel.getBody().getTime_stamp() != null) {
                String timeFormat = botBrandingModel.getBody().getTime_stamp().getTimeFormat();
                String dateFormat = botBrandingModel.getBody().getTime_stamp().getDateFormat();
                BotResponse.TIME_FORMAT = !StringUtils.isNullOrEmpty(timeFormat) ? Integer.parseInt(timeFormat) : 12;
                BotResponse.DATE_FORMAT = !StringUtils.isNullOrEmpty(dateFormat) ? dateFormat : BotResponse.DATE_FORMAT;
            }

            if (botBrandingModel.getChat_bubble() != null && !StringUtils.isNullOrEmpty(botBrandingModel.getChat_bubble().getStyle())) {
                sharedPreferences.edit().putString(BundleConstants.BUBBLE_STYLE, botBrandingModel.getChat_bubble().getStyle()).apply();
            }

            if (botBrandingModel.getBody() != null && !StringUtils.isNullOrEmpty(botBrandingModel.getBody().getBubble_style())) {
                sharedPreferences.edit().putString(BundleConstants.BUBBLE_STYLE, botBrandingModel.getBody().getBubble_style()).apply();
            }

            if (botBrandingModel.getOverride_kore_config() != null && botBrandingModel.getOverride_kore_config().isEnable()) {
                SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = botBrandingModel.getOverride_kore_config().isEmoji_short_cut();
                SDKConfiguration.OverrideKoreConfig.typing_indicator_timeout = botBrandingModel.getOverride_kore_config().getTyping_indicator_timeout();
                if (botBrandingModel.getOverride_kore_config().getHistory() != null) {
                    SDKConfiguration.OverrideKoreConfig.history_enable = botBrandingModel.getOverride_kore_config().getHistory().isEnable();
                    if (botBrandingModel.getOverride_kore_config().getHistory().getRecent() != null)
                        SDKConfiguration.OverrideKoreConfig.history_batch_size = botBrandingModel.getOverride_kore_config().getHistory().getRecent().getBatch_size();
                    if (botBrandingModel.getOverride_kore_config().getHistory().getPaginated_scroll() != null) {
                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_enable = botBrandingModel.getOverride_kore_config().getHistory().getPaginated_scroll().isEnable();
                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_batch_size = botBrandingModel.getOverride_kore_config().getHistory().getPaginated_scroll().getBatch_size();
                        SDKConfiguration.OverrideKoreConfig.paginated_scroll_loading_label = botBrandingModel.getOverride_kore_config().getHistory().getPaginated_scroll().getLoading_label();
                    }
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (botBrandingModel.getBody() != null && botBrandingModel.getBody().getBot_message() != null) {
                editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, botBrandingModel.getBody().getBot_message().getBg_color());
                editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, botBrandingModel.getBody().getBot_message().getColor());
            }

            if (botBrandingModel.getBody() != null && botBrandingModel.getBody().getUser_message() != null) {
                editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, botBrandingModel.getBody().getUser_message().getBg_color());
                editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, botBrandingModel.getBody().getUser_message().getColor());
            }

            if (botBrandingModel.getGeneral() != null && botBrandingModel.getGeneral().getColors() != null && botBrandingModel.getGeneral().getColors().isUseColorPaletteOnly()) {
                editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, botBrandingModel.getGeneral().getColors().getPrimary());
                editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, botBrandingModel.getGeneral().getColors().getPrimary_text());
                editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, botBrandingModel.getGeneral().getColors().getSecondary());
                editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, botBrandingModel.getGeneral().getColors().getSecondary_text());
                SDKConfiguration.BubbleColors.quickReplyColor = botBrandingModel.getGeneral().getColors().getSecondary();
                SDKConfiguration.BubbleColors.quickReplyTextColor = botBrandingModel.getGeneral().getColors().getPrimary();
                SDKConfiguration.BubbleColors.quickBorderColor = botBrandingModel.getGeneral().getColors().getSecondary_text();
                editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, botBrandingModel.getGeneral().getColors().getSecondary());
                editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, botBrandingModel.getGeneral().getColors().getPrimary_text());
                editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, botBrandingModel.getGeneral().getColors().getPrimary());
                editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, botBrandingModel.getGeneral().getColors().getSecondary_text());
            }
            editor.apply();
        }
        botChatView.onBrandingDetails(botBrandingModel);
    }
}
