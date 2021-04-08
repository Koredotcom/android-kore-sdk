package kore.botssdk.models;

public class BrandingNewModel
{
    private BotOptionsModel hamburgermenu;
    private BrandingModel brandingwidgetdesktop;

    public void setHamburgermenu(BotOptionsModel hamburgermenu) {
        this.hamburgermenu = hamburgermenu;
    }

    public BotOptionsModel getHamburgermenu() {
        return hamburgermenu;
    }

    public void setBrandingwidgetdesktop(BrandingModel brandingwidgetdesktop) {
        this.brandingwidgetdesktop = brandingwidgetdesktop;
    }

    public BrandingModel getBrandingwidgetdesktop() {
        return brandingwidgetdesktop;
    }

}
