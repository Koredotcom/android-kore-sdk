package kore.botssdk.models;

public class BrandingOverrideHistoryModel {
    private boolean enable;
    private BrandingOverrideHistoryRecentModel recent;
    private BrandingOverrideHistoryRecentModel paginated_scroll;

    public BrandingOverrideHistoryRecentModel getRecent() {
        return recent;
    }

    public boolean isEnable() {
        return enable;
    }

    public BrandingOverrideHistoryRecentModel getPaginated_scroll() {
        return paginated_scroll;
    }
}
