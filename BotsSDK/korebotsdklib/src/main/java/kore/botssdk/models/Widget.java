package kore.botssdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 08-Mar-19.
 */

public class Widget implements Serializable {


    public class Action implements Serializable {

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

    }

    private String id;
    private String title;
    private String refresh_interval;
    private String cache_interval;
    private String summary_placeholder;
    private String type;
    private String theme;
   // private String api;
    private String utterances_header;
    @SerializedName("actions")
    @Expose
    private List<Action> actions = null;
    public List<String> getUtterances() {
        return utterances;
    }

    public void setUtterances(List<String> utterances) {
        this.utterances = utterances;
    }

    private List<String> utterances = null;

    private List<Filter> filters = null;

    public String getId() {
        return id;
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
        return refresh_interval;
    }
    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setRefreshInterval(String refresh_interval) {
        this.refresh_interval = refresh_interval;
    }

    public String getCacheInterval() {
        return cache_interval;
    }

    public void setCacheInterval(String cache_interval) {
        this.cache_interval = cache_interval;
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
    @SerializedName("custom_style")
    @Expose
    private String customStyle;

    public String getCustomStyle() {
        return customStyle!=null?customStyle:"default";
    }

    public void setCustomStyle(String customStyle) {
        this.customStyle = customStyle;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }


    public class Hook implements Serializable {

        @SerializedName("method")
        @Expose
        private String method;
        @SerializedName("api")
        @Expose
        private String api;
        @SerializedName("params")
        @Expose
        private Params params;
        @SerializedName("body")
        @Expose
        private Body body;

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

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }

    }

    public class Params implements Serializable{

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

    }

    public class Tz implements Serializable {

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
    public class Body implements Serializable{

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

    }
}
