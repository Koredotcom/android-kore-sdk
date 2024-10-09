package com.kore.ui.audiocodes.webrtcclient.structure

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class CallEntry {
    var id = 0
    var startTime: Long = 0
    var duration: Long = 0
    var contactName: String? = null
    lateinit var contactNumber: String

    //    public String getCallTypeAsString() {
    var callType: CallType? = null

    //Call types with int value for DB usage
    enum class CallType(val value: Int) {
        INCOMING(1),
        OUTGOING(2),
        MISSED(3)

    }

    constructor()
    constructor(startTime: Long, duration: Long, contactName: String?, contactNumber: String, callType: CallType?) {
        this.startTime = startTime
        this.duration = duration
        this.contactName = contactName
        this.contactNumber = contactNumber
        this.callType = callType
    }

    val timeAsString: String
        //Return time as simple string from milliseconds
        get() {
            val date = Date(startTime)
            val formatter: DateFormat = SimpleDateFormat("dd MMMM YYYY HH:mm:ss")
            return formatter.format(date)
        }
    val typeAsInt: Int
        get() = callType!!.value

    fun setType(callType: Int) {
        if (callType <= CallType.values().size) this.callType = CallType.values()[callType - 1]
    }
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
