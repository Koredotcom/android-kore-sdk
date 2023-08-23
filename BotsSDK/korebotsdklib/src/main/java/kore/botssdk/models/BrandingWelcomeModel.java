package kore.botssdk.models;

public class BrandingWelcomeModel {
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

    public void setBackground(BrandingTitleModel background) {
        this.background = background;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setSub_title(BrandingTitleModel sub_title) {
        this.sub_title = sub_title;
    }

    public void setTitle(BrandingTitleModel title) {
        this.title = title;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setBottom_background(BrandingTitleModel bottom_background) {
        this.bottom_background = bottom_background;
    }

    public void setLogo(BrandingWelcomeLogoModel logo) {
        this.logo = logo;
    }

    public void setNote(BrandingTitleModel note) {
        this.note = note;
    }

    public void setStarter_box(BrandingStarterBoxModel starter_box) {
        this.starter_box = starter_box;
    }

    public void setTop_fonts(BrandingTitleModel top_fonts) {
        this.top_fonts = top_fonts;
    }
}
