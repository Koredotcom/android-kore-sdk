package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingWelcomeLogoModel implements Serializable {
    private String logo_url;

    public BrandingWelcomeLogoModel updateWith(BrandingWelcomeLogoModel configModel) {
        logo_url = !StringUtils.isNullOrEmpty(configModel.logo_url) ? configModel.logo_url : logo_url;
        return this;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }
}
