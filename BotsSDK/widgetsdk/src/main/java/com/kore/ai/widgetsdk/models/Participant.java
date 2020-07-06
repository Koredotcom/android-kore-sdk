package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Participant {

@SerializedName("lN")
@Expose
private String lN;
@SerializedName("name")
@Expose
private String name;
@SerializedName("fN")
@Expose
private String fN;
@SerializedName("emailId")
@Expose
private String emailId;
@SerializedName("id")
@Expose
private String id;

public String getLN() {
return lN;
}

public void setLN(String lN) {
this.lN = lN;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getFN() {
return fN;
}

public void setFN(String fN) {
this.fN = fN;
}

public String getEmailId() {
return emailId;
}

public void setEmailId(String emailId) {
this.emailId = emailId;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

}