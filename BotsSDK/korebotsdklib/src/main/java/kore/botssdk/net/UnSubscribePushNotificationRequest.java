package kore.botssdk.net;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UnSubscribePushNotificationRequest {

    private final String accessToken;
    private final String userId;
    private final HashMap<String, Object> request;

    public UnSubscribePushNotificationRequest(String userId, String accessToken, HashMap<String, Object> request) {
        this.userId = userId;
        this.request = request;
        this.accessToken = accessToken;
    }

    public Observable<ResponseBody> loadDataFromNetwork() {
        return Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseBody> emitter) throws Exception {
                try {
                    Call<ResponseBody> _responseBodyCall = RestBuilder.getRestAPI().unSubscribeForPushNotification(userId, accessToken, request);
                    Response<ResponseBody> rBody = _responseBodyCall.execute();
                    ResponseBody resp = rBody.body();

                    emitter.onNext(resp);
                    emitter.onComplete();
                }catch(Exception e){
                    emitter.onError(e);
                }
            }
        });
        //return response;
    }
}
