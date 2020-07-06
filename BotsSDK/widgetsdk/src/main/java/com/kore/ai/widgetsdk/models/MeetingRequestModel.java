package com.kore.ai.widgetsdk.models;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 27-Aug-18.
 */

public class MeetingRequestModel {
    private String _id;
    private String title;
    private String desc;
    private Owner owner;
    private Preview preview;
    private String expireOn;
    private String orgId;
    private int state;
    private String lMod;
    private String cOn;
    private List<Slot> slots = null;
    private List<Object> dU = null;
    private List<String> pU = null;
    private List<Object> rU = null;
    private int respondCount;
    private int pendingCount;
    private int declinedCount;
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLMod() {
        return lMod;
    }

    public void setLMod(String lMod) {
        this.lMod = lMod;
    }

    public String getCOn() {
        return cOn;
    }

    public void setCOn(String cOn) {
        this.cOn = cOn;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public List<Object> getDU() {
        return dU;
    }

    public void setDU(List<Object> dU) {
        this.dU = dU;
    }

    public List<String> getPU() {
        return pU;
    }

    public void setPU(List<String> pU) {
        this.pU = pU;
    }

    public List<Object> getRU() {
        return rU;
    }

    public void setRU(List<Object> rU) {
        this.rU = rU;
    }

    public int getRespondCount() {
        return respondCount;
    }

    public void setRespondCount(int respondCount) {
        this.respondCount = respondCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public int getDeclinedCount() {
        return declinedCount;
    }

    public void setDeclinedCount(int declinedCount) {
        this.declinedCount = declinedCount;
    }
}
