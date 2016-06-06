package kore.botssdk.net;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.models.MarketStreams;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface RestAPI {

    String URL_VERSION = "/1.1";

    /** Login Service **/
    @POST("/api" + URL_VERSION + "/oAuth/token")
    RestResponse.LoginResponse loginUser(@Body HashMap<String, Object> userCredentials);

    // Get JWT Token
    @POST("/api/users/sts")
    RestResponse.JWTTokenResponse getJWTToken(@Header("Authorization") String token);

    //Getting jwt grant
    @POST("/api/oAuth/token/jwtgrant")
    RestResponse.BotAuthorization jwtGrant(@Body HashMap<String,Object> jwtToken);

    //Getting rtm URL
    @POST("/api/rtm/start")
    RestResponse.RTMUrl getRtmUrl(@Header("Authorization") String token);

    //Get Market Streams
    @GET("/api/1.1/users/{userId}/builder/streams")
    MarketStreamList getMarketStreams(@Path("userId") String userId, @Header("Authorization") String token);

}
