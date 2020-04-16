/*
package kore.botssdk.net;

import android.content.Context;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import kore.botssdk.models.KoreLoginResponse;
import retrofit2.Call;
import retrofit2.Response;

*/
/**
 * Created by Ramachandra Pradeep on 1/9/2017.
 *//*

public class KoreLoginRequest extends RestRequest<KoreLoginResponse> {

    protected HashMap<String, Object> userCredentials;

    public KoreLoginRequest(HashMap<String, Object> userCredentials) {
        this.userCredentials = userCredentials;
    }

    public Observable<KoreLoginResponse> loadDataFromNetwork()  {
        return Observable.create(new ObservableOnSubscribe<KoreLoginResponse>() {
            @Override
            public void subscribe(ObservableEmitter<KoreLoginResponse> emitter) throws Exception {
                try{
                    Call<KoreLoginResponse> _resp = RestBuilder.getRestAPI().loginNormalUser(userCredentials);
                    Response<KoreLoginResponse> rBody = _resp.execute();
                    KoreLoginResponse loginResp = rBody.body();

                    emitter.onNext(loginResp);
                    emitter.onComplete();
                }catch(Exception e){
                    emitter.onError(e);
                }
            }
        });
        //KoreLoginResponse resp = RestBuilder.getRestAPI().loginNormalUser(userCredentials);
        //return null;//resp;
    }

}
*/
