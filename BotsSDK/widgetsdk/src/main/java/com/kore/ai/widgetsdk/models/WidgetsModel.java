package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kore.ai.widgetsdk.net.SDKConfiguration;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.kore.ai.widgetsdk.net.SDKConfiguration.Client.identity;

public class WidgetsModel implements Serializable,Cloneable {

    @Override
    public WidgetsModel clone() throws CloneNotSupportedException {
        WidgetsModel widget = (WidgetsModel) super.clone();
        return widget;
    }
    public LoginModel getLogin() {
        return login;
    }

    public void setLogin(LoginModel login) {
        this.login = login;
    }

    @SerializedName("login")
    @Expose
    private LoginModel login;

    private String id;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String refId;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    private int __v;

    public int get__v() {
        return __v;
    }

    public void setRefId(int __v) {
        this.__v = __v;
    }

    private String adminTaskStatus;

    public String getAdminTaskStatus() {
        return adminTaskStatus;
    }

    public void setAdminTaskStatus(String adminTaskStatus) {
        this.adminTaskStatus = adminTaskStatus;
    }

    private List<String> approvalRequestedLanguages;

    public List<String> getApprovalRequestedLanguages() {
        return approvalRequestedLanguages;
    }

    public void setAdminTaskStatus(List<String> approvalRequestedLanguages) {
        this.approvalRequestedLanguages = approvalRequestedLanguages;
    }

    private List<String> approvedLanguages;

    public List<String> getApprovedLanguages() {
        return approvedLanguages;
    }

    public void setApprovedLanguages(List<String> approvedLanguages) {
        this.approvedLanguages = approvedLanguages;
    }

    private List<String> authProfiles;

    public List<String> getAuthProfiles() {
        return authProfiles;
    }

    public void setAuthProfiles(List<String> authProfiles) {
        this.authProfiles = authProfiles;
    }

    private AutoRefresh autoRefresh;

    public AutoRefresh getAutoRefresh(){ return autoRefresh;}

    public void setAutoRefresh(AutoRefresh autoRefresh){ this.autoRefresh = autoRefresh;}

    private Param param;

    public Param getParam(){ return param = new Param();}

    public void setParam(Param param){ this.param = param;}

    private int cacheInterval;

    public int getCacheInterval() {
        return cacheInterval;
    }

    public void setCacheInterval(int cacheInterval) {
        this.cacheInterval = cacheInterval;
    }

    private String contextMap;

    public String getContextMap() {
        return contextMap;
    }

    public void setContextMap(String contextMap) {
        this.contextMap = contextMap;
    }

    private String createdBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    private String createdOn;

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

//    private List<String> fields;

//    public List<String> getFields() {
//        return fields;
//    }
//
//    public void setFields(List<String> fields) {
//        this.fields = fields;
//    }

    private String lMod;

    public String getlMod() {
        return lMod;
    }

    public void setlMod(String lMod) {
        this.lMod = lMod;
    }

    private String lModBy;

    public String getlModBy() {
        return lModBy;
    }

    public void setlModBy(String lModBy) {
        this.lModBy = lModBy;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Source source;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String name) {
        this.state = name;
    }

    private String streamId;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    private String templateType;

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    private Visibility visibility;

    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String callbackURL;

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }


    public static class AutoRefresh implements Serializable{

        private boolean enabled;
        private int interval;

        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

    }

    public static class Source implements Serializable{

        private String type;
        private String resource;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

    }

    public static class Param implements Serializable
    {
        private String from = identity;
//        private String from = UUID.randomUUID().toString();
        private Object inputs = new Object();

        public String getFrom(){ return from;}

        public void setFrom(String from)
        {
            this.from = from;
        }

        public Object getInputs()
        {
            return inputs;
        }

        public void setInputs(Object inputs)
        {
            this.inputs = inputs;
        }
    }

    public static class Visibility implements Serializable{

        private String namespace;
        private String[] namespaceIds;

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String[] getNamespaceIds() {
            return namespaceIds;
        }

        public void setNamespaceIds(String[] namespaceIds) {
            this.namespaceIds = namespaceIds;
        }

    }
}
