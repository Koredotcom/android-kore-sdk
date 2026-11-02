package kore.botssdk.models;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.List;

public class EventMessageModel {
    private String type;
    private String conversationId;
    private String sipURI;
    private String sipUser;
    private boolean videoCall;
    private ArrayList<String> addresses;
    private String domain;
    private List<EventIceServersModel> iceServers;
    private boolean restoreCall;
    private boolean screenShare;
    private String firstName;
    private String lastName;
    private String profileIcon;
    private String iId;
    private String aId;
    private String traceId;
    private String event;
    private String timestamp;
    private String productName;

    public boolean isVideoCall() {
        return videoCall;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public List<EventIceServersModel> getIceServers() {
        return iceServers;
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getaId() {
        return aId;
    }

    public String getDomain() {
        return domain;
    }

    public String getEvent() {
        return event;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getiId() {
        return iId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProfileIcon() {
        return profileIcon;
    }

    public String getSipURI() {
        return sipURI;
    }

    public String getSipUser() {
        return sipUser;
    }

    public ArrayList<PeerConnection.IceServer> getIceServer() {
        ArrayList<PeerConnection.IceServer> result = new ArrayList<>();
        if (iceServers == null) return result;

        for (EventIceServersModel eventIceServersModel : iceServers) {
            if (eventIceServersModel == null) continue;
            String username = eventIceServersModel.getUsername() != null ? eventIceServersModel.getUsername() : "";
            String credential = eventIceServersModel.getCredential() != null ? eventIceServersModel.getCredential() : "";
            List<String> urls = eventIceServersModel.getIceUrls();
            if (urls.isEmpty()) continue;

            // This hostname is the optional TURN/TLS certificate hostname, not the SIP domain.
            // Passing the SIP domain here makes WebRTC reject DNS-based ICE URLs.
            PeerConnection.IceServer.Builder builder = PeerConnection.IceServer.builder(urls)
                    .setUsername(username)
                    .setPassword(credential)
                    .setTlsCertPolicy(PeerConnection.TlsCertPolicy.TLS_CERT_POLICY_INSECURE_NO_CHECK)
                    .setHostname("");
            result.add(builder.createIceServer());
        }
        return result;
    }
}

