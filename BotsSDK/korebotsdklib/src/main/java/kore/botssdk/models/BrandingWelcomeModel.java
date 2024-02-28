package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BrandingWelcomeModel implements Serializable {
    private boolean show;
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
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
