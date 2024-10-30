package kore.botssdk.repository.branding;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import io.reactivex.annotations.NonNull;
import kore.botssdk.R;
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
    Gson gson = new Gson();

    public BrandingRepository(Context context, BotChatViewListener chatView) {
        this.botChatView = chatView;
        this.context = context;
    }

    public void getBrandingDetails(String botId, String botToken, boolean isReconnection) {
        Call<BotActiveThemeModel> getBankingConfigService = BrandingRestBuilder.getRestAPI().getBrandingNewDetails(botId, "bearer " + botToken, "published", "1", "en_US", botId);
        getBankingConfigService.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BotActiveThemeModel> call, @NonNull Response<BotActiveThemeModel> response) {
                if (response.isSuccessful()) {
                    BotActiveThemeModel botActiveThemeModel = response.body();
                    if (botActiveThemeModel != null && botActiveThemeModel.getV3() != null) {
                        BotBrandingModel botOptionsModel = botActiveThemeModel.getV3();
                        botChatView.onBrandingDetails(botOptionsModel, !isReconnection);
                    } else getBrandingDataFromTxt(isReconnection);
                } else getBrandingDataFromTxt(isReconnection);
            }

            @Override
            public void onFailure(@NonNull Call<BotActiveThemeModel> call, @NonNull Throwable t) {
                LogUtils.e("getBrandingDetails", t.toString());
                getBrandingDataFromTxt(isReconnection);
            }
        });
    }

    public void getBrandingDataFromTxt(boolean isReconnection) {
        BotActiveThemeModel botActiveThemeModel;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.branding_response);
            Reader reader = new InputStreamReader(is);
            botActiveThemeModel = gson.fromJson(reader, BotActiveThemeModel.class);
            if (botActiveThemeModel != null) {
                if (botActiveThemeModel.getV3() != null) {
                    BotBrandingModel botOptionsModel = botActiveThemeModel.getV3();
                    botChatView.onBrandingDetails(botOptionsModel, !isReconnection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
