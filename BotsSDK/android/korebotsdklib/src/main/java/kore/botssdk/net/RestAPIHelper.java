package kore.botssdk.net;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestAPIHelper {
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
}
