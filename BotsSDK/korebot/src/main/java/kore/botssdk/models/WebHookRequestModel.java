package kore.botssdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebHookRequestModel
{
    public Session session;
    public Message message;
    public From from;
    public To to;
    public Token token;

    public From getFrom() {
        return from;
    }

    public Message getMessage() {
        return message;
    }

    public Session getSession() {
        return session;
    }

    public To getTo() {
        return to;
    }

    public Token getToken() {
        return token;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public static class Session
    {
        @SerializedName("new")
        @Expose
        private boolean newSession;


        public boolean getNewSession() {
        return newSession;
        }

        public void setNewSession(boolean newSession) {
        this.newSession = newSession;
        }
    }

    public static class Message
    {
        private String type;
        private String val;

        public String getVal() {
            return val;
        }

        public String getType() {
            return type;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public static class From
    {
        public String id;
        public WebHookUserInfo userInfo;

        public void setId(String id) {
            this.id = id;
        }

        public void setUserInfo(WebHookUserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public String getId() {
            return id;
        }

        public WebHookUserInfo getUserInfo() {
            return userInfo;
        }

        public static class WebHookUserInfo
        {
            private String firstName;
            private String lastName;
            private String email;

            public String getEmail() {
                return email;
            }

            public String getFirstName() {
                return firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }
        }
    }

    public static class To
    {
        private String id;
        private GroupInfo groupInfo;

        public String getId() {
            return id;
        }

        public GroupInfo getGroupInfo() {
            return groupInfo;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setGroupInfo(GroupInfo groupInfo) {
            this.groupInfo = groupInfo;
        }

        public static class GroupInfo
        {
            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }

    public static class Token
    {

    }
}

