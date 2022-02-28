package com.kore.findlysdk.net;

import com.kore.findlysdk.models.AutoSuggestionsModel;
import com.kore.findlysdk.models.LiveSearchModel;
import com.kore.findlysdk.models.PopularSearchModel;
import com.kore.findlysdk.models.PopularSearchResultsModel;
import com.kore.findlysdk.models.RecentSearchesModel;
import com.kore.findlysdk.models.RestResponse;
import com.kore.findlysdk.models.ResultsViewModel;
import com.kore.findlysdk.models.SearchInterfaceModel;
import com.kore.findlysdk.models.SearchModel;
import com.kore.findlysdk.models.TabFacetsModel;

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
    @GET("{botid}/{sidx}/popularSearches")
    Call<PopularSearchResultsModel> getPopularSearch(@Path("botid") String botId, @Path("sidx") String sidx, @Header("Authorization") String token, @Header("auth") String jwtToken);

    @GET("{botid}/{sidx}/recentSearches")
    Call<RecentSearchesModel> getRecentSearches(@Path("botid") String botId, @Path("sidx") String sidx, @Header("Authorization") String token, @Header("auth") String jwtToken);

    @POST("{botid}/{sidx}/liveSearch")
    Call<LiveSearchModel> getLiveSearch(@Path("botid") String botId, @Path("sidx") String sidx,@Header("Authorization") String token, @Header("state") String state, @Body Object body, @Header("auth") String jwtToken);

    @GET("{botid}/{sidx}/resultTemplateSettings")
    Call<ResultsViewModel> getResultViewSettings(@Path("botid") String botId,@Path("sidx") String sidx, @Header("Authorization") String token, @Header("auth") String jwtToken);

    @GET("{botid}/{sidx}/searchInterface")
    Call<SearchInterfaceModel> getSearchInterface(@Path("botid") String botId, @Path("sidx") String sidx, @Header("auth") String token);

    @POST("{botid}/{sidx}/search")
    Call<SearchModel> getSearch(@Path("botid") String botId, @Path("sidx") String sidx, @Header("Authorization") String token, @Body Object body, @Header("auth") String jwtToken);

    //@POST("/api/users/sts")
    @POST("{botid}/{sidx}/unlockbot")
    Call<ArrayList<PopularSearchModel>> doUnlock(@Path("botid") String botId, @Path("sidx") String sidx, @Header("Authorization") String token, @Header("auth") String jwtToken, @Body Object body);

    // Get JWT Token
    @POST("/searchassistapi/users/sts")
    Call<RestResponse.JWTTokenResponse> getJWTToken(@Header("Authorization") String token);

    // Get JWT Token
    @POST("/searchassistapi/users/sts")
    Call<RestResponse.JWTTokenResponse> getJWTSearchToken(@Body Object body);

    @POST("{botid}/{sidx}/autoSuggestions")
    Call<AutoSuggestionsModel> getAutoSuggestions(@Path("botid") String botId, @Path("sidx") String sidx, @Header("Authorization") String token, @Body Object body, @Header("auth") String jwtToken);

    @GET("{botid}/{sidx}/tabfacet")
    Call<TabFacetsModel> getTabFacets(@Path("botid") String botId, @Path("sidx") String sidx, @Header("Authorization") String token, @Header("auth") String jwtToken);

    //Getting jwt grant
    @POST("/searchassistapi/oAuth/token/jwtgrant")
    Call<RestResponse.BotAuthorization> jwtGrant(@Body HashMap<String,Object> jwtToken);

    //Getting rtm URL
    @POST("/searchassistapi/rtm/start")
    Call<RestResponse.RTMUrl> getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo);

    @POST("/searchassistapi/rtm/start")
    Call<RestResponse.RTMUrl> getRtmUrl(@Header("Authorization") String token, @Body HashMap<String, Object> optParameterBotInfo, @Query("isReconnect") boolean isReconnect);
}
