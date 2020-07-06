package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetingDetails {

@SerializedName("start")
@Expose
private Long start;
@SerializedName("end")
@Expose
private Long end;
@SerializedName("title")
@Expose
private String title;
@SerializedName("otherAttendees")
@Expose
private String otherAttendees;
@SerializedName("id")
@Expose
private String id;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    private String cId;

public Long getStart() {
return start;
}

public void setStart(Long start) {
this.start = start;
}

public Long getEnd() {
return end;
}

public void setEnd(Long end) {
this.end = end;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getOtherAttendees() {
return otherAttendees;
}

public void setOtherAttendees(String otherAttendees) {
this.otherAttendees = otherAttendees;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

}