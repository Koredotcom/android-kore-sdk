package kore.botssdk.models;

import static kore.botssdk.utils.DateUtils.getCorrectedTimeZone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kore.botssdk.models.limits.LimitAccount;
import kore.botssdk.models.limits.UsageLimit;

public class KaUserProfileModel implements Serializable, Cloneable {
    private String id;
    private String fN;
    private String lN;
    private String color;
    private String dept;
    private String jTitle;
    private String orgId;
    private String cloudProvider;
    private boolean enableOnlineMeet;

    public boolean isEnableOnlineMeet() {
        return enableOnlineMeet;
    }

    public void setEnableOnlineMeet(boolean enableOnlineMeet) {
        this.enableOnlineMeet = enableOnlineMeet;
    }

    public String getCloudProvider() {
        return cloudProvider;
    }

    public void setCloudProvider(String cloudProvider) {
        this.cloudProvider = cloudProvider;
    }

    @SerializedName("usageLimits")
    @Expose
    private List<UsageLimit> usageLimits = null;

    public List<UsageLimit> getUsageLimits() {
        return usageLimits;
    }

    public void setUsageLimits(List<UsageLimit> usageLimits) {
        this.usageLimits = usageLimits;
    }
    @SerializedName("account")
    @Expose
    private LimitAccount limitAccount;

    public LimitAccount getLimitAccount() {
        return limitAccount;
    }

    public void setLimitAccount(LimitAccount limitAccount) {
        this.limitAccount = limitAccount;
    }




    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String empId;
    private String manager;
    private String phoneNo;
    private String designation;
    private String address;

    public Onboarding getOnboarding() {
        return onboarding;
    }

    public void setOnboarding(Onboarding onboarding) {
        this.onboarding = onboarding;
    }

    @SerializedName("onboarding")
    @Expose
    private Onboarding onboarding;
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public NPrefs getnPrefs() {
        return nPrefs;
    }

    public void setnPrefs(NPrefs nPrefs) {
        this.nPrefs = nPrefs;
    }

    private String icon;

    private AccountInfo accountInfo = new AccountInfo();

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Boolean getSpeakerIcon() {
        return speakerIcon;
    }

    public void setSpeakerIcon(Boolean speakerIcon) {
        this.speakerIcon = speakerIcon;
    }

    public int getWeatherUnit() {
        return weatherUnit;
    }

    public void setWeatherUnit(int weatherUnit) {
        this.weatherUnit = weatherUnit;
    }

    private Boolean speakerIcon;

    private int weatherUnit;

    public NPrefs getNPrefs() {
        return nPrefs;
    }

    public final void setNPrefs(NPrefs nPrefs) {
        this.nPrefs = nPrefs;
    }

    private NPrefs nPrefs;

    public String getKaIdentity() {
        return kaIdentity;
    }

    public void setKaIdentity(String kaIdentity) {
        this.kaIdentity = kaIdentity;
    }

    private String emailId;
    private String workTimeZone;
    private String kaIdentity;

    public String[] getWorkTimeZoneOffset() {
        return workTimeZoneOffset;
    }

    public void setWorkTimeZoneOffset(String[] workTimeZoneOffset) {
        this.workTimeZoneOffset = workTimeZoneOffset;
    }

    private String[] workTimeZoneOffset;

    public String getTempWorkTimeZone() {
        return getCorrectedTimeZone(tempWorkTimeZone);
    }

    public void setTempWorkTimeZone(String tempWorkTimeZone) {
        this.tempWorkTimeZone = getCorrectedTimeZone(tempWorkTimeZone);
    }

    public String getWorkTimeZoneCity() {
        return workTimeZoneCity;
    }

    public void setWorkTimeZoneCity(String workTimeZoneCity) {
        this.workTimeZoneCity = workTimeZoneCity;
    }

    private String tempWorkTimeZone;
    private String workTimeZoneCity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfN() {
        return fN;
    }

    public void setfN(String fN) {
        this.fN = fN;
    }

    public String getlN() {
        return lN;
    }

    public void setlN(String lN) {
        this.lN = lN;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getjTitle() {
        return jTitle;
    }

    public void setjTitle(String jTitle) {
        this.jTitle = jTitle;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getWorkTimeZone() {
        return getCorrectedTimeZone(workTimeZone);
    }

    public void setWorkTimeZone(String workTimeZone) {
        this.workTimeZone = getCorrectedTimeZone(workTimeZone);
    }

    public boolean isOnBoarded() {
        return isOnBoarded;
    }

    public void setOnBoarded(boolean onBoarded) {
        isOnBoarded = onBoarded;
    }

    public boolean isTeachInitiated() {
        return isTeachInitiated;
    }

    public void setTeachInitiated(boolean teachInitiated) {
        isTeachInitiated = teachInitiated;
    }

    public ArrayList<WorkHoursModel> getWorkHours() {
        return workHours;
    }

    public void setWorkHours(ArrayList<WorkHoursModel> workHours) {
        this.workHours = workHours;
    }

    private boolean isOnBoarded;
    private boolean isTeachInitiated;
    private ArrayList<WorkHoursModel> workHours;


    @Override
    public KaUserProfileModel clone() throws CloneNotSupportedException {
        KaUserProfileModel kaUserProfileModel = (KaUserProfileModel) super.clone();
        try {
            kaUserProfileModel.setNPrefs(kaUserProfileModel.getNPrefs().clone());
        } catch (Exception e) {
          e.printStackTrace();
        }
        return kaUserProfileModel;
    }




    public static class Onboarding  implements Serializable{

        @SerializedName("android")
        @Expose
        private Long android;
        @SerializedName("ios")
        @Expose
        private Long ios;
        @SerializedName("knwPortal")
        @Expose
        private Long knwPortal;
        @SerializedName("plugin")
        @Expose
        private Long plugin;
        @SerializedName("skillStore")
        @Expose
        private Long skillStore;
        @SerializedName("sta")
        @Expose
        private Long sta;
        @SerializedName("web")
        @Expose
        private Long web;

        public Long getAndroid() {
            return android;
        }

        public void setAndroid(Long android) {
            this.android = android;
        }

        public Long getIos() {
            return ios;
        }

        public void setIos(Long ios) {
            this.ios = ios;
        }

        public Long getKnwPortal() {
            return knwPortal;
        }

        public void setKnwPortal(Long knwPortal) {
            this.knwPortal = knwPortal;
        }

        public Long getPlugin() {
            return plugin;
        }

        public void setPlugin(Long plugin) {
            this.plugin = plugin;
        }

        public Long getSkillStore() {
            return skillStore;
        }

        public void setSkillStore(Long skillStore) {
            this.skillStore = skillStore;
        }

        public Long getSta() {
            return sta;
        }

        public void setSta(Long sta) {
            this.sta = sta;
        }

        public Long getWeb() {
            return web;
        }

        public void setWeb(Long web) {
            this.web = web;
        }

    }
}
