package com.kore.ai.widgetsdk.net;

import com.kore.ai.widgetsdk.models.JWTTokenResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Ramachandra Pradeep on 15-Mar-17.
 */
public interface BotJWTRestAPI {
    String URL_VERSION = "/1.1";
    @Headers({
            "alg:RS256",
            "typ:JWT"
    })
    //@POST("/api/users/sts")
    @POST("/dev/users/sts")
    Call<JWTTokenResponse> getJWTToken(@Body HashMap<String, Object> jsonObject);

    // Get JWT Token
    @POST("/api" + URL_VERSION + "/users/jwttoken")
    Call<JWTTokenResponse> getJWTToken(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    // Get JWT Token
    @POST("/api/users/sts")
    Call<JWTTokenResponse> getJWTToken(@Header("Authorization") String token);

}
