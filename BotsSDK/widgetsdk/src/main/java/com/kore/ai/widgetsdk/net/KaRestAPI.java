package com.kore.ai.widgetsdk.net;

import com.kore.ai.widgetsdk.models.AnnoucementResModel;
import com.kore.ai.widgetsdk.models.AnnouncementPanelNewResponse;
import com.kore.ai.widgetsdk.models.BotTableListTemplateModel;
import com.kore.ai.widgetsdk.models.CalEventsTemplateModel;
import com.kore.ai.widgetsdk.models.CreateEmailActionRequest;
import com.kore.ai.widgetsdk.models.CreateEmailActionResponse;
import com.kore.ai.widgetsdk.models.CreateMeetingNotesResponse;
import com.kore.ai.widgetsdk.models.DeviceContactsModel;
import com.kore.ai.widgetsdk.models.DeviceContactsTimeStampResponse;
import com.kore.ai.widgetsdk.models.EmailAvailablityResponseModel;
import com.kore.ai.widgetsdk.models.EmailSuggestionModel;
import com.kore.ai.widgetsdk.models.FeatureUtteranceModel;
import com.kore.ai.widgetsdk.models.FeedbackDataResponse;
import com.kore.ai.widgetsdk.models.GetDomainInfoModel;
import com.kore.ai.widgetsdk.models.JWTTokenResponse;
import com.kore.ai.widgetsdk.models.KnowledgeDetailModel;
import com.kore.ai.widgetsdk.models.KnowledgeDetailModelResponseNew;
import com.kore.ai.widgetsdk.models.KoraHelpModel;
import com.kore.ai.widgetsdk.models.KoreLoginResponse;
import com.kore.ai.widgetsdk.models.KoreSToken;
import com.kore.ai.widgetsdk.models.MeetingAttendingRequestModel;
import com.kore.ai.widgetsdk.models.MeetingDetailModel;
import com.kore.ai.widgetsdk.models.MeetingDraftResponse;
import com.kore.ai.widgetsdk.models.MeetinngActionResponse;
import com.kore.ai.widgetsdk.models.PanelResponseData;
import com.kore.ai.widgetsdk.models.PanelWidgetsDataModel;
import com.kore.ai.widgetsdk.models.RecentSkills;
import com.kore.ai.widgetsdk.models.SkillsListResponse;
import com.kore.ai.widgetsdk.models.SummaryViewResponseModel;
import com.kore.ai.widgetsdk.models.TaskCompletionModel;
import com.kore.ai.widgetsdk.models.TaskTemplateModel;
import com.kore.ai.widgetsdk.models.TaskTemplateResponse;
import com.kore.ai.widgetsdk.models.TrendingHahTagPanelNewResponse;
import com.kore.ai.widgetsdk.models.WUpcomingFilesModel;
import com.kore.ai.widgetsdk.models.WUpcomingMeetingModel;
import com.kore.ai.widgetsdk.models.WUpcomingTasksModel;
import com.kore.ai.widgetsdk.models.WeatherWidgetModel;
import com.kore.ai.widgetsdk.models.Widget;
import com.kore.ai.widgetsdk.models.WidgetListDataModel;
import com.kore.ai.widgetsdk.models.WidgetTableListDataModel;
import com.kore.ai.widgetsdk.models.WidgetsDataModel;
import com.kore.ai.widgetsdk.models.WidgetsWidgetModel;
import com.kore.ai.widgetsdk.room.models.KORestResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created on 11/16/2017.
 */

public interface KaRestAPI {
    String URL_VERSION = "/1.1";

    /**
     * Login Service
     **/
//    @POST("/api/oAuth/token")
//    KoreLoginResponse loginUser(@Body HashMap<String, Object> userCredentials);
    @POST("/api" + URL_VERSION + "/oAuth/token")
    Call<KORestResponse.KOLoginResponse> refreshAccessToken(@Body HashMap<String, Object> userCredentials);

