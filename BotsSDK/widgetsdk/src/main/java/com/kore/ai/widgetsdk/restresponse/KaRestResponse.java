package com.kore.ai.widgetsdk.restresponse;

import com.kore.ai.widgetsdk.models.Authorization;
import com.kore.ai.widgetsdk.models.BotInfoModel;
import com.kore.ai.widgetsdk.models.BotUserInfo;
import com.kore.ai.widgetsdk.models.KnowledgeDetailModel;
import com.kore.ai.widgetsdk.models.MeetingRequestModel;
import com.kore.ai.widgetsdk.models.User;
import com.kore.ai.widgetsdk.models.WTaskTemplateModel;
//import com.kore.korelib.koradbmodels.KoraTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@SuppressWarnings("serial")
public class KaRestResponse {

    public class LoginResponse extends User {
        public String status;
    }
    public class JWTKeyResponse{
        private String bearer;

        public String getBearer(){
            return bearer;
        }
    }
    /*public class JWTTokenResponse{
        private String jwt;
        private String streamId;
        private String botName;
        private String botsUrl;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        private String taskId;

        public String getStreamId() {
            return streamId;
        }

        public void setStreamId(String streamId) {
            this.streamId = streamId;
        }

        public String getBotName() {
            return botName;
        }

        public void setBotName(String botName) {
            this.botName = botName;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        private String clientId;
        private String clientSecret;

        *//**
         * @return
         * The jwt
         *//*
        public String getJwt() {
            return jwt;
        }
        *//**
         * @param jwt
         * The jwt
         *//*
        public void setJwt(String jwt) {
            this.jwt = jwt;
        }

        public String getBotsUrl() {
            return botsUrl;
        }

        public void setBotsUrl(String botsUrl) {
            this.botsUrl = botsUrl;
        }
    }*/

    public static class URLUnfurlResponse {

        private String title;
        private String description;
        private String source;
        private String url;
        private String site;
        private String type;
        private String image;
        private String video;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

    }
    public class KoreStringErrorResponse {
        public StringErrorList errors;
    }

    public static class StringErrorList extends ArrayList<KoreStringError> {
    }
    public class KoreStringError {
        public String msg;
        public String code;
    }
    public class BotAuthorization {
        private Authorization authorization;
        private BotUserInfo userInfo;

        /**
         * @return
         * The authorization
         */
        public Authorization getAuthorization() {
            return authorization;
        }

        /**
         * @param authorization
         * The authorization
         */
        public void setAuthorization(Authorization authorization) {
            this.authorization = authorization;
        }

        /**
         * @return
         * The userInfo
         */
        public BotUserInfo getUserInfo() {
            return userInfo;
        }

        /**
         * @param userInfo
         * The userInfo
         */
        public void setUserInfo(BotUserInfo userInfo) {
            this.userInfo = userInfo;
        }

    }
    public class RTMUrl {
        private String url;

        /**
         * @return The url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url The url
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class BotMessage {
        private String body;
        private ArrayList<Objects> attachments = new ArrayList<>();

        public BotMessage(String body) {
            this.body = body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setAttachments(ArrayList<Objects> attachments) {
            this.attachments = attachments;
        }

        public String getBody() {
            return body;
        }
    }

    public static class Meta{
        public String timezone;
        public String locale;
        public Meta(String timezone, String locale){
            this.timezone = timezone;
            this.locale = locale;
        }
    }

    public static class BotPayLoad {


        private KaRestResponse.BotMessage message;
        private String resourceid = "/bot.message";
        private BotInfoModel botInfo;
        private int clientMessageId = (int)System.currentTimeMillis();
        private KaRestResponse.Meta meta;
        private int id = clientMessageId;
        private String client = "sdk";

        public void setMessage(KaRestResponse.BotMessage message) {
            this.message = message;
        }
        public void setBotInfo(BotInfoModel botInfo){
            this.botInfo = botInfo;
        }

        public int getClientMessageId() {
            return clientMessageId;
        }

        public void setClientMessageId(int clientMessageId) {
            this.clientMessageId = clientMessageId;
        }

        public KaRestResponse.Meta getMeta() {
            return meta;
        }

        public void setMeta(KaRestResponse.Meta meta) {
            this.meta = meta;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }
        public KaRestResponse.BotMessage getMessage() {
            return message;
        }

        public String getResourceid() {
            return resourceid;
        }

        public BotInfoModel getBotInfo() {
            return botInfo;
        }
    }

    public static class KnowledgeInfo extends HashMap {

    }
    public static class DeleteResource{
        private int commentsCount;

        public int getCommentsCount() {
            return commentsCount;
        }
    }
    /*public static class RecentKnowledgeResponse extends ArrayList<CreateKnowledgeModel> {

    }*/
    public static class AutoSuggestionsResponse extends ArrayList<String> {

    }
    public static class VoteResponse {
        private ArrayList<KnowledgeDetailModel.VoteModel> votes;
        private int upVoteCount;

