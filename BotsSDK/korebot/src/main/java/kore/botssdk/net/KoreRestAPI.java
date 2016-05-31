package kore.botssdk.net;

import java.util.HashMap;

import retrofit.http.Body;
import retrofit.http.POST;

public interface KoreRestAPI {

    String URL_VERSION = "/1.1";

    /** Login Service **/
    @POST("/api" + URL_VERSION + "/oAuth/token")
    KoreRestResponse.KoreLoginResponse loginUser(@Body HashMap<String, Object> userCredentials);

}
