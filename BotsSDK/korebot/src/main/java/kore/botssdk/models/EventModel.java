package kore.botssdk.models;

public class EventModel
{
    private String type;
    private String from;
    private BotInfoModel botInfo;
    private String contextId;
    private String customEvent;
    private EventMessageModel message;
    private String traceId;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getType() {
        return type;
    }

    public BotInfoModel getBotInfo() {
        return botInfo;
    }

    public EventMessageModel getMessage() {
        return message;
    }

    public String getContextId() {
        return contextId;
    }

    public String getCustomEvent() {
        return customEvent;
    }

    public String getFrom() {
        return from;
    }
}
