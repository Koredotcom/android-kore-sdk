package kore.botssdk.net;


import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.models.Authorization;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotUserInfo;
import kore.botssdk.models.User;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressWarnings("serial")
public class RestResponse {

    public class LoginResponse extends User {
        public String status;
    }
    public class JWTTokenResponse{
        private String jwt;

        /**
         * @return
         * The jwt
         */
        public String getJwt() {
            return jwt;
        }
        /**
         * @param jwt
         * The jwt
         */
        public void setJwt(String jwt) {
            this.jwt = jwt;
        }
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
        private BotCustomData customData;
        public HashMap<String, Object> getParams() {
            return params;
        }

        public void setParams(HashMap<String, Object> params) {
            this.params = params;
        }

        private HashMap<String, Object> params;
        private ArrayList<HashMap<String, String>> attachments = new ArrayList<>();

        public BotMessage(String body) {
            this.body = body;
        }

        public BotMessage(String body, ArrayList<HashMap<String, String>> attachments) {
            this.body = body;
            this.attachments = attachments;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setAttachments(ArrayList<HashMap<String, String>> attachments) {
            this.attachments = attachments;
        }

        public String getBody() {
            return body;
        }

        public BotCustomData getCustomData() {
            return customData;
        }

        public void setCustomData(BotCustomData customData) {
            this.customData = customData;
        }
    }

    public static class BotCustomData extends HashMap<String,Object>{

    }

    public static class BotResponses extends ArrayList<BotResponse>{}

    public static class Meta{
        public final String timezone;
        public final String locale;
        public Meta(String timezone, String locale){
            this.timezone = timezone;
            this.locale = locale;
        }
    }

    public static class BotPayLoad {
        private BotMessage message;
        private final String resourceid = "/bot.message";
        private BotInfoModel botInfo;
        private int clientMessageId = (int)System.currentTimeMillis();
        private Meta meta;
        private int id = clientMessageId;
        private String client = "Android";

        public void setMessage(BotMessage message) {
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

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
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
        public BotMessage getMessage() {
            return message;
        }

        public String getResourceid() {
            return resourceid;
        }

        public BotInfoModel getBotInfo() {
            return botInfo;
        }
    }

}
