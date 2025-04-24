package kore.botssdk.net;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.models.Authorization;
import kore.botssdk.models.BotInfoModel;
import kore.botssdk.models.BotUserInfo;
import kore.botssdk.models.User;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressWarnings({"UnknownNullness"})
public class RestResponse {
    public static class LoginResponse extends User {
        public String status;
    }

    public static class JWTTokenResponse {
        private String jwt;

        /**
         * @return The jwt
         */
        public String getJwt() {
            return jwt;
        }

        /**
         * @param jwt The jwt
         */
        public void setJwt(String jwt) {
            this.jwt = jwt;
        }
    }

    public static class BotAuthorization {
        private Authorization authorization;
        private BotUserInfo userInfo;

        /**
         * @return The authorization
         */
        public Authorization getAuthorization() {
            return authorization;
        }

        /**
         * @param authorization The authorization
         */
        public void setAuthorization(Authorization authorization) {
            this.authorization = authorization;
        }

        /**
         * @return The userInfo
         */
        public BotUserInfo getUserInfo() {
            return userInfo;
        }

        /**
         * @param userInfo The userInfo
         */
        public void setUserInfo(BotUserInfo userInfo) {
            this.userInfo = userInfo;
        }

    }

    public static class RTMUrl {
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
        private String renderMsg;
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
        private String type;

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public BotMessage(String body, String renderMsg) {
            this.body = body;
            this.renderMsg = renderMsg == null || renderMsg.isEmpty() ? null : renderMsg;
        }

        public BotMessage(String body, ArrayList<HashMap<String, String>> attachments) {
            this.body = body;
            this.attachments = attachments;
        }

        public BotMessage(String body, String type, String renderMsg) {
            this.renderMsg = renderMsg == null || renderMsg.isEmpty() ? null : renderMsg;
            this.body = body;
            this.type = type;
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

        public String getRenderMsg() {
            return renderMsg;
        }

        public void setRenderMsg(String renderMsg) {
            this.renderMsg = renderMsg;
        }

        public BotCustomData getCustomData() {
            return customData;
        }

        public void setCustomData(BotCustomData customData) {
            this.customData = customData;
        }
    }

    public static class BotCustomData extends HashMap<String, Object> {

    }

    public static class Meta {
        public final String timezone;
        public final String locale;

        public Meta(String timezone, String locale) {
            this.timezone = timezone;
            this.locale = locale;
        }
    }

    public static class BotPayLoad {
        private BotMessage message;
        private String resourceid = "/bot.message";
        private BotInfoModel botInfo;
        private double clientMessageId = System.currentTimeMillis();
        private Meta meta;
        private Object id = clientMessageId;
        private String client = "Android";
        private String event;

        public void setEvent(String event) {
            this.event = event;
        }

        public String getEvent() {
            return event;
        }

        public void setMsgId(String msgId) {
            this.id = msgId;
        }

        public void setMessage(BotMessage message) {
            this.message = message;
        }

        public void setBotInfo(BotInfoModel botInfo) {
            this.botInfo = botInfo;
        }

        public double getClientMessageId() {
            return clientMessageId;
        }

        public void setClientMessageId(long clientMessageId) {
            this.clientMessageId = clientMessageId;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
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

        public void setResourceid(String resourceid) {
            this.resourceid = resourceid;
        }

        public BotInfoModel getBotInfo() {
            return botInfo;
        }
    }

}
