package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SkillsListResponse {

@SerializedName("id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("uTrigger")
@Expose
private String uTrigger;
@SerializedName("utterances")
@Expose
private List<String> utterances = null;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getUTrigger() {
return uTrigger;
}

public void setUTrigger(String uTrigger) {
this.uTrigger = uTrigger;
}

public List<String> getUtterances() {
return utterances;
}

public void setUtterances(List<String> utterances) {
this.utterances = utterances;
}

}