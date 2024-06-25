package kore.botssdk.models;

public class BrandingColorsModel {
    String primary;
    String secondary;
    String primary_text;
    String secondary_text;
    boolean useColorPaletteOnly;

    public String getPrimary() {
        return primary;
    }

    public String getPrimary_text() {
        return primary_text;
    }

    public String getSecondary() {
        return secondary;
    }

    public String getSecondary_text() {
        return secondary_text;
    }

    public boolean isUseColorPaletteOnly() {
        return useColorPaletteOnly;
    }
}
