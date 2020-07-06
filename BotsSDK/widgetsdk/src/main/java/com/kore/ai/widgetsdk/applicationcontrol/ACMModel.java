package com.kore.ai.widgetsdk.applicationcontrol;

/**
 * Created by Ramachandra Pradeep on 6/24/2016.
 */
public class ACMModel {
    private ApplicationControl applicationControl;
    private OrgSettings orgSettings;
    private String accountId;
    private String orgId;
    private boolean isFreeDomain;
    private String customerLicenseType;
    private String managedBy;
    private boolean isEnterpriseExist;
    private boolean isTrial;
    private boolean isDVerified;
    private boolean wfAdmin;
    private int groupChatLimit;

    public ACMModel(){
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean getFreeDomain() {
        return isFreeDomain;
    }

    public void setFreeDomain(boolean freeDomain) {
        isFreeDomain = freeDomain;
    }

    public String getCustomerLicenseType() {
        return customerLicenseType;
    }

    public void setCustomerLicenseType(String customerLicenseType) {
        this.customerLicenseType = customerLicenseType;
    }

    public String getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(String managedBy) {
        this.managedBy = managedBy;
    }

    public boolean getEnterpriseExist() {
        return isEnterpriseExist;
    }

    public void setEnterpriseExist(boolean enterpriseExist) {
        isEnterpriseExist = enterpriseExist;
    }

    public boolean getTrial() {
        return isTrial;
    }

    public void setTrial(boolean trial) {
        isTrial = trial;
    }

    public boolean getDVerified() {
        return isDVerified;
    }

    public void setDVerified(boolean DVerified) {
        isDVerified = DVerified;
    }

    public boolean getWfAdmin() {
        return wfAdmin;
    }

    public void setWfAdmin(boolean wfAdmin) {
        this.wfAdmin = wfAdmin;
    }


    /**
     *
     * @return
     * The applicationControl
     */
    public ApplicationControl getApplicationControl() {
        if(applicationControl == null)
            applicationControl = new ApplicationControl();
        return applicationControl;
    }

    /**
     *
     *
     * @param applicationControl
     * The applicationControl
     */
    public void setApplicationControl(ApplicationControl applicationControl) {
        this.applicationControl = applicationControl;
    }

    public OrgSettings getOrgSettings() {
        return orgSettings;
    }

    public void setOrgSettings(OrgSettings orgSettings) {
        this.orgSettings = orgSettings;
    }
    public int getGroupChatLimit() {
        return groupChatLimit;
    }

    public void setGroupChatLimit(int groupChatLimit) {
        this.groupChatLimit = groupChatLimit;
    }
}
