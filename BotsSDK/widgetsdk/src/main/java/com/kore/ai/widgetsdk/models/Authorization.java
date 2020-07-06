package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 */
public class Authorization {
    private String identity;
    private String resourceOwnerID;
    private List<String> datasource = new ArrayList<String>();
    private String orgID;
    private String clientID;
    private String sesId;
    private String accountId;
    private String managedBy;
    private List<String> scope = new ArrayList<String>();
    private AllowedResource allowedResource;
    private String platDevType;
    private String lastActivity;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String expiresDate;
    private String refreshExpiresDate;
    private String issuedDate;

    /**
     *
     * @return
     * The identity
     */
    public String getIdentity() {
        return identity;
    }

    /**
     *
     * @param identity
     * The identity
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     *
     * @return
     * The resourceOwnerID
     */
    public String getResourceOwnerID() {
        return resourceOwnerID;
    }

    /**
     *
     * @param resourceOwnerID
     * The resourceOwnerID
     */
    public void setResourceOwnerID(String resourceOwnerID) {
        this.resourceOwnerID = resourceOwnerID;
    }

    /**
     *
     * @return
     * The datasource
     */
    public List<String> getDatasource() {
        return datasource;
    }

    /**
     *
     * @param datasource
     * The datasource
     */
    public void setDatasource(List<String> datasource) {
        this.datasource = datasource;
    }

    /**
     *
     * @return
     * The orgID
     */
    public String getOrgID() {
        return orgID;
    }

    /**
     *
     * @param orgID
     * The orgID
     */
    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    /**
     *
     * @return
     * The clientID
     */
    public String getClientID() {
        return clientID;
    }

    /**
     *
     * @param clientID
     * The clientID
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    /**
     *
     * @return
     * The sesId
     */
    public String getSesId() {
        return sesId;
    }

    /**
     *
     * @param sesId
     * The sesId
     */
    public void setSesId(String sesId) {
        this.sesId = sesId;
    }

    /**
     *
     * @return
     * The accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     *
     * @param accountId
     * The accountId
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     *
     * @return
     * The managedBy
     */
    public String getManagedBy() {
        return managedBy;
    }

    /**
     *
     * @param managedBy
     * The managedBy
     */
    public void setManagedBy(String managedBy) {
        this.managedBy = managedBy;
    }

    /**
     *
     * @return
     * The scope
     */
    public List<String> getScope() {
        return scope;
    }

    /**
     *
     * @param scope
     * The scope
     */
    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    /**
     *
     * @return
     * The allowedResource
     */
    public AllowedResource getAllowedResource() {
        return allowedResource;
    }

    /**
     *
     * @param allowedResource
     * The allowedResource
     */
    public void setAllowedResource(AllowedResource allowedResource) {
        this.allowedResource = allowedResource;
    }

    /**
     *
     * @return
     * The platDevType
     */
    public String getPlatDevType() {
        return platDevType;
    }

    /**
     *
     * @param platDevType
     * The platDevType
     */
    public void setPlatDevType(String platDevType) {
        this.platDevType = platDevType;
    }

    /**
     *
     * @return
     * The lastActivity
     */
    public String getLastActivity() {
        return lastActivity;
    }

    /**
     *
     * @param lastActivity
     * The lastActivity
     */
    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    /**
     *
     * @return
     * The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     * The accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     * The refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     *
     * @param refreshToken
     * The refreshToken
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     *
     * @return
     * The tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     *
     * @param tokenType
     * The token_type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     *
     * @return
     * The expiresDate
     */
    public String getExpiresDate() {
        return expiresDate;
    }

    /**
     *
     * @param expiresDate
     * The expiresDate
     */
    public void setExpiresDate(String expiresDate) {
        this.expiresDate = expiresDate;
    }

    /**
     *
     * @return
     * The refreshExpiresDate
     */
    public String getRefreshExpiresDate() {
        return refreshExpiresDate;
    }

    /**
     *
     * @param refreshExpiresDate
     * The refreshExpiresDate
     */
    public void setRefreshExpiresDate(String refreshExpiresDate) {
        this.refreshExpiresDate = refreshExpiresDate;
    }

    /**
     *
     * @return
     * The issuedDate
     */
    public String getIssuedDate() {
        return issuedDate;
    }

    /**
     *
     * @param issuedDate
     * The issuedDate
     */
    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }


}