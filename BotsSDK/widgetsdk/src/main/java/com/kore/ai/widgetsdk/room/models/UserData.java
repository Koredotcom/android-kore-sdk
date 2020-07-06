package com.kore.ai.widgetsdk.room.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.kore.ai.widgetsdk.room.converters.StringListConverter;

import java.util.ArrayList;

/**
 * Created by Ramachandra Pradeep on 19-Jul-18.
 */

@Entity(tableName = "user_data")
public class UserData {

    public static final String TABLE_NAME = "user_data";
    public static final String USER_ID = "user_id";
    public static final String FIRST_NAME = "firstname";
    public static final String LAST_NAME = "lastname";
    public static final String EMAIL = "email";
    public static final String ORG_ID = "org_id";
    public static final String PROFILE_COLOR = "profColour";
    public static final String PROFILE_IMAGE_COLOR = "prof_img_color";
    public static final String IS_LOGGED_OFF = "isLoggedOff";
    public static final String LAST_SYNCED = "last_synced";
    public static final String IDENTITITIES = "identities";
    public static final String MAKE_CONSTENT_POLICY = "make_constent_policy";
    public static final String MUTE_TIME = "mute_time";
    public static final String SSO_TYPE = "sso_type";
    public static final String USER_APP_STATE = "user_app_state";
    public static final String ORG_LOGO_URL = "org_logo_url";
    public static final String ENROLL_TYPE = "enroll_type";
    public static final String ROLLS = "admin_roles";

    public static final String SSO_GOOGLE = "google";
    public static final String SSO_365 = "AzureAD";
    public static final String SSO_365_EXCHAGE = "ews";


    @PrimaryKey
    @ColumnInfo(name = USER_ID)
    @NonNull private String id;

    @ColumnInfo(name = FIRST_NAME)
    public String fName;

    @ColumnInfo(name = LAST_NAME)
    public String lName;

    @ColumnInfo(name = EMAIL)
    private String emailId;

    @ColumnInfo(name = ORG_ID)
    private String orgID;

    @ColumnInfo(name = PROFILE_IMAGE_COLOR)
    private String img;

    @ColumnInfo(name = PROFILE_COLOR)
    private String profColour;

    @ColumnInfo(name = IS_LOGGED_OFF)
    private boolean isLoggedOff;

    @ColumnInfo(name = LAST_SYNCED)
    private long lastSynced;

    @ColumnInfo(name = IDENTITITIES)
    private String identities;

    @ColumnInfo(name = MUTE_TIME)
    private long muteTime;

    @ColumnInfo(name = SSO_TYPE)
    private String ssoType;

    @ColumnInfo(name = MAKE_CONSTENT_POLICY)
    private boolean makeConsentPolicyAccept;

    @ColumnInfo(name = USER_APP_STATE)
    private String userAppState;

    @ColumnInfo(name = ORG_LOGO_URL)
    private String orgLogoUrl;

    @ColumnInfo(name = ENROLL_TYPE)
    private String enrollType;

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    @ColumnInfo(name=ROLLS)
    @TypeConverters(StringListConverter.class)
    private ArrayList<String> roles = new ArrayList<String>();

    @Ignore
    private String deviceType = "android";




    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }



    public boolean isLoggedOff() {
        return isLoggedOff;
    }

    public void setLoggedOff(boolean loggedOff) {
        isLoggedOff = loggedOff;
    }

    public long getLastSynced() {
        return lastSynced;
    }

    public void setLastSynced(long lastSynced) {
        this.lastSynced = lastSynced;
    }

    public String getIdentities() {
        return identities;
    }

    public void setIdentities(String identities) {
        this.identities = identities;
    }

    public long getMuteTime() {
        return muteTime;
    }

    public void setMuteTime(long muteTime) {
        this.muteTime = muteTime;
    }

    public boolean isMakeConsentPolicyAccept() {
        return makeConsentPolicyAccept;
    }

    public void setMakeConsentPolicyAccept(boolean makeConsentPolicyAccept) {
        this.makeConsentPolicyAccept = makeConsentPolicyAccept;
    }

    public String getUserAppState() {
        return userAppState;
    }

    public void setUserAppState(String userAppState) {
        this.userAppState = userAppState;
    }

    public String getOrgLogoUrl() {
        return orgLogoUrl;
    }

    public void setOrgLogoUrl(String orgLogoUrl) {
        this.orgLogoUrl = orgLogoUrl;
    }

    public String getEnrollType() {
        return enrollType;
    }

    public void setEnrollType(String enrollType) {
        this.enrollType = enrollType;
    }

    public void processForPersistance() {
        if(fName == null) fName = "";
        if(lName == null) lName = "";
    }
    public String getSsoType() {
        return ssoType;
    }

    public void setSsoType(String ssoType) {
        this.ssoType = ssoType;
    }

    public String getDisplayName() {
        return getfName() + " " +  getlName();
    }

    public String getProfColour() {
        return profColour;//SDKConfiguration.APP_REQ_COLOR/*profColour*/;
    }

    public void setProfColour(String profColour) {
        this.profColour = profColour;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
