package com.kore.findlysdk.net;

import com.kore.findlysdk.models.LiveSearchModel;
import com.kore.findlysdk.models.PopularSearchModel;
import com.kore.findlysdk.models.RestResponse;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.models.SearchModel;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ramachandra Pradeep on 15-Mar-17.
 */
public interface BotRestAPI {
    String URL_VERSION = "/1.1";
    @Headers({
            "alg:RS256",
            "typ:JWT"
    })
    //@POST("/api/users/sts")
    @GET("api"+URL_VERSION+"/searchAssist/{sidx}/popularSearches")
    Call<ArrayList<PopularSearchModel>> getPopularSearch(@Path("sidx") String sidx);

    @GET("api"+URL_VERSION+"findly/{sidx}/users/:userId/recentSearches")
    Call<ArrayList<PopularSearchModel>> getRecentlyAsked(@Path("sidx") String sidx);

//    @POST("searchAssistant/liveSearch/{sidx}")
//    Call<LiveSearchModel> getLiveSearch(@Path("sidx") String sidx, @Header("Authorization") String token, @Body Object body);

//    https://pilot.findly.ai/api/1.1/searchAssist/sidx-a0d5b74c-ef8d-51df-8cf0-d32617d3e66e/liveSearch

    @POST("api"+URL_VERSION+"/searchAssist/{sidx}/liveSearch")
    Call<LiveSearchModel> getLiveSearch(@Path("sidx") String sidx,@Header("Authorization") String token, @Header("state") String state, @Body Object body);

    @GET("api"+URL_VERSION+"/findly/{sidx}/getresultviewsettings")
    Call<ResultsViewModel> getResultViewSettings(@Path("sidx") String sidx, @Header("Authorization") String token);

    @POST("api"+URL_VERSION+"/findly/{sidx}/search")
    Call<SearchModel> getSearch(@Path("sidx") String sidx, @Header("Authorization") String token, @Body Object body);

    // Get JWT Token
    @POST("/api/users/sts")
    Call<RestResponse.JWTTokenResponse> getJWTToken(@Header("Authorization") String token);

    //Getting jwt grant
    @POST("/api/oAuth/token/jwtgrant")
    Call<RestResponse.BotAuthorization> jwtGrant(@Body HashMap<String,Object> jwtToken);

    //Getting rtm URL
    @POST("/api/rtm/start")
    Call<RestResponse.RTMUrl> getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo);

    @POST("/api/rtm/start")
    Call<RestResponse.RTMUrl> getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo, @Query("isReconnect") boolean isReconnect);
}
