package kore.botssdk.net;

import java.util.HashMap;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

public interface KoreRestAPI {

    String URL_VERSION = "/1.1";

    /** Login Service **/
    @POST("/api" + URL_VERSION + "/oAuth/token")
    KoreRestResponse.KoreLoginResponse loginUser(@Body HashMap<String, Object> userCredentials);

    // Get JWT Token
    @POST("/api/users/sts")
    KoreRestResponse.JWTTokenResponse getJWTToken(@Header("Authorization") String token);

    //Getting jwt grant
    @POST("/api/oAuth/token/jwtgrant")
    KoreRestResponse.BotAuthorization jwtGrant(@Body HashMap<String,Object> jwtToken);

    //Getting rtm URL
    @POST("/api/rtm/start")
    KoreRestResponse.RTMUrl getRtmUrl(@Header("Authorization") String token);

}
