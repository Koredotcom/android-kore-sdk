package kore.botssdk.net;

import java.util.HashMap;

import kore.botssdk.models.BotSpeechSocketStream;
import kore.botssdk.models.KoreLoginResponse;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface RestAPI {

//    String URL_VERSION = "/1.1";

    /** Login Service **/
    @POST("/api/oAuth/token")
    RestResponse.LoginResponse loginUser(@Body HashMap<String, Object> userCredentials);

    @POST("/api/oAuth/token")
    KoreLoginResponse loginNormalUser(@Body HashMap<String, Object> userCredentials);

    // Get JWT Token
    @POST("/api/users/sts")
    RestResponse.JWTTokenResponse getJWTToken(@Header("Authorization") String token);

    //Getting jwt grant
    @POST("/api/oAuth/token/jwtgrant")
    RestResponse.BotAuthorization jwtGrant(@Body HashMap<String,Object> jwtToken);

//    //Getting jwt grant Anonymous
//    @POST("/api/oAuth/token/jwtgrant/anonymous")
//    RestResponse.BotAuthorization jwtGrantAnonymous(@Body HashMap<String,Object> jwtToken);

    //Getting rtm URL
    @POST("/api/rtm/start")
    RestResponse.RTMUrl getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo);

    @POST("/api/rtm/start")
    RestResponse.RTMUrl getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo, @Query("isReconnect") boolean isReconnect);

    //Get Market Streams
    @GET("/api/users/{userId}/builder/streams")
    MarketStreamList getMarketStreams(@Path("userId") String userId, @Header("Authorization") String token);

    //Get Speech Socket URL
    @POST("/asr/wss/start")
    BotSpeechSocketStream getSpeechSocketStream(@Header("Authorization") String token, @Query("email") String email);

    //Subscribe to Push notification
    @POST("/api/users/{userId}/sdknotifications/subscribe")
    Response subscribeForPushNotification(@Path("userId") String userId, @Header("Authorization") String token, @Body HashMap<String, Object> req);

    //Unsubscribe to Push notification
    @Headers({
            "Content-type: application/json",
            "X-HTTP-Method-Override:DELETE"
    })
    @POST("/api/users/{userId}/sdknotifications/unsubscribe")
    Response unSubscribeForPushNotification(@Path("userId") String userId, @Body HashMap<String, Object> body);

}
