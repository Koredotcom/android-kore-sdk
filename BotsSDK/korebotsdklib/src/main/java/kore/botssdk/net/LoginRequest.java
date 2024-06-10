/*
package kore.botssdk.net;

import android.content.Context;

import java.util.HashMap;


public class LoginRequest extends RestRequest<RestResponse.LoginResponse> {

    protected HashMap<String, Object> userCredentials;
    private Context context;

    public LoginRequest(Context context, HashMap<String, Object> userCredentials) {
        this.userCredentials = userCredentials;
        this.context = context;
    }

    public Observable<RestResponse.LoginResponse> loadDataFromNetwork(){
        return Observable.create(new ObservableOnSubscribe<LoginResponse>() {
            @Override
            public void subscribe(ObservableEmitter<LoginResponse> emitter) throws Exception {
                try{
                    Call<LoginResponse> _resp = RestBuilder.getRestAPI().loginUser(userCredentials);
                    Response<LoginResponse> rBody = _resp.execute();
                    LoginResponse response = rBody.body();

                    emitter.onNext(response);
                    emitter.onComplete();
                }catch(Exception e){
                    emitter.onError(e);
                }
            }
        });
    }
}
*/
