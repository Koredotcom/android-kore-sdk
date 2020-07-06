package com.kore.ai.widgetsdk.models;

import java.util.List;

public class TaskCompletionModel {


private String status;
/*private Long dueDate;*/

private List<String> tIds = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

/*public Long getDueDate() {
return dueDate;
}

public void setDueDate(Long dueDate) {
this.dueDate = dueDate;
}*/

public List<String> getTIds() {
return tIds;
}

public void setTIds(List<String> tIds) {
this.tIds = tIds;
}

}