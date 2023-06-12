package kore.botssdk.models;

import java.net.URI;

/**
 * Created by Ramachandra Pradeep on 15-May-19.
 */
public class BotSocketOptions {
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String protocol;
    private int port = -1;
    private String host;

    public BotSocketOptions(String protocol, int port, String host){
        this.protocol = protocol;
        this.port = port;
        this.host = host;
    }

    public String replaceOptions(String url, BotSocketOptions options){
        try {
            URI _url = new URI(url);
            if(options != null){
                URI uri = new URI(!isNullOrEmpty(options.getProtocol())?options.getProtocol(): _url.getScheme(),
                        _url.getUserInfo(),
                        !isNullOrEmpty(options.getHost())?options.getHost():_url.getHost(),
                        options.getPort()!=-1?options.getPort():_url.getPort(), _url.getPath(), _url.getQuery(), null);
                return uri.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
