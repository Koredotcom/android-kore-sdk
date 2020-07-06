package com.kore.ai.widgetsdk.net;

import com.kore.ai.widgetsdk.interfaces.PinUnPinnCallBack;
import com.kore.ai.widgetsdk.models.PinWidget;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.utils.Utils;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ramachandra Pradeep on 27-Oct-18.
 */

public class KaRestAPIHelper {
    private static final int DEFAULT_RETRIES = 1;

    public static <T> void enqueueWithRetry(Call<T> call, final int retryCount, final Callback<T> callback) {
        call.enqueue(new RetryableCallback<T>(call, retryCount) {

            @Override
            public void onFinalResponse(Call<T> call, Response<T> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFinalFailure(Call<T> call, Throwable t) {
//                if(t instanceof IOException && KORestBuilder.mContext != null)
//                    Toast.makeText(KORestBuilder.mContext,"No Network Available!",Toast.LENGTH_LONG).show();
                callback.onFailure(call, t);
            }
        });
    }

    public static <T> void enqueueWithRetry(Call<T> call, final Callback<T> callback) {
        enqueueWithRetry(call, DEFAULT_RETRIES, callback);
    }

    static boolean isCallSuccess(Response response) {
        int code = response.code();
        return (code >= 200 && code < 400);
    }



    public static  void actionPinnAndUnPinn(String accessToken, String userId, Widget widget, PanelLevelData panelData, final PinUnPinnCallBack callBack)
    {
        PinWidget pinWidget = new PinWidget();
        pinWidget.setSkillId(panelData.getSkillId());
        pinWidget.setPanelId(panelData.get_id());
        pinWidget.setPanelName(panelData.getName());


        pinWidget.setWidgetTitle(widget.getName());
        pinWidget.setWidgetId(widget.get_id());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(widget.isPinned() ? "unpinWidget" : "pinWidget", pinWidget);

        Call<ResponseBody> call = KaRestBuilder.getKaRestAPI().pinUnPinAction(Utils.ah(accessToken),userId, hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                callBack.success();
                else
                callBack.failure(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.failure(t);

            }
        });

    }

}
