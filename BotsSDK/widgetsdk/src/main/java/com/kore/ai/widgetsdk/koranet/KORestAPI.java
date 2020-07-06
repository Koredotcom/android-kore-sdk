package com.kore.ai.widgetsdk.koranet;


import com.kore.ai.widgetsdk.applicationcontrol.ACMModel;
import com.kore.ai.widgetsdk.net.KaRestResponse;
import com.kore.ai.widgetsdk.restresponse.EmailVerifyResponse;
import com.kore.ai.widgetsdk.room.models.KORestResponse;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ramachandra Pradeep on 18-Jul-18.
 */

public interface KORestAPI {
    String URL_VERSION = "/1.1";
    /** Login Service **/
    @POST("/api" + URL_VERSION + "/oAuth/token")
    Call<KORestResponse.KOLoginResponse> loginUser(@Body HashMap<String, Object> userCredentials);

    /** Login Service **/
    @POST("/api" + URL_VERSION + "/sso/login")
    Call<KORestResponse.KOLoginResponse> loginSSoUser(@Body HashMap<String, Object> y);

    @GET("/api" + URL_VERSION + "/users/{userId}/isOnboarded")
    Call<KaRestResponse.KaCheckOnBoardingResponse> isUserOnBoarded(@Header("authorization") String token, @Path("userId") String userId);


    @GET("/api" + URL_VERSION + "/check_id_status")
    Call<EmailVerifyResponse> varifyEmailId(@Query("emailId") String email);

    //Getting Application control list
    @GET("/api" + URL_VERSION + "/ka/users/{userId}/AppControlPermissions")
    Call<ACMModel> getAppControlPermissions(@Path("userId") String userId, @Header("Authorization") String token);

    /** Send Forgot password verification link **/
    @POST("/api" + URL_VERSION + "/passwordReset")
    Call<String[]> sendForgotPasswordLink(@Body HashMap<String, String> email);


    @Headers({
            "Content-type:multipart/form-data;boundary=--------MultipartData0.4838800609634488"
    })
    @POST("/api" + URL_VERSION + "/signup_")
    Call<Object> signUp(@Body RequestBody reqbody);

    /** Resend SignUp verification link **/
    @POST("/api" + URL_VERSION + "/resend")
    Call<ResponseBody> resendActivationLink(@Body HashMap<String, String> email);

}
