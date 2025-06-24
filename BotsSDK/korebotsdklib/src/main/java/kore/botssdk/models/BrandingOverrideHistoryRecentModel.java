package kore.botssdk.models;

import kore.botssdk.utils.StringUtils;

public class BrandingOverrideHistoryRecentModel {
    private int batch_size;
    private String loading_label;
    private Boolean enable;

    public BrandingOverrideHistoryRecentModel updateWith(BrandingOverrideHistoryRecentModel configModel) {
        enable = configModel.enable != null ? configModel.enable : enable;
        loading_label = !StringUtils.isNullOrEmpty(configModel.loading_label) ? configModel.loading_label : loading_label;
        return this;
    }

    public int getBatch_size() {
        return batch_size;
    }

    public String getLoading_label() {
        return loading_label;
    }

    public boolean isEnable() {
        return enable != null ? enable : false;
    }
}
