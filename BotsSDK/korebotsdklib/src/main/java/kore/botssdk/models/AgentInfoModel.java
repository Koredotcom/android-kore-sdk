package kore.botssdk.models;

import java.util.ArrayList;

public class AgentInfoModel
{
    private String type;
    private String from;
    private Message message;
    private String customEvent;

    public String getCustomEvent() {
        return customEvent;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public Message getMessage() {
        return message;
    }

    public class Message
    {
        private String type;
        private String event;
        private boolean fromAgent;
        private String timestamp;
        private AgentInfo agentInfo;

        public AgentInfo getAgentInfo() {
            return agentInfo;
        }

        public void setAgentInfo(AgentInfo agentInfo) {
            this.agentInfo = agentInfo;
        }

        public void setFromAgent(boolean fromAgent) {
            this.fromAgent = fromAgent;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isFromAgent() {
            return fromAgent;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getEvent() {
            return event;
        }

        public String getType() {
            return type;
        }
    }

    public class AgentInfo
    {
        private String status;
        private String customId;
        private String roleId;
        private String userId;
        private String accountId;
        private String orgId;
        private String id;
        private String emailId;
        private String firstName;
        private String lastName;
        private String nickName;
        private String name;
        private String phoneNumber;
        private String profImage;
        private String timestamp;
        private String startTimeStamp;
        private String agentId;
        private ArrayList<AgentGroups> agentGroups;

        public void setAgentGroups(ArrayList<AgentGroups> agentGroups) {
            this.agentGroups = agentGroups;
        }

        public ArrayList<AgentGroups> getAgentGroups() {
            return agentGroups;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getAgentId() {
            return agentId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getCustomId() {
            return customId;
        }

        public String getEmailId() {
            return emailId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getNickName() {
            return nickName;
        }

        public String getOrgId() {
            return orgId;
        }

        public String getProfImage() {
            return profImage;
        }

        public String getRoleId() {
            return roleId;
        }

        public String getStartTimeStamp() {
            return startTimeStamp;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public void setCustomId(String customId) {
            this.customId = customId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public void setProfImage(String profImage) {
            this.profImage = profImage;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public void setStartTimeStamp(String startTimeStamp) {
            this.startTimeStamp = startTimeStamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public class AgentGroups
    {
        private String groupId;
        private String role;
        private String name;

        public String getGroupId() {
            return groupId;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
