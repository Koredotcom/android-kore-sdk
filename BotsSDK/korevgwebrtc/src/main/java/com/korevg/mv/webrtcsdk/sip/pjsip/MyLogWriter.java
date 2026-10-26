/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.sip.pjsip;

import com.korevg.mv.webrtcsdk.log.Log;
import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;

public class MyLogWriter
extends LogWriter {
    public static final String TAG = "PJSIP";

    @Override
    public void write(LogEntry entry) {
        switch (entry.getLevel()) {
            case 6: {
                Log.v(TAG, "" + entry.getMsg());
                break;
            }
            case 5: {
                Log.d(TAG, "" + entry.getMsg());
                break;
            }
            case 4: {
                Log.i(TAG, "" + entry.getMsg());
                break;
            }
            case 3: {
                Log.w(TAG, "" + entry.getMsg());
                break;
            }
            case 2: {
                Log.e(TAG, "" + entry.getMsg());
                break;
            }
        }
    }
}

