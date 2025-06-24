package kore.botssdk.models;

public class BrandingOverrideHistoryModel {
    private Boolean enable;
    private BrandingOverrideHistoryRecentModel recent;
    private BrandingOverrideHistoryRecentModel paginated_scroll;

    public BrandingOverrideHistoryModel updateWith(BrandingOverrideHistoryModel configModel) {
        enable = configModel.enable != null ? configModel.enable : enable;
        recent = recent != null && configModel.recent != null ? recent.updateWith(configModel.recent) : recent;
        paginated_scroll = paginated_scroll != null && configModel.paginated_scroll != null ? paginated_scroll.updateWith(configModel.paginated_scroll) : paginated_scroll;
        return this;
    }

    public BrandingOverrideHistoryRecentModel getRecent() {
        return recent;
    }

    public boolean isEnable() {
        return enable != null ? enable : false;
    }

    public BrandingOverrideHistoryRecentModel getPaginated_scroll() {
        return paginated_scroll;
    }
}
