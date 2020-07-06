package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDomainInfoModel {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("identity")
@Expose
private String identity;
@SerializedName("domain")
@Expose
private String domain;
@SerializedName("type")
@Expose
private String type;
@SerializedName("userId")
@Expose
private String userId;
@SerializedName("emailId")
@Expose
private String emailId;
@SerializedName("lMod")
@Expose
private String lMod;
@SerializedName("cOn")
@Expose
private String cOn;
@SerializedName("__v")
@Expose
private Long v;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getIdentity() {
return identity;
}

public void setIdentity(String identity) {
this.identity = identity;
}

public String getDomain() {
return domain;
}

public void setDomain(String domain) {
this.domain = domain;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getEmailId() {
return emailId;
}

public void setEmailId(String emailId) {
this.emailId = emailId;
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

public Long getV() {
return v;
}

public void setV(Long v) {
this.v = v;
}

}