    // Get JWT Token
    @POST("/api" + URL_VERSION + "/users/jwttoken")
    Call<JWTTokenResponse> getJWTToken(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    // Get JWT Token
    @POST("/api" + URL_VERSION + "/users/jwttoken")
    Call<JWTTokenResponse> getJWTTokenS(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    @POST("/api" + URL_VERSION + "/users/{userId}/jwt/sign")
    KaRestResponse.JWTKeyResponse getJwtKey(@Path("userId") String userId, @Header("Authorization") String token, @Body Object body);


    @Headers({
            "alg:RS256",
            "typ:JWT"
    })
    //@POST("/api/users/sts")
    @POST("/users/sts")
    JWTTokenResponse getJWTToken(@Body HashMap<String, Object> jsonObject);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge")
    Call<KaRestResponse.KnowledgeInfo> createKnowledge(@Header("Authorization") String token, @Path("userId") String userId, @Body KnowledgeDetailModel jsonObject, @Query("questionId") String qId);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kId}/edit")
    Call<KaRestResponse.KnowledgeInfo> editKnowledge(@Header("Authorization") String token, @Path("userId") String userId, @Body KnowledgeDetailModel jsonObject, @Path("kId") String kId);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}")
    Call<KnowledgeDetailModel> getKnowledge(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}")
    Call<KnowledgeDetailModel> getKnowledgeFullData(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Query("type") String type);



    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}")
    Call<KnowledgeDetailModel> deleteKnowledge(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid);


//    @POST("/api" + URL_VERSION + "/users/{userId}/builder/knowledgetasks")
//    Call<KaRestResponse.KnowledgeInfo> createKnowledge(@Header("Authorization") String token, @Path("userId") String userId, @Body CreateKnowledgeBotModel jsonObject);
//
//    @POST("/api" + URL_VERSION + "/users/{userId}/builder/faqs")
//    KaRestResponse.KnowledgeInfo createFaq(@Header("Authorization") String token, @Path("userId") String userId, @Body CreateFaqModel jsonObject);

    @POST("/api" + URL_VERSION + "/users/{userId}/builder/knowledgeTasks/faq/train")
    KaRestResponse.KnowledgeInfo trainFaq(@Header("Authorization") String token, @Path("userId") String userId, @Query("streamId") String streamId, @Query("ktId") String ktId, @Body HashMap jsonObject);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/recent")
    Call<KaRestResponse.RecentKnowledgeResponse> getRecentKnowledge(@Header("Authorization") String token, @Path("userId") String userId, @Query("q") String query, @Query("offSet") int offSet, @Query("offSetTS") long lastMod, @Query("limit") int limit);



    @POST
    Call<KnowledgeDetailModelResponseNew> getRecentKnowledgePanel(@Header("Authorization") String token, @Url String url, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);


    @POST
    Call<TrendingHahTagPanelNewResponse> getTrendingHahTagPanel(@Header("Authorization") String token, @Url String url, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);
