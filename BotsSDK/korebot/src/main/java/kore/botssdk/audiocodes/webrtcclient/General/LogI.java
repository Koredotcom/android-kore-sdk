package kore.botssdk.audiocodes.webrtcclient.General;

import com.audiocodes.mv.webrtcsdk.log.ILog;

public class LogI implements ILog
{
    @Override
    public int v(String s, String s1) {
        return Log.v(s, s1);
    }

    @Override
    public int v(String s, String s1, Throwable throwable) {
        return Log.v(s, s1);
    }

    @Override
    public int d(String s, String s1) {
        return Log.d(s, s1);
    }

    @Override
    public int d(String s, String s1, Throwable throwable) {
        return Log.d(s, s1);
    }

    @Override
    public int i(String s, String s1) {
        return Log.i(s, s1);
    }

    @Override
    public int i(String s, String s1, Throwable throwable) {
        return Log.i(s, s1);
    }

    @Override
    public int w(String s, String s1) {
        return Log.w(s, s1);
    }

    @Override
    public int w(String s, String s1, Throwable throwable) {
        return Log.w(s, s1);
    }

    @Override
    public int e(String s, String s1) {
        return Log.e(s, s1);
    }

    @Override
    public int e(String s, String s1, Throwable throwable) {
        return Log.e(s, s1);
    }
}

