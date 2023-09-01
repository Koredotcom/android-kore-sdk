package kore.botssdk.net;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;



public class RegisterPushNotificationRequest  {


    private final String userId;
    private final String token;
    private final HashMap<String, Object> request;

    public RegisterPushNotificationRequest(String userId, String accessToken, HashMap<String, Object> request) {
        this.request = request;
        this.userId = userId;
        this.token = accessToken;
    }

    public Observable<ResponseBody> loadDataFromNetwork() {
        return Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseBody> emitter) throws Exception {
                try{
                    Call<ResponseBody> _resp = RestBuilder.getRestAPI().subscribeForPushNotification(userId, token, request);
                    Response<ResponseBody> rbody = _resp.execute();
                    ResponseBody resp = rbody.body();

                    emitter.onNext(resp);
                    emitter.onComplete();

                }catch(Exception e){
                    emitter.onError(e);
                }
            }
        });
        //Response response = RestBuilder.getRestAPI().subscribeForPushNotification(userId, accessTokenHeader(), request);
        //return response;
    }
}