        public ArrayList<KnowledgeDetailModel.VoteModel> getVotes() {
            return votes;
        }

        public void setVotes(ArrayList<KnowledgeDetailModel.VoteModel> votes) {
            this.votes = votes;
        }

        public int getUpVoteCount() {
            return upVoteCount;
        }

        public void setUpVoteCount(int upVoteCount) {
            this.upVoteCount = upVoteCount;
        }

        public int getDownVoteCount() {
            return downVoteCount;
        }

        public void setDownVoteCount(int downVoteCount) {
            this.downVoteCount = downVoteCount;
        }

        private int downVoteCount;

    }

    public static class FollowResponse {
        public ArrayList<String> getFollowers() {
            return followers;
        }

        public void setFollowers(ArrayList<String> followers) {
            this.followers = followers;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        private ArrayList<String> followers;
        private int followCount;
    }

    public static class CommentResponse{
        public ArrayList<String> getFollowers() {
            return followers;
        }

        public void setFollowers(ArrayList<String> followers) {
            this.followers = followers;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        private ArrayList<String> followers;
        private int followCount;
        private KnowledgeDetailModel.CommentModel comment;

        public KnowledgeDetailModel.CommentModel getComment() {
            return comment;
        }

        public void setComment(KnowledgeDetailModel.CommentModel comment) {
            this.comment = comment;
        }

        public KnowledgeDetailModel.MyActions getMyActions() {
            return myActions;
        }

        public void setMyActions(KnowledgeDetailModel.MyActions myActions) {
            this.myActions = myActions;
        }

        private KnowledgeDetailModel.MyActions myActions;
    }

    public static class PaginateInfoDetails {
        int commentsCount;

        public ArrayList<KnowledgeDetailModel.CommentModel> getComments() {
            return comments;
        }

        public void setComments(ArrayList<KnowledgeDetailModel.CommentModel> comments) {
            this.comments = comments;
        }

        ArrayList<KnowledgeDetailModel.CommentModel> comments;

        public int getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(int commentsCount) {
            this.commentsCount = commentsCount;
        }



        public ArrayList<KnowledgeDetailModel.VoteModel> getVotes() {
            return votes;
        }

        public void setVotes(ArrayList<KnowledgeDetailModel.VoteModel> votes) {
            this.votes = votes;
        }

        public int getUpVoteCount() {
            return upVoteCount;
        }

        public void setUpVoteCount(int upVoteCount) {
            this.upVoteCount = upVoteCount;
        }

        public int getDownVoteCount() {
            return downVoteCount;
        }

        public void setDownVoteCount(int downVoteCount) {
            this.downVoteCount = downVoteCount;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public ArrayList<String> getFollowers() {
            return followers;
        }

        public void setFollowers(ArrayList<String> followers) {
            this.followers = followers;
        }

        ArrayList<KnowledgeDetailModel.VoteModel> votes;
        int upVoteCount;
        int downVoteCount;
        int followCount;
        ArrayList<String> followers;
    }

    public static class UserContext{
        private Org org;

        public Org getOrg() {
            return org;
        }

        public void setOrg(Org org) {
            this.org = org;
        }
    }

    public static class Org{
        private ContactsInfo contacts;

        public ContactsInfo getContacts() {
            return contacts;
        }

        public void setContacts(ContactsInfo contacts) {
            this.contacts = contacts;
        }
    }
    public static class ContactsInfo{
        private long lastModTime;

        public long getLastModTime() {
            return lastModTime;
        }

        public void setLastModTime(long lastModTime) {
            this.lastModTime = lastModTime;
        }
    }

   /* public static class ShareResponse {
        private int sharesCount;

        public int getnShares() {
            return sharesCount;
        }

        public void setnShares(int sharesCount) {
            this.sharesCount = sharesCount;
        }

        public ArrayList<SharedList> getSharedList() {
            return sharedList;
        }

        public void setSharedList(ArrayList<SharedList> sharedList) {
            this.sharedList = sharedList;
        }

        private ArrayList<SharedList> sharedList;
    }*/

    /*public static class SharedList extends ContactsTeamsHolder {
        int privilege;

        public int getPrivilege() {
            return privilege;
        }

        public void setPrivilege(int privilege) {
            this.privilege = privilege;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        String type;

        public boolean isPending() {
            return isPending;
        }

        public void setPending(boolean pending) {
            isPending = pending;
        }

        boolean isPending;
    }*/
    public static class QuestionsResponse extends ArrayList<QuestionModel> {

    }
    public static class QuestionModel{
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            question = question;
        }

        String id;
        String question;
    }

