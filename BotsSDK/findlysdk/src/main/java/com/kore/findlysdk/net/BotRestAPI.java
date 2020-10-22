package com.kore.findlysdk.net;

import com.kore.findlysdk.models.LiveSearchModel;
import com.kore.findlysdk.models.PopularSearchModel;
import com.kore.findlysdk.models.SearchModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    @GET("api"+URL_VERSION+"/searchAssist/sidx-f3a43e5f-74b6-5632-a488-8af83c480b88/popularSearches")
    Call<ArrayList<PopularSearchModel>> getPopularSearch();

    @GET("api"+URL_VERSION+"findly/sidx-f3a43e5f-74b6-5632-a488-8af83c480b88/users/:userId/recentSearches")
    Call<ArrayList<PopularSearchModel>> getRecentlyAsked();

    @POST("searchAssistant/liveSearch/{sidx}")
    Call<LiveSearchModel> getLiveSearch(@Path("sidx") String sidx, @Header("Authorization") String token, @Body Object body);

    @POST("searchAssistant/search/{sidx}")
    Call<SearchModel> getSearch(@Path("sidx") String sidx, @Header("Authorization") String token, @Body Object body);
}