//
//
    @POST
    Call<AnnouncementPanelNewResponse> getRecentAnnoucementPanel(@Header("Authorization") String token, @Url String url, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/recent")
    Call<ArrayList<AnnoucementResModel>> getRecentAnnoucement(@Header("Authorization") String token, @Path("userId") String userId, @Query("type") String announcement, @Query("q") String querytype, @Query("offSet") int offSet, @Query("offSetTS") long lastMod, @Query("limit") int limit);


    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/resolve")
    Call<ArrayList<AnnoucementResModel>> resolveAnnouncement(@Header("Authorization") String token, @Path("userId") String userId, @Query("type") String announcement, @Body HashMap jsonObject);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/hashTag")
    Call<KaRestResponse.AutoSuggestionsResponse> getAutoSuggestions(@Header("Authorization") String token, @Path("userId") String userId, @Query("q") String query);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/trendinghashtags")
    Call<KaRestResponse.TrendingHashtagResponse> getTrendingHashtags(@Header("Authorization") String token, @Path("userId") String userId);


    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/reaction")
    Call<KaRestResponse.LikeResponse> likeInfo(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/reaction")
    Call<KaRestResponse.LikeResponse> getLikes(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Query("offSet") int offSet, @Query("limit") int limit);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/activity")
    Call<KaRestResponse.UserContext> getUserContext(@Header("Authorization") String token, @Path("userId") String userId);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/follow")
    Call<KaRestResponse.FollowResponse> followInfo(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/follow")
    Call<KaRestResponse.FollowResponse> getFollowers(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Query("offSet") int offSet, @Query("limit") int limit);


    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/comment")
    Call<KaRestResponse.CommentResponse> commentOnInfo(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/share")
    Call<KaRestResponse.ShareResponse> getSharedUsers(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/reaction")
    Call<KaRestResponse.VoteResponse> voteInfo(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/reaction")
    Call<KaRestResponse.VoteResponse> getVotes(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Query("offSet") int offSet, @Query("limit") int limit);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/share")
    Call<KaRestResponse.ShareResponse> shareInfo(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Query("notify") boolean notify, @Body HashMap jsonObject);

    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/share")
    Call<KaRestResponse.ShareResponse> updateSharePrivilege(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/share")
    Call<KaRestResponse.ShareResponse> unShareArticle(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/train")
    Call<KaRestResponse.QuestionModel> trainInfo(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @Headers({
            "Content-type: application/json",
            "X-HTTP-Method-Override:DELETE"
    })
    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/train")
    Call<KaRestResponse.QuestionModel> deleteQuestion(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/train")
    Call<KaRestResponse.QuestionModel> editQuestion(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Body HashMap jsonObject);

    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/train")
    Call<KaRestResponse.QuestionModel> deleteAllQuestions(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Query("deleteAll") Boolean deleteAll);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/train")
    Call<KaRestResponse.QuestionsResponse> getQuestions(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid);

    @POST("/api" + URL_VERSION + "/ka/utils/users/{userId}/unfurl")
    KaRestResponse.URLUnfurlResponse unfURL(@Header("Authorization") String token, @Path("userId") String userId, @Body HashMap jsonObject);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/resolve")
    Call<ArrayList<KnowledgeDetailModel>> resolveKnowledges(@Header("Authorization") String token, @Path("userId") String userId, @Body HashMap jsonObject);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/{action}")
    Call<KaRestResponse.PaginateInfoDetails> getPaginatedResultsByAction(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Path("action") String action, @Query("offSet") int offSet, @Query("offSetTS") long lastMod, @Query("limit") int limit);

    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/knowledge/{kid}/{action}/{resourceId}")
    Call<KaRestResponse.DeleteResource> deleteResource(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Path("action") String action, @Path("resourceId") String resourceId);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/{kid}/{fileId}/signedMediaURL")
    Call<KaRestResponse.KaSignedMediaUrlResponse> getSignedMediaURL(@Header("Authorization") String token, @Path("userId") String userId, @Path("kid") String kid, @Path("fileId") String fileId);

    @GET("/api" + URL_VERSION + "/users/{userId}/isOnboarded")
    Call<KaRestResponse.KaCheckOnBoardingResponse> isUserOnboarded(@Header("authorization") String token, @Path("userId") String userId);

    @PUT("/api" + URL_VERSION + "/users/{userId}/isOnboarded")
    Call<KaRestResponse.KaCheckOnBoardingResponse> isUserOnboarded(@Header("authorization") String token, @Path("userId") String userId, @Body HashMap<String, Boolean> body);

    /**
     * Teams Related End points
     */
//    @POST("/api" + URL_VERSION + "/ka/users/{userId}/teams")
//    Call<KaTeamModel> createTeam(@Header("Authorization") String token, @Path("userId") String userId, @Body KaTeamModel kaTeamModel);
//
//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams/colors")
//    Call<ArrayList<ColorModel>> getColors(@Header("Authorization") String token, @Path("userId") String userId);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams/checkavailability")
    Call<KaRestResponse.KnowledgeInfo> checkNameAvailability(@Header("Authorization") String token, @Path("userId") String userId, @Query("name") String name, @Query("atMention") String atMention);

//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/members")
//    Call<TeamMembersResponse> getTeamMembers(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Query("limit") int limit, @Query("offSet") int offSet, @Query("q") String query);
//
//    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/members")
//    Call<TeamMembersResponse> changeTeamMemberPrivilege(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Body TeamMembersResponse payLoad);

//    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}")
//    Call<KaTeamModel> editTeam(@Header("Authorization") String token, @Path("userId") String userId, @Body KaTeamModel kaTeamModel, @Path("teamId") String teamId);
//
//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}")
//    Call<KaTeamModel> getTeam(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId);
//
//    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}")
//    Call<KaTeamModel> deleteTeam(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId);
//
//    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}")
//    Call<KaTeamModel> leaveTeam(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Query("leave") boolean leave, @Query("privilege") int privilege);

//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams?limit=100")
//    Call<KaRestResponse.KoraTeams> getTeams(@Header("Authorization") String token, @Path("userId") String userId);

//    @POST("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/members")
//    Call<TeamMembersResponse> addMembersToTeam(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Body TeamMembersResponse payLoad);

    @GET("/api" + URL_VERSION + "/appversionupgradestatus")
    Call<KoreVersionUpgradeResponse> fetchVersionUpgradeStatus(@Header("Authorization") String token, @Query("appId") String appId, @Query("version") String version);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/knowledge")
    Call<KaRestResponse.RecentKnowledgeResponse> getTeamKnowledges(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Query("offSet") int offSet, @Query("offSetTS") long lastMod, @Query("limit") int limit);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/pending")
    Call<KaRestResponse.RecentKnowledgeResponse> getPendingKnowledges(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Query("offSet") int offSet, @Query("offSetTS") long lastMod, @Query("limit") int limit);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/pending/knowledge/{kid}")
    Call<KnowledgeDetailModel> getPendingKnowledge(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Path("kid") String kid);


//    @POST("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/pending")
//    Call<KaRestResponse.KnowledgeInfo> applyAction(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Body KnowledgeActionModel knowledgeActionModel);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/teams/{teamId}/knowledge")
    Call<KaRestResponse.KnowledgeInfo> createKnowledgeInTeam(@Header("Authorization") String token, @Path("userId") String userId, @Path("teamId") String teamId, @Body KnowledgeDetailModel createKnowledgeModel);

    /**
     * /ka/users/:userId/contacts
     * Contacts fetching end Point
     */
    /**
     * Get Contacts
     **/
    @GET("/api" + URL_VERSION + "/ka/users/{userId}/entities/contacts")
    Call<KaRestResponse.ContactList> getContacts(@Path("userId") String userId, @Header("Authorization") String token, @Query("offSet") long offset, @Query("limit") int limit, @Query("team") String team, @Query("q") String q);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/entities")
    Call<KaRestResponse.ContactList> getEntities(@Path("userId") String userId, @Header("Authorization") String token, @Query("offSet") long offset, @Query("limit") int limit, @Query("knowledge") String knowledge, @Query("q") String q);

//    @GET("/api" + URL_VERSION + "/_resolve/user")
//    Call<KaRestResponse.UsersList> getContactsByIds(@Header("Authorization") String token, @Query("id") ArrayList<String> ids);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/meetingrequest")
    Call<KaRestResponse.MeetingsList> getMeetingsList(@Path("userId") String userId, @Header("Authorization") String token);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/notifications")
    Call<KaRestResponse.Notifications> getNotifications(@Path("userId") String userId, @Header("Authorization") String token);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/notifications/updates")
    Call<KaRestResponse.Notifications> getUpdatesByPaginate(@Path("userId") String userId, @Header("Authorization") String token, @Query("offSet") int offSet, @Query("limit") int limit);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/notifications/actions/all")
    Call<KaRestResponse.Notifications> getActionsByPaginate(@Path("userId") String userId, @Header("Authorization") String token, @Query("offSet") int offSet, @Query("limit") int limit);

    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/notifications/updates")
    Call<KaRestResponse.Notifications> updateLastSeen(@Path("userId") String userId, @Header("Authorization") String token, @Body HashMap body);


//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/profile")
//    Call<KaUserProfileModel> getProfile(@Header("Authorization") String token, @Path("userId") String userId);
//
//    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/profile")
//    Call<KaUserProfileModel> updateProfile(@Header("Authorization") String token, @Path("userId") String userId, @Body KaUserProfileModel kaUserProfileModel);


    @POST("/api" + URL_VERSION + "/users/{userId}/notifications/push/subscribe")
    Call<ResponseBody> registerForPush(@Path("userId") String userId, @Header("Authorization") String token, @Body HashMap<String, Object> req);
    //code for the resolution of ids

    @Headers({
            "Content-type: application/json",
            "X-HTTP-Method-Override:DELETE"
    })
    @POST("/api" + URL_VERSION + "/users/{userId}/notifications/push/subscribe")
    Call<ResponseBody> unSubscribePushNotification(@Path("userId") String userId, @Header("Authorization") String token, @Body Object body);

    @Headers({
            "Content-type: application/json",
            "X-HTTP-Method-Override:DELETE"
    })
    @POST("/api" + URL_VERSION + "/notifications/unsubscribe")
    Call<ResponseBody> unSubscribeDeviceFromAllNotifications(@Body HashMap<String, String> body);

    @Headers({
            "Content-type: application/json",
            "X-HTTP-Method-Override:DELETE"
    })
    @POST("/api" + URL_VERSION + "/oAuth/token")
    Call<ResponseBody> logoutUser(@Header("Authorization") String token, @Body Object body);

    /**
     * Login Service
     **/
    @POST("/api" + URL_VERSION + "/sso/login")
    KoreLoginResponse loginSSoUser(@Body HashMap<String, Object> userCredentials);

//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType")
//    Call<MeetingTypeModel> getMeetingTypes(@Header("Authorization") String token, @Path("userId") String userId);
//
//    @POST("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/web")
//    Call<WebMeetingType> createWebMeetingType(@Header("Authorization") String token, @Path("userId") String userId, @Body WebMeetingType webMeetingType);
//
//    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/web/{typeId}")
//    Call<WebMeetingType> editWebMeetingType(@Header("Authorization") String token, @Path("userId") String userId, @Path("typeId") String typeId, @Body WebMeetingType webMeetingType);
//
//    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/web/{typeId}")
//    Call<WebMeetingType> deleteWebMeetingType(@Header("Authorization") String token, @Path("userId") String userId, @Path("typeId") String typeId);


    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/defaultWeb")
    Call<KaRestResponse> setAsDefaultWeb(@Header("Authorization") String token, @Path("userId") String userId, @Body HashMap<String, String> webMeetingType);

    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/defaultPhone")
    Call<KaRestResponse> setAsDefaultPhone(@Header("Authorization") String token, @Path("userId") String userId, @Body HashMap<String, String> phoneMeetingType);

    //{{host}}ka/users/{{userId}}/profile/meetingType/default?rnd=n2yhrp
    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/default")
    Call<KaRestResponse> setAsDefaultMeetingType(@Header("Authorization") String token, @Path("userId") String userId, @Body HashMap<String, String> phoneMeetingType);

//    @POST("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/phone")
//    Call<PhoneMeetingType> createPhoneMeetingType(@Header("Authorization") String token, @Path("userId") String userId, @Body PhoneMeetingType phoneMeetingType);
//
//    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/phone/{typeId}")
//    Call<PhoneMeetingType> editPhoneMeetingType(@Header("Authorization") String token, @Path("userId") String userId, @Path("typeId") String typeId, @Body PhoneMeetingType webMeetingType);
//
//    @DELETE("/api" + URL_VERSION + "/ka/users/{userId}/profile/meetingType/phone/{typeId}")
//    Call<PhoneMeetingType> deletePhoneMeetingType(@Header("Authorization") String token, @Path("userId") String userId, @Path("typeId") String typeId);

//    @GET("/downloads/videos/{fileName}")
//    Call<ResponseBody> downloadVideo(@Path("fileName") String fileName);

    @GET("/static/videos/{fileName}")
    Call<ResponseBody> downloadNewVideo(@Path("fileName") String fileName);

    @POST("/api" + URL_VERSION + "/presence/start")
    Call<KoreSToken> getSToken(@Header("Authorization") String token, @Body Object body);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/kora/phrases?help=true")
    Call<KoraHelpModel> getFeatureUtterances(@Path("userId") String userId, @Header("Authorization") String token);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/skills/triggerphrase")
    Call<ArrayList<SkillsListResponse>> getSkills(@Path("userId") String userId, @Header("Authorization") String token);


    @GET("/api" + URL_VERSION + "/ka/conf/users/{userId}/kora/phrases")
    Call<FeatureUtteranceModel.FeatureModel> getFeatureUtterancesQuery(@Path("userId") String userId, @Query("q") String query);

    @GET
    public Call<TaskTemplateResponse> getTasks(@Url String url, @Query("offSet") int offSet, @Query("limit") int limit, @Header("authorization") String accessToken);

    @GET
    public Call<com.kore.ai.widgetsdk.restresponse.KaRestResponse.WTasksList> getWTasks(@Url String url, @Query("offSet") int offSet, @Query("limit") int limit, @Header("authorization") String accessToken);


//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/widgets")
//    Call<MainWidgetDataModel> getBaseWidgetData(@Path("userId") String userId, @Header("Authorization") String token, @Query("tz") String timeZone, @Query("lat") double latitude,
//                                                @Query("lon") double longitude);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/panels")
    Call<PanelResponseData> getPannelData(@Path("userId") String userId, @Header("Authorization") String token, @Query("tz") String timeZone);

    @GET("/widgetsdk/{clientId}/panels?resolveWidgets=true")
    Call<List<PanelResponseData.Panel>> getWidgetPannelData(@Path("clientId") String clientId, @Header("Authorization") String token,  @Query("from") String identity);

//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/widgets")
//    Call<MainWidgetDataModel> getBaseWidgetDataWithoutLoc(@Path("userId") String userId, @Header("Authorization") String token, @Query("tz") String timeZone);

    @GET
    Call<com.kore.ai.widgetsdk.models.Header> getHeaderData(@Url String url, @Header("Authorization") String token);

    @GET
    Call<PanelWidgetsDataModel> getPanelWidgetsData(@Url String url, @Header("Authorization") String token);

    @POST
    Call<WUpcomingTasksModel> getTasksAssignedToYou(@Url String url, @Header("Authorization") String token, @QueryMap(encoded=true) Map<String, Object> param, @Body Object body);

    @GET
    Call<WUpcomingTasksModel> getTasksAssignedByYou(@Url String url, @Header("Authorization") String token);

    @GET
    Call<WUpcomingTasksModel> getOverDueTasksAssignedToYou(@Url String url, @Header("Authorization") String token);

    @GET
    Call<com.kore.ai.widgetsdk.models.Header> getHeaderDataWithLoc(@Url String url, @Header("Authorization") String token, @Query("lat") double latitude,
                                      @Query("lon") double longitude);

    @POST
    Call<SummaryViewResponseModel> getSummaryView(@Url String url, @Header("Authorization") String token, @Body Object body);

    @POST
    Call<WUpcomingMeetingModel> getUpcomingMeetingsData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded=true) Map<String, Object> param, @Body Object body);
//    @POST
//    Call<WUpcomingMeetingModel> getUpcomingMeetingsData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);

    @POST
    Call<Widget> getDefaultServiceData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);

    @POST
    Call<WidgetsWidgetModel> getDefaultWidgetServiceData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param);

    @POST
    Call<WidgetsDataModel> getChartData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param);

    @POST
    Call<WidgetsDataModel> getWidgetChartData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param);

    @POST
    Call<WidgetListDataModel> getListWidgetData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param);

    @POST
    Call<WidgetTableListDataModel> getTableListWidgetData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param);

    @POST
    Call<WeatherWidgetModel> getWeatherServiceData(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);

//    @POST
//    Call<WUpcomingTasksModel> getTasksAssignedToYou(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);
//
//    @GET
//    Call<WUpcomingTasksModel> getTasksAssignedByYou(@Url String url, @Header("Authorization") String token);
//
//    @GET
//    Call<WUpcomingTasksModel> getOverDueTasksAssignedToYou(@Url String url, @Header("Authorization") String token);
//
    @GET
    Call<WUpcomingTasksModel> getOverDueTasksAssignedByYou(@Url String url, @Header("Authorization") String token);
//
    @POST
    Call<WUpcomingFilesModel> getFilesCreatedByYou(@Url String url, @Header("Authorization") String token, @QueryMap(encoded = true) Map<String, Object> param, @Body Object body);

    @GET
    Call<ResponseBody> getFilesSharedWithYou(@Url String url, @Header("Authorization") String token);

    //   /ka/users/:userId/widgets/layout
    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/panels/{panelId}/layout")
    Call<ResponseBody> reorderWidgets(@Path("userId") String userId, @Path("panelId") String panelId, @Header("Authorization") String token, @Body Map<String, List<String>> body);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/task/{tId}")
    Call<TaskTemplateModel> getTask(@Header("Authorization") String token, @Path("userId") String userId, @Path("tId") String tId);


    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/task")
    Call<ResponseBody> getTaskCompletion(@Header("Authorization") String token, @Path("userId") String userId, @Body TaskCompletionModel taskCompletionModel);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/subdomain/suggestion")
    Call<EmailSuggestionModel> getSuggestionForEmail(@Header("Authorization") String token, @Path("userId") String userId);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/subdomain/availability")
    Call<EmailAvailablityResponseModel> getEmailAvailablityCheck(@Header("Authorization") String token, @Path("userId") String userId, @Query("subdomain") String subdomain);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/subdomain")
    Call<GetDomainInfoModel> getExistingEmailActionDetail(@Header("Authorization") String token, @Path("userId") String userId);


    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/subdomain")
    Call<CreateEmailActionResponse> createEmailAction(@Header("Authorization") String token, @Path("userId") String userId, @Body CreateEmailActionRequest body);



    @POST("/api" + URL_VERSION + "/ka/users/{userId}/calendar/updateEvent")
    Call<MeetinngActionResponse> meetingYesNoMayBe(@Header("Authorization") String token, @Path("userId") String userId, @Body MeetingAttendingRequestModel requestModel);


   /* @GET("/api" + URL_VERSION + "/ka/users/{userId}/calendar/{eId}")
    Call<List<CalEventsTemplateModel>> getMeetingDetail(@Header("Authorization") String token, @Path("userId") String userId, @Path("eId") String eId);*/

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/calendar/getSpecificEvent")
    Call<List<CalEventsTemplateModel>> getMeetingDetail(@Header("Authorization") String token, @Path("userId") String userId, @Body HashMap<String, Object> eventDetails);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/skills/recent ")
    Call<RecentSkills> getRecentSkills(@Header("Authorization") String token, @Path("userId") String userId);

    @POST("/api" + URL_VERSION + "/ka/users/{userId}/devicecontacts")
    Call<ResponseBody> postDeviceContacts(@Header("Authorization") String token, @Path("userId") String userId, @Query("deviceId") String deviceId, @Body List<DeviceContactsModel> deviceContactsList);

    @GET("/api" + URL_VERSION + "/ka/users/{userId}/devicecontacts")
    Call<DeviceContactsTimeStampResponse> getDeviceContactsTimeStamp(@Header("Authorization") String token, @Path("userId") String userId, @Query("deviceId") String deviceId);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/meetings/{mId}/notes/draft")
    Call<MeetingDraftResponse> getMeetingNotesDraft(@Header("Authorization") String token, @Path("userId") String userId, @Path("mId") String mId, @Query("eId") String eId);


    @POST("/api" + URL_VERSION + "/ka/users/{userId}/meetings/{mId}/notes")
    Call<CreateMeetingNotesResponse> createMeeting(@Header("Authorization") String token, @Path("userId") String userId, @Path("mId") String mId, @Body KnowledgeDetailModel jsonObject);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/meetings/{mId}/notes")
    Call<ArrayList<MeetingDetailModel>> getMeetingNotesTitles(@Header("Authorization") String token, @Path("userId") String userId, @Path("mId") String mId);


    @GET("/api" + URL_VERSION + "/ka/users/{userId}/meetings/{mId}/notes/participants")
    Call<ResponseBody> getParticipants(@Header("Authorization") String token, @Path("userId") String userId, @Path("mId") String mId);


//    @POST
//    Call<SummaryViewResponseModel> getSummaryView(@Url String url, @Header("Authorization") String token, @Body Object body);


    @GET("/api" + URL_VERSION + "/ka/conf/users/{userId}/feedback/meta")
    Call<FeedbackDataResponse> getFeedbackItemLIst(@Header("Authorization") String token, @Path("userId") String userId);

    // /ka/users/{{userId}}/profile
//    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/profile")
//    Call<ResponseBody> setUserProfileDetails(@Path("userId") String userId, @Header("Authorization") String token, @Body KaUserProfileModel body);

//   // ka/users/u-30db838f-d7f2-5431-857a-9e61ca1614e1/skills?enabled=true&type=search&authcheck=true
//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/skills/?enabled=true&type=search&authcheck=true")
//    Call<SkillSearchResponse> getSearchSkillsListEnabled(@Header("Authorization") String token, @Path("userId") String userId);
//
//
//
//    @GET("/api" + URL_VERSION + "/ka/users/{userId}/skills/?enabled=false&type=search&authcheck=true")
//    Call<SkillSearchResponse> getSearchSkillsListDisabled(@Header("Authorization") String token, @Path("userId") String userId);
//
//
//
//    ///ka/users/:userId/skills/:skillId/actions/enable
//    @POST("/api" + URL_VERSION + "/ka/users/{userId}/skills/{skillId}/actions/{action}/?authcheck=true")
//    Call<SearchSkillActionResponse> switchSkillAction(@Header("Authorization") String token, @Path("userId") String userId,
//                                                      @Path("skillId") String skillId, @Path("action") String action);




    @PUT("/api" + URL_VERSION + "/ka/users/{userId}/layout")
    Call<ResponseBody> pinUnPinAction(@Header("Authorization") String token, @Path("userId") String userId, @Body HashMap<String, Object> body);




}



