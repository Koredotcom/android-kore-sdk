package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 03-Oct-18.
 */

public class BotHistoryMessage {
    private String _id;
    private String type;
    private String status;
    private String lmodifiedOn;
    private String createdBy;
    private List<Channel> channels = null;
    private List<Component> components = null;
    private String botId;
    private String orgId;
    private String tN;
    private String createdOn;
    private Integer v;
    private String resourceid;
    private String lmodifiedBy;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLmodifiedOn() {
        return lmodifiedOn;
    }

    public void setLmodifiedOn(String lmodifiedOn) {
        this.lmodifiedOn = lmodifiedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTN() {
        return tN;
    }

    public void setTN(String tN) {
        this.tN = tN;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getLmodifiedBy() {
        return lmodifiedBy;
    }

    public void setLmodifiedBy(String lmodifiedBy) {
        this.lmodifiedBy = lmodifiedBy;
    }
}
