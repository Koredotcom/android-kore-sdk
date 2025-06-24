package kore.botssdk.models;

import java.io.Serializable;

public class BrandingHeaderButtonsModel implements Serializable {
    private BrandingIconModel close;
    private BrandingIconModel minimise;
    private BrandingIconModel expand;
    private BrandingIconModel reconnect;
    private BrandingIconModel help;
    private BrandingIconModel live_agent;

    public BrandingHeaderButtonsModel updateWith(BrandingHeaderButtonsModel configModel) {
        close = configModel.close != null && close != null ? close.updateWith(configModel.close) : close;
        minimise = configModel.minimise != null && minimise != null ? minimise.updateWith(configModel.minimise) : minimise;
        expand = configModel.expand != null && expand != null ? expand.updateWith(configModel.expand) : expand;
        reconnect = configModel.reconnect != null && reconnect != null ? reconnect.updateWith(configModel.reconnect) : reconnect;
        help = configModel.help != null && help != null ? help.updateWith(configModel.help) : help;
        live_agent = configModel.live_agent != null && live_agent != null ? live_agent.updateWith(configModel.live_agent) : live_agent;
        return this;
    }

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
