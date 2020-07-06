package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateMeetingNotesResponse {

@SerializedName("creator")
@Expose
private String creator;
@SerializedName("nViews")
@Expose
private Long nViews;
@SerializedName("mId")
@Expose
private String mId;
@SerializedName("pId")
@Expose
private String pId;
@SerializedName("title")
@Expose
private String title;
@SerializedName("type")
@Expose
private String type;
/*@SerializedName("version")
@Expose
private Long version;*/
@SerializedName("lMUId")
@Expose
private String lMUId;
@SerializedName("desc")
@Expose
private String desc;
@SerializedName("createdOn")
@Expose
private Long createdOn;
@SerializedName("lastMod")
@Expose
private Long lastMod;
@SerializedName("myActions")
@Expose
private MyActions myActions;
@SerializedName("id")
@Expose
private String id;
@SerializedName("linkPreviews")
@Expose
private List<Object> linkPreviews = null;

public String getCreator() {
return creator;
}

public void setCreator(String creator) {
this.creator = creator;
}

public Long getNViews() {
return nViews;
}

public void setNViews(Long nViews) {
this.nViews = nViews;
}

public String getMId() {
return mId;
}

public void setMId(String mId) {
this.mId = mId;
}

public String getPId() {
return pId;
}

public void setPId(String pId) {
this.pId = pId;
}

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

/*public Long getVersion() {
return version;
}*/

/*public void setVersion(Long version) {
this.version = version;
}*/

public String getLMUId() {
return lMUId;
}

public void setLMUId(String lMUId) {
this.lMUId = lMUId;
}

public String getDesc() {
return desc;
}

public void setDesc(String desc) {
this.desc = desc;
}

public Long getCreatedOn() {
return createdOn;
}

public void setCreatedOn(Long createdOn) {
this.createdOn = createdOn;
}

public Long getLastMod() {
return lastMod;
}

public void setLastMod(Long lastMod) {
this.lastMod = lastMod;
}

public MyActions getMyActions() {
return myActions;
}

public void setMyActions(MyActions myActions) {
this.myActions = myActions;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public List<Object> getLinkPreviews() {
return linkPreviews;
}

public void setLinkPreviews(List<Object> linkPreviews) {
this.linkPreviews = linkPreviews;
}
    public class MyActions {


    }
}