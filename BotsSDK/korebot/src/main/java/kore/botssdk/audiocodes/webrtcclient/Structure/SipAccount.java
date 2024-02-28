package kore.botssdk.audiocodes.webrtcclient.Structure;

import com.audiocodes.mv.webrtcsdk.sip.enums.Transport;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.General.BasePrefs;

public class SipAccount {

    private String proxy;
    private int port;
    private String domain;
    private Transport transport;
    private String username;
    private String password;
    private String displayName;

    public SipAccount() {
        proxy = BasePrefs.getString("SipAccountProxy");
        domain = BasePrefs.getString("SipAccountDomain");
        username = BasePrefs.getString("SipAccountUsername");
        password = BotApplication.getGlobalContext().getString(R.string.sip_account_password_default);
        displayName = BasePrefs.getString("SipAccountDisplayName");
        String portStr = BotApplication.getGlobalContext().getString(R.string.sip_account_port_default);
        port = Integer.parseInt(portStr);
        String transportStr =  BotApplication.getGlobalContext().getString(R.string.sip_account_transport_default);
        transport = Transport.valueOf(transportStr);
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "SipAccount{" +
                "proxy='" + proxy + '\'' +
                ", port=" + port +
                ", domain='" + domain + '\'' +
                ", transport=" + transport +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
