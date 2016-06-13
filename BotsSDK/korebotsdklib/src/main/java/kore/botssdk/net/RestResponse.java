package kore.botssdk.net;


import java.util.ArrayList;
import java.util.Objects;

import kore.botssdk.models.Authorization;
import kore.botssdk.models.BotInfoModel;
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

    public static class BotPayLoad {
        private BotMessage message;
        private String resourceid = "/bot.message";
        private BotInfoModel botInfo;
        private int id = 1;

        public void setMessage(BotMessage message) {
            this.message = message;
        }
    }

}
