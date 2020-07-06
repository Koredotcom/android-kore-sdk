package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetinngActionResponse {

@SerializedName("updated")
@Expose
private Boolean updated;

public Boolean getUpdated() {
return updated;
}

public void setUpdated(Boolean updated) {
this.updated = updated;
}

}