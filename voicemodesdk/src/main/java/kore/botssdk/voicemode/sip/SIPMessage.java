package kore.botssdk.voicemode.sip;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a SIP message for WebSocket-based SIP signaling.
 * Used for REGISTER, INVITE, ACK, BYE, and other SIP methods.
 */
public class SIPMessage {

    public enum Method {
        REGISTER,
        INVITE,
        ACK,
        BYE,
        CANCEL,
        OPTIONS,
        INFO,
        UPDATE,
        PRACK,
        MESSAGE
    }

    public enum Type {
        REQUEST,
        RESPONSE
    }

    private Type type;
    private Method method;
    private int statusCode;
    private String statusText;
    private String callId;
    private String fromUri;
    private String toUri;
    private String fromTag;
    private String toTag;
    private int cseq;
    private String via;
    private String contact;
    private String contentType;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public SIPMessage() {
        this.callId = UUID.randomUUID().toString();
    }

    public static SIPMessage createRegister(String uri, String domain, String callId, int cseq) {
        SIPMessage message = new SIPMessage();
        message.type = Type.REQUEST;
        message.method = Method.REGISTER;
        message.callId = callId;
        message.fromUri = uri;
        message.toUri = uri;
        message.fromTag = generateTag();
        message.cseq = cseq;
        return message;
    }

    public static SIPMessage createInvite(String fromUri, String toUri, String sdp) {
        SIPMessage message = new SIPMessage();
        message.type = Type.REQUEST;
        message.method = Method.INVITE;
        message.fromUri = fromUri;
        message.toUri = toUri;
        message.fromTag = generateTag();
        message.contentType = "application/sdp";
        message.body = sdp;
        return message;
    }

    public static SIPMessage createAck(String callId, String fromUri, String toUri, int cseq, String fromTag, String toTag) {
        SIPMessage message = new SIPMessage();
        message.type = Type.REQUEST;
        message.method = Method.ACK;
        message.callId = callId;
        message.fromUri = fromUri;
        message.toUri = toUri;
        message.fromTag = fromTag;
        message.toTag = toTag;
        message.cseq = cseq;
        return message;
    }

    public static SIPMessage createBye(String callId, String fromUri, String toUri, int cseq, String fromTag, String toTag) {
        SIPMessage message = new SIPMessage();
        message.type = Type.REQUEST;
        message.method = Method.BYE;
        message.callId = callId;
        message.fromUri = fromUri;
        message.toUri = toUri;
        message.fromTag = fromTag;
        message.toTag = toTag;
        message.cseq = cseq;
        return message;
    }

    public static SIPMessage createResponse(int statusCode, String statusText, SIPMessage request) {
        SIPMessage message = new SIPMessage();
        message.type = Type.RESPONSE;
        message.statusCode = statusCode;
        message.statusText = statusText;
        message.callId = request.getCallId();
        message.fromUri = request.getFromUri();
        message.toUri = request.getToUri();
        message.fromTag = request.getFromTag();
        message.cseq = request.getCseq();
        message.method = request.getMethod();
        return message;
    }

    private static String generateTag() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getFromUri() {
        return fromUri;
    }

    public void setFromUri(String fromUri) {
        this.fromUri = fromUri;
    }

    public String getToUri() {
        return toUri;
    }

    public void setToUri(String toUri) {
        this.toUri = toUri;
    }

    public String getFromTag() {
        return fromTag;
    }

    public void setFromTag(String fromTag) {
        this.fromTag = fromTag;
    }

    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public int getCseq() {
        return cseq;
    }

    public void setCseq(int cseq) {
        this.cseq = cseq;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public boolean isSuccess() {
        return type == Type.RESPONSE && statusCode >= 200 && statusCode < 300;
    }

    public boolean isRinging() {
        return type == Type.RESPONSE && (statusCode == 180 || statusCode == 183);
    }

    public boolean isError() {
        return type == Type.RESPONSE && statusCode >= 400;
    }
}
