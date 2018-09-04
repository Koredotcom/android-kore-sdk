package kore.botssdk.net;

import java.util.HashMap;

import kore.botssdk.models.JWTTokenResponse;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by Ramachandra Pradeep on 15-Mar-17.
 */
public interface BotDemoRestAPI {
    String URL_VERSION = "/1.1";
    @Headers({
            "alg:RS256",
            "typ:JWT"
    })
    //@POST("/api/users/sts")
    @POST("/users/sts")
    JWTTokenResponse getJWTToken(@Body HashMap<String, Object> jsonObject);

    // Get JWT Token
    @POST("/api" + URL_VERSION + "/users/jwttoken")
    JWTTokenResponse getJWTToken(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    // Get JWT Token
    @POST("/api/users/sts")
    JWTTokenResponse getJWTToken(@Header("Authorization") String token);

}
