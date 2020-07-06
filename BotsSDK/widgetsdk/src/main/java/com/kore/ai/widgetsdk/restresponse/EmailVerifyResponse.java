package com.kore.ai.widgetsdk.restresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmailVerifyResponse {

@SerializedName("status")
@Expose
private String status;
@SerializedName("ssoProvider")
@Expose
private String ssoProvider;
@SerializedName("idp")
@Expose
private String idp;
@SerializedName("companyName")
@Expose
private String companyName;
@SerializedName("scope")
@Expose
private String scope;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getSsoProvider() {
return ssoProvider;
}

public void setSsoProvider(String ssoProvider) {
this.ssoProvider = ssoProvider;
}

public String getIdp() {
return idp;
}

public void setIdp(String idp) {
this.idp = idp;
}

public String getCompanyName() {
return companyName;
}

public void setCompanyName(String companyName) {
this.companyName = companyName;
}

public String getScope() {
return scope;
}

public void setScope(String scope) {
this.scope = scope;
}

}