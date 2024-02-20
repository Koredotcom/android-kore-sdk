package kore.botssdk.models;

public class BrandingHeaderButtonsModel {
    private BrandingIconModel close;
    private BrandingIconModel minimise;
    private BrandingIconModel expand;
    private BrandingIconModel reconnect;
    private BrandingIconModel help;
    private BrandingIconModel live_agent;

    public BrandingIconModel getClose() {
        return close;
    }

    public BrandingIconModel getExpand() {
        return expand;
    }

    public BrandingIconModel getHelp() {
        return help;
    }

    public BrandingIconModel getLive_agent() {
        return live_agent;
    }

    public BrandingIconModel getMinimise() {
        return minimise;
    }

    public BrandingIconModel getReconnect() {
        return reconnect;
    }

    public void setLive_agent(BrandingIconModel live_agent) {
        this.live_agent = live_agent;
    }

    public void setMinimise(BrandingIconModel minimise) {
        this.minimise = minimise;
    }

    public void setClose(BrandingIconModel close) {
        this.close = close;
    }

    public void setExpand(BrandingIconModel expand) {
        this.expand = expand;
    }

    public void setHelp(BrandingIconModel help) {
        this.help = help;
    }

    public void setReconnect(BrandingIconModel reconnect) {
        this.reconnect = reconnect;
    }
}
