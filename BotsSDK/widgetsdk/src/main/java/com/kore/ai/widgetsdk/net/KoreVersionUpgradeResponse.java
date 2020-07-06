package com.kore.ai.widgetsdk.net;

import com.google.gson.annotations.Expose;

/**
 * Created by Naval on 26-Oct-15.
 */
public class KoreVersionUpgradeResponse {

    @Expose
    private String appId;

    @Expose
    private String appDescription;

    @Expose
    private String platform;

    @Expose
    private String version;

    @Expose
    private String title;

    @Expose
    private String body;

    @Expose
    private boolean mandatory;

    @Expose
    private boolean update;


    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    private String downloadLink;

    /**
     *
     * @return
     * The appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     *
     * @param appId
     * The appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     *
     * @return
     * The appDescription
     */
    public String getAppDescription() {
        return appDescription;
    }

    /**
     *
     * @param appDescription
     * The appDescription
     */
    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    /**
     *
     * @return
     * The platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     *
     * @param platform
     * The platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     *
     * @return
     * The version
     */
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The body
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body
     * The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     *
     * @return
     * The mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     *
     * @param mandatory
     * The mandatory
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     *
     * @return
     * The update
     */
    public boolean isUpdate() {
        return update;
    }

    /**
     *
     * @param update
     * The update
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }
}
