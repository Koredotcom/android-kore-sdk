/*
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
    private HashMap<String, Object> request;

    public UnSubscribePushNotificationRequest(String accessToken, HashMap<String, Object> request) {
        this.request = request;
        this.accessToken = accessToken;
    }

    public Observable<ResponseBody> loadDataFromNetwork() {
        return Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseBody> emitter) throws Exception {
                try {
                    Call<ResponseBody> _responseBodyCall = RestBuilder.getRestAPI().unSubscribeForPushNotification(accessToken, request);
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
*/
