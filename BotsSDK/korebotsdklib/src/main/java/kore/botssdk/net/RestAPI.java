package kore.botssdk.net;


import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.models.BotActiveThemeModel;
import kore.botssdk.models.BotHistory;
import kore.botssdk.models.BotMetaModel;
import kore.botssdk.models.BrandingNewModel;
import kore.botssdk.models.JWTTokenResponse;
import kore.botssdk.models.KoreLoginResponse;
import kore.botssdk.models.PdfResponseModel;
import kore.botssdk.models.TokenResponseModel;
import kore.botssdk.models.WebHookResponseDataModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface RestAPI {

    String URL_VERSION = "/1.1";

    /** Login Service **/
    @POST("/api/oAuth/token")
    Call<RestResponse.LoginResponse> loginUser(@Body HashMap<String, Object> userCredentials);

    @POST("/api/oAuth/token")
    Call<KoreLoginResponse> loginNormalUser(@Body HashMap<String, Object> userCredentials);

    // Get JWT Token
    @POST("/api" + URL_VERSION + "/users/jwttoken")
    Call<JWTTokenResponse> getJWTToken(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    // Get JWT Token
    @POST("finastraLoginDEMO/token")
    Call<TokenResponseModel> getTokenJWT(@Body HashMap<String, Object> body);

    // Get JWT Token
    @POST("/api/users/sts")
    Call<RestResponse.JWTTokenResponse> getJWTToken(@Header("Authorization") String token);

    //Getting jwt grant
    @POST("/api/oAuth/token/jwtgrant")
    Call<RestResponse.BotAuthorization> jwtGrant(@Body HashMap<String,Object> jwtToken);

//    //Getting jwt grant Anonymous
//    @POST("/api/oAuth/token/jwtgrant/anonymous")
//    RestResponse.BotAuthorization jwtGrantAnonymous(@Body HashMap<String,Object> jwtToken);

    //Getting rtm URL
    @POST("/api/rtm/start")
    Call<RestResponse.RTMUrl> getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo);

    @POST("/api/rtm/start")
    Call<RestResponse.RTMUrl> getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo, @Query("isReconnect") boolean isReconnect);

    //Get Market Streams
//    @GET("/api/users/{userId}/builder/streams")
//    Call<MarketStreamList> getMarketStreams(@Path("userId") String userId, @Header("Authorization") String token);

    //Subscribe to Push notification
    @POST("/api/users/{userId}/sdknotifications/subscribe")
    Call<ResponseBody> subscribeForPushNotification(@Path("userId") String userId, @Header("Authorization") String token, @Body HashMap<String, Object> req);

    //Unsubscribe to Push notification
    @Headers({
            "Content-type: application/json",
            "X-HTTP-Method-Override:DELETE"
    })
    @POST("/api/users/{userId}/sdknotifications/unsubscribe")
    Call<ResponseBody> unSubscribeForPushNotification(@Path("userId") String userId, @Header("Authorization") String token, @Body HashMap<String, Object> body);

//    @GET("/api" + URL_VERSION + "/botmessages/rtm")
//    Call<BotHistory> getHistory(@Header("Authorization") String token, @Query("botId") String botId, @Query("limit") int limit, @Query("msgId") String msgId, @Query("direction") int forward);

    @GET("/api" + URL_VERSION + "/botmessages/rtm")
    Call<BotHistory> getBotHistory(@Header("Authorization") String token, @Query("botId") String botId, @Query("limit") int limit, @Query("offset") int offset, @Query("forward") boolean forward);

    // Get JWT Token
//    @GET("workbench/api/workbench/sdkData?objectId=hamburgermenu&objectId=brandingwidgetdesktop")
//    Call<ArrayList<BrandingNewModel>> getBrandingNewDetails(@Header("Authorization") String token, @Header("tenantId") String tenantId, @Header("state") String state, @Header("Accepts-version") String version, @Header("Accept-Language") String language, @Header("botid") String botId);

    @GET("api/websdkthemes/{botId}/activetheme")
    Call<BotActiveThemeModel> getBrandingNewDetails(@Path("botId") String bot_Id, @Header("Authorization") String token, @Header("state") String state, @Header("Accepts-version") String version, @Header("Accept-Language") String language, @Header("botid") String botId);

    // Send WebHook Message
    @POST("chatbot/v2/webhook/{streamId}")
    Call<WebHookResponseDataModel> sendWebHookMessage(@Path("streamId") String streamId, @Header("Authorization") String token, @Body HashMap<String, Object> body);

    @GET("/api/chathistory/{streamId}/ivr?")
    Call<BotHistory> getWebHookBotHistory(@Header("Authorization") String token, @Path("streamId") String streamId, @Query("botId") String botId, @Query("limit") int limit);

    @GET("api/botmeta/{streamId}")
    Call<BotMetaModel> getWebHookBotMeta(@Header("Authorization") String token, @Path("streamId") String streamId);

    @GET("chatbot/v2/webhook/{streamId}/poll/{pollId}")
    Call<WebHookResponseDataModel> getPollIdData(@Header("Authorization") String token, @Path("streamId") String streamId, @Path("pollId") String pollId);
    @POST
    Call<PdfResponseModel> getPdfBaseDetails(@Url String url, @HeaderMap HashMap<String, String> header, @Body JsonObject body);
    @POST
    Call<ResponseBody> getPdfDetails(@Url String url, @HeaderMap HashMap<String, String> header,  @Body JsonObject body);
}
