package kore.botssdk.models;

import java.io.Serializable;

import kore.botssdk.utils.StringUtils;

public class BrandingWelcomeModel implements Serializable {
    private Boolean show;
    private String layout;
    private BrandingWelcomeLogoModel logo;
    private BrandingTitleModel title;
    private BrandingTitleModel sub_title;
    private BrandingTitleModel note;
    private BrandingTitleModel background;
    private BrandingTitleModel top_fonts;
    private BrandingTitleModel bottom_background;
    private BrandingStarterBoxModel starter_box;
    private PromotionalContentModel promotional_content;
    private StaticLinksModel static_links;

    public BrandingWelcomeModel updateWith(BrandingWelcomeModel configModel) {
        show = configModel.show != null ? configModel.show : null;
        layout = !StringUtils.isNullOrEmpty(configModel.layout) ? configModel.layout : layout;
        logo = configModel.logo != null && logo != null ? logo.updateWith(configModel.logo) : logo;
        title = configModel.title != null && title != null ? title.updateWith(configModel.title) : title;
        sub_title = configModel.sub_title != null && sub_title != null ? sub_title.updateWith(configModel.sub_title) : sub_title;
        note = configModel.note != null && note != null ? note.updateWith(configModel.note) : note;
        background = configModel.background != null && background != null ? background.updateWith(configModel.background) : background;
        top_fonts = configModel.top_fonts != null && top_fonts != null ? top_fonts.updateWith(configModel.top_fonts) : top_fonts;
        bottom_background = configModel.bottom_background != null && bottom_background != null ? bottom_background.updateWith(configModel.bottom_background) : bottom_background;
        starter_box = configModel.starter_box != null && starter_box != null ? starter_box.updateWith(configModel.starter_box) : starter_box;
        promotional_content = configModel.promotional_content != null && promotional_content != null ? promotional_content.updateWith(configModel.promotional_content) : promotional_content;
        static_links = configModel.static_links != null && static_links != null ? static_links.updateWith(configModel.static_links) : static_links;
        return this;
    }

    public StaticLinksModel getStatic_links() {
        return static_links;
    }

    public PromotionalContentModel getPromotional_content() {
        return promotional_content;
    }

    public BrandingTitleModel getSub_title() {
        return sub_title;
    }

    public String getLayout() {
        return layout;
    }

    public BrandingTitleModel getTitle() {
        return title;
    }

    public BrandingStarterBoxModel getStarter_box() {
        return starter_box;
    }

    public BrandingTitleModel getBackground() {
        return background;
    }

    public BrandingTitleModel getBottom_background() {
        return bottom_background;
    }

    public BrandingTitleModel getNote() {
        return note;
    }

    public BrandingTitleModel getTop_fonts() {
        return top_fonts;
    }

    public BrandingWelcomeLogoModel getLogo() {
        return logo;
    }

    public boolean isShow() {
        return show != null ? show : false;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
