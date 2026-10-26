/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.webrtc.RTCStats
 */
package com.korevg.mv.webrtcsdk.session;

import java.util.HashMap;
import java.util.Map;
import org.webrtc.RTCStats;

public class ACCallStatistics {
    public RTPInboundStatistics rtpInboundStats = new RTPInboundStatistics();
    public RTPOutboundStatistics rtpOutboundStats = new RTPOutboundStatistics();
    public Map<String, RTCStats> fullStats = new HashMap<String, RTCStats>();

    public class RTPOutboundStatistics {
        public int packetsSent;
        public int bytesSent;
    }

    public class RTPInboundStatistics {
        public int packetsReceived;
        public int bytesReceived;
        public int packetsLost;
        public float jitter;
        public float fractionLost;
    }
}

