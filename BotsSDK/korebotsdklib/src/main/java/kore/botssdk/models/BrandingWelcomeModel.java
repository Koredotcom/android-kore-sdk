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

    @NonNull
    public StaticLinksModel getStatic_links() {
        return static_links;
    }
    @NonNull
    public PromotionalContentModel getPromotional_content() {
        return promotional_content;
    }
    @NonNull
    public BrandingTitleModel getSub_title() {
        return sub_title;
    }

    @NonNull
    public String getLayout() {
        return layout;
    }

    @NonNull
    public BrandingTitleModel getTitle() {
        return title;
    }

    @NonNull
    public BrandingStarterBoxModel getStarter_box() {
        return starter_box;
    }

    @NonNull
    public BrandingTitleModel getBackground() {
        return background;
    }

    @NonNull
    public BrandingTitleModel getBottom_background() {
        return bottom_background;
    }
    @NonNull
    public BrandingTitleModel getNote() {
        return note;
    }

    @NonNull
    public BrandingTitleModel getTop_fonts() {
        return top_fonts;
    }

    @NonNull
    public BrandingWelcomeLogoModel getLogo() {
        return logo;
    }

    public boolean isShow() {
        return show;
    }
}