    public static class KaSignedMediaUrlResponse {
        private String mediaUrl;
        private String filename;

        /**
         * @return the mediaUrl
         */
        public String getMediaUrl() {
            return mediaUrl;
        }

        /**
         * @param mediaUrl the mediaUrl to set
         */
        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public String getFilename() {
            return filename;
        }
    }
    public class KaCheckOnBoardingResponse {
        public boolean isOnboarded() {
            return isOnboarded;
        }

        public void setIsOnboarded(boolean isOnboarded) {
            this.isOnboarded = isOnboarded;
        }

        private boolean isOnboarded;
        private boolean isTeachInitiated;

        public boolean isTeachInitiated() {
            return isTeachInitiated;
        }

        public void setTeachInitiated(boolean teachInitiated) {
            isTeachInitiated = teachInitiated;
        }
    }
    public class KoreError {
        public String msg;
        public int code;
        public MoreInfo moreInfo;
    }

    public class MoreInfo {
        public int numberOfAttempts;
        public int maxAttemptsAllowed;
        public long forDuration = 86400000;
    }
    public static class ErrorList extends ArrayList<KoreError> {
    }
    public class KoreErrorResponse {
        public ErrorList errors;

        public String getFirstMessage() {
            if (errors != null && errors.size() > 0) {
                KoreError err = errors.get(0);
                return err.msg;
            }

            return null;
        }

        public int getFirstErrorCode() {
            if (errors != null && errors.size() > 0) {
                KoreError err = errors.get(0);
                return err.code;
            }
            return 0;
        }
    }
    public class MoreInfoForAppControl{
        private String userId;
        private String firstName;
        private String lastName;
        private String emailId;

        /**
         *
         * @return
         * The userId
         */
        public String getUserId() {
            return userId;
        }

        /**
         *
         * @param userId
         * The userId
         */
        public void setUserId(String userId) {
            this.userId = userId;
        }

        /**
         *
         * @return
         * The firstName
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         *
         * @param firstName
         * The firstName
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         *
         * @return
         * The lastName
         */
        public String getLastName() {
            return lastName;
        }

        /**
         *
         * @param lastName
         * The lastName
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        /**
         *
         * @return
         * The emailId
         */
        public String getEmailId() {
            return emailId;
        }

        /**
         *
         * @param emailId
         * The emailId
         */
        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

    }
    public class KoreErrorForAppControl{
        private String msg;
        private String code;
        private List<MoreInfoForAppControl> moreInfo = new ArrayList<MoreInfoForAppControl>();

        /**
         *
         * @return
         * The msg
         */
        public String getMsg() {
            return msg;
        }

        /**
         *
         * @param msg
         * The msg
         */
        public void setMsg(String msg) {
            this.msg = msg;
        }

        /**
         *
         * @return
         * The code
         */
        public String getCode() {
            return code;
        }

        /**
         *
         * @param code
         * The code
         */
        public void setCode(String code) {
            this.code = code;
        }

        /**
         *
         * @return
         * The moreInfo
         */
        public List<MoreInfoForAppControl> getMoreInfo() {
            return moreInfo;
        }

        /**
         *
         * @param moreInfo
         * The moreInfo
         */
        public void setMoreInfo(List<MoreInfoForAppControl> moreInfo) {
            this.moreInfo = moreInfo;
        }

    }
    public static class ErrorListForAppControl extends ArrayList<KoreErrorForAppControl>{

    }
    public class KoreErrorRespForAC{
        public ErrorListForAppControl errors;
    }
    public class KoreErrorCodeString {
        public String msg;
        public String code;
        public String location;
        public MoreInfo moreInfo;
    }
    public static class ErrorListString extends ArrayList<KoreErrorCodeString> {
    }

    public class KoreErrorResponseString {
        public ErrorListString errors;

        public String getFirstMessage() {
            if (errors != null && errors.size() > 0) {
                KoreErrorCodeString err = errors.get(0);
                return err.msg;
            }
            return null;
        }

        public String getFirstErrorCode() {
            if (errors != null && errors.size() > 0) {
                KoreErrorCodeString err = errors.get(0);
                return err.code;
            }
            return null;
        }
    }

    /*public class ColorResponse {
        public List<ColorModel> spaceColors;
    }*/



//    public static class KoraTeams extends ArrayList<KoraTeam>{}

    /*public static class ContactList extends ArrayList<ContactsTeamsHolder> {
    }*/



    public static class MeetingsList extends ArrayList<MeetingRequestModel>{

    }

    public static class WTasksList {
        public ArrayList<WTaskTemplateModel> getElements() {
            return elements;
        }

        public void setElements(ArrayList<WTaskTemplateModel> elements) {
            this.elements = elements;
        }

        ArrayList<WTaskTemplateModel> elements;
    }


}
