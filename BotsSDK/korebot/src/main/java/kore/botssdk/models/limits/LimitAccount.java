package kore.botssdk.models.limits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LimitAccount implements Serializable {
    @SerializedName("licenseId")
    @Expose
    private String licenseId;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("managedBy")
    @Expose
    private String managedBy;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("emailId")
    @Expose
    private String emailId;

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(String managedBy) {
        this.managedBy = managedBy;
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

}
