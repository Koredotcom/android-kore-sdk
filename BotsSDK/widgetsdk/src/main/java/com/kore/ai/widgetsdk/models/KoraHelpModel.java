package com.kore.ai.widgetsdk.models;

public class KoraHelpModel {
    public FeatureUtteranceModel getHelp() {
        return help;
    }

    public void setHelp(FeatureUtteranceModel help) {
        this.help = help;
    }

    public KoraSummaryHelpModel getQuickHelp() {
        return quickHelp;
    }

    public void setQuickHelp(KoraSummaryHelpModel quickHelp) {
        this.quickHelp = quickHelp;
    }

    private FeatureUtteranceModel help;
    private KoraSummaryHelpModel quickHelp;
}
