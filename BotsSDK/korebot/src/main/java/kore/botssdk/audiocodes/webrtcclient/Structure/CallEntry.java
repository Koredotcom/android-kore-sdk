package kore.botssdk.audiocodes.webrtcclient.Structure;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallEntry {

    private int id;
    private long startTime;
    private long duration;
    private String contactName;
    private String contactNumber;
    private CallType callType;

    //Call types with int value for DB usage
    public enum CallType {
        INCOMING(1), OUTGOING(2), MISSED(3);

        private final int value;
        CallType(int value)
        {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }


    public CallEntry() {

    }

    public CallEntry(long startTime, long duration, String contactName, String contactNumber, CallType callType) {
        this.startTime = startTime;
        this.duration = duration;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.callType = callType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Return time as simple string from milliseconds
    public String getTimeAsString() {

        Date date = new Date(startTime);
        DateFormat formatter = new SimpleDateFormat("dd MMMM YYYY HH:mm:ss");
        return formatter.format(date);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getTypeAsInt() {
        return callType.getValue();
    }

    public void setType(int callType) {
        if (callType <= CallType.values().length)
            this.callType = CallType.values()[callType - 1];
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

//    public String getCallTypeAsString() {
//
//        switch(callType) {
//            case INCOMING:
//                return  "Incoming";
//
//            case OUTGOING:
//                return "Outgoing";
//
//            case MISSED:
//                return "Not Answered";
//
//            default :
//                return "";
//        }
//    }

}
