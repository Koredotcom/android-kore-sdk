package kore.botssdk.models;

import kore.botssdk.utils.StringUtils;

public class BrandingStaticLinksModel {
    private Boolean show;
    private String layout;

    public BrandingStaticLinksModel updateWith(BrandingStaticLinksModel configModel) {
        show = configModel.show != null ? configModel.show : show;
        layout = !StringUtils.isNullOrEmpty(configModel.layout) ? configModel.layout : layout;
        return this;
    }
}
