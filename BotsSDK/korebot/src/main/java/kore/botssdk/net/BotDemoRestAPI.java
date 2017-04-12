package kore.botssdk.net;

import java.util.HashMap;

import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by Ramachandra Pradeep on 15-Mar-17.
 */
public interface BotDemoRestAPI {

    @Headers({
            "alg:RS256",
            "typ:JWT"
    })
    //@POST("/api/users/sts")
    @POST("/users/sts")
    RestResponse.JWTTokenResponse getJWTToken(@Body HashMap<String, Object> jsonObject);

}
