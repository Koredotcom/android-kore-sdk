package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kore.ai.widgetsdk.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 08-Mar-19.
 */

public class Widget implements Serializable,Cloneable {


    @Override
    public Widget clone() throws CloneNotSupportedException {
        Widget widget = (Widget) super.clone();
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

    public static class Action implements Serializable {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("payload")
        @Expose
        private String payload;
        @SerializedName("utterance")
        @Expose
        private String utterance;


        private String theme;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String url;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public String getUtterance() {
            return utterance;
        }

        public void setUtterance(String utterance) {
            this.utterance = utterance;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

    }

    private String id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String _id;

    public boolean isPinned() {
        return pinned;
    }

    public String pname;
    public String trigger;
    public String formLink;

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setFormLink(String formLink) {
        this.formLink = formLink;
    }

    public String getFormLink() {
        return formLink;
    }

    private boolean pinned;
    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String refreshInterval;
    private String cacheInterval;
    private String summary_placeholder;
    private String type;
    private String theme;

   // private String api;
    private String utterances_header;

    public String getTemplateType() {
        return templateType!=null?templateType:"default";
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;



    private List<MultiAction> multi_actions = null;

    @SerializedName("actions")
    @Expose
    private List<Action> actions = null;

    public List<Action> getUtterances() {
        return utterances;
    }

    public void setUtterances(List<Action> utterances) {
        this.utterances = utterances;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public List<MultiAction> getMultiActions() {
        return multi_actions;
    }

    public void setMultiActions(List<MultiAction> multi_actions) {
        this.multi_actions = multi_actions;
    }

    private List<Action> utterances = null;

    private List<Filter> filters = null;

    public String getId() {
        if(!StringUtils.isNullOrEmpty(id))
            return id;
        else
            return get_id();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRefreshInterval() {
        return refreshInterval;
    }
    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setRefreshInterval(String refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getCacheInterval() {
        return cacheInterval;
    }

    public void setCacheInterval(String cacheInterval) {
        this.cacheInterval = cacheInterval;
    }

    public String getSummaryPlaceholder() {
        return summary_placeholder;
    }

    public void setSummaryPlaceholder(String summary_placeholder) {
        this.summary_placeholder = summary_placeholder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
/*
    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }*/
    @SerializedName("hook")
    @Expose
    private Hook hook;

    public Hook getHook() {
        return hook;
    }

    public void setHook(Hook hook) {
        this.hook = hook;
    }
    @SerializedName("templateType")
    @Expose
    private String templateType;



    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }


    public static class Hook implements Serializable {

        @SerializedName("method")
        @Expose
        private String method;
        @SerializedName("api")
        @Expose
        private String api;

        @SerializedName("params")
        @Expose
        private Object params;
        @SerializedName("body")
        @Expose
        private Object body;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getApi() {
            return api;
        }

        public void setApi(String api) {
            this.api = api;
        }

        public Object getParams() {
            return params;
        }

        public void setParams(Object params) {
            this.params = params;
        }

        public Object getBody() {
            return body;
        }

        public void setBody(Object body) {
            this.body = body;
        }

    }

    /*public class Params implements Serializable{

        @SerializedName("q")
        @Expose
        private String q;

        public String getQ() {
            return q;
        }

        public void setQ(String q) {
            this.q = q;
        }

        @SerializedName("tz")
        @Expose
        private String tz;
        @SerializedName("filter")
        @Expose
        private String filter;
        @SerializedName("offSet")
        @Expose
        private String offSet;
        @SerializedName("limit")
        @Expose
        private String limit;

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        private String skillId;

        public String getTz() {
            return tz;
        }

        public void setTz(String tz) {
            this.tz = tz;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public String getOffSet() {
            return offSet;
        }

        public void setOffSet(String offSet) {
            this.offSet = offSet;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

    }*/

    public static class Tz implements Serializable {

        @SerializedName("default")
        @Expose
        private String _default;
        @SerializedName("type")
        @Expose
        private String type;

        public String getDefault() {
            return _default;
        }

        public void setDefault(String _default) {
            this._default = _default;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

   /* public class Body implements Serializable{

        @SerializedName("filter")
        @Expose
        private Filter filter;
        @SerializedName("tz")
        @Expose
        private Tz tz;

        public Filter getFilter() {
            return filter;
        }

        public void setFilter(Filter filter) {
            this.filter = filter;
        }

        public Tz getTz() {
            return tz;
        }

        public void setTz(Tz tz) {
            this.tz = tz;
        }

    }*/

    /*public class Element implements Serializable{
        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("subTitle")
        @Expose
        private String sub_title;

        @SerializedName("icon")
        @Expose
        private String icon;

        @SerializedName("filter")
        @Expose
        private Button filter;

    }

    public class Button extends MultiAction{
        private String theme;

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }
    }*/

    public static class Element implements Serializable{

        private String paceHolder;

        public String getPaceHolder() {
            return paceHolder;
        }

        public void setPaceHolder(String paceHolder) {
            this.paceHolder = paceHolder;
        }

        public String getIconId() {
            return iconId;
        }

        public void setIconId(String iconId) {
            this.iconId = iconId;
        }

        public String getPostback() {
            return postback;
        }

        public void setPostback(String postback) {
            this.postback = postback;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public Hook getHook() {
            return hook;
        }

        public void setHook(Hook hook) {
            this.hook = hook;
        }

        public DefaultAction getDefault_action() {
            return default_action;
        }

        public void setDefault_action(DefaultAction default_action) {
            this.default_action = default_action;
        }

        private String iconId;
        private String postback;
        private String _id;
        private Hook hook;
        private String acl;

        private String title;
        private String sub_title;

        public String getAcl() {
            return acl;
        }

        public void setAcl(String acl) {
            this.acl = acl;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        private String payload;
        private String icon;
        private List<Button> button = null;
        private String text;

        public String getTheme() {
            if(theme != null)
                return theme;
            else
                return "#4741fa";
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        private String theme;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;

        private List<Action> actions = null;
        private DefaultAction default_action;

        private DefaultAction defaultAction;

        public String getModifiedTime() {
            return modifiedTime;
        }

        public void setModifiedTime(String modifiedTime) {
            this.modifiedTime = modifiedTime;
        }

        private String modifiedTime;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public List<Button> getButton() {
            return button;
        }

        public void setButton(List<Button> button) {
            this.button = button;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Action> getActions() {
            return actions;
        }

        public void setActions(List<Action> actions) {
            this.actions = actions;
        }

        public DefaultAction getDefaultAction() {
            return default_action!= null ? default_action : defaultAction;
        }

        public void setDefaultAction(DefaultAction default_action) {
            this.default_action = default_action;
            this.defaultAction= default_action;
        }

    }

    public static class Button implements Serializable{

        private String title;
        private String theme;
        private String type;
        private String utterance;
        private String url;

        public ImageModel getImage() {
            return image;
        }

        public void setImage(ImageModel image) {
            this.image = image;
        }

        private ImageModel image;

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        private String payload;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTheme() {
            if(theme != null)
                return theme;
            else
                return "#4741fa";
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUtterance() {
            return utterance;
        }

        public void setUtterance(String utterance) {
            this.utterance = utterance;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public static class DefaultAction implements Serializable{

        private String title;
        private String type;
        private String url;

        public String getUtterance() {
            return utterance;
        }

        public void setUtterance(String utterance) {
            this.utterance = utterance;
        }

        private String utterance;

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        private String payload;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


}
