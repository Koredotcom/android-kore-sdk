package kore.botssdk.models;


public class BotData {
    private String cloudProvider;
    private String workHours;
    private String KATZ;
    private boolean enableOnlineMeet;

    public String getCloudProvider() {
        return cloudProvider;
    }

    public void setCloudProvider(String cloudProvider) {
        this.cloudProvider = cloudProvider;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public String getKATZ() {
        return KATZ;
    }

    public void setKATZ(String KATZ) {
        this.KATZ = KATZ;
    }

    public boolean isEnableOnlineMeet() {
        return enableOnlineMeet;
    }

    public void setEnableOnlineMeet(boolean enableOnlineMeet) {
        this.enableOnlineMeet = enableOnlineMeet;
    }
}
