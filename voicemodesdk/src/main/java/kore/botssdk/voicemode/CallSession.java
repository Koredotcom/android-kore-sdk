package kore.botssdk.voicemode;

import org.webrtc.AudioTrack;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.VideoTrack;

/**
 * Represents an active call session.
 * Provides methods to control the call (mute, unmute, hangup).
 */
public class CallSession {

    public enum State {
        IDLE,
        CONNECTING,
        RINGING,
        IN_PROGRESS,
        ENDED,
        FAILED
    }

    private String callId;
    private String remoteUri;
    private State state = State.IDLE;
    private PeerConnection peerConnection;
    private MediaStream localStream;
    private MediaStream remoteStream;
    private boolean isMuted = false;
    private boolean isOnHold = false;
    private boolean isOutgoing = true;
    private long startTime;
    private long endTime;

    private CallSessionListener listener;

    public CallSession(String callId, String remoteUri) {
        this.callId = callId;
        this.remoteUri = remoteUri;
    }

    public String getCallId() {
        return callId;
    }

    public String getRemoteUri() {
        return remoteUri;
    }

    public State getState() {
        return state;
    }

    void setState(State state) {
        this.state = state;
        if (state == State.IN_PROGRESS && startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if (state == State.ENDED || state == State.FAILED) {
            endTime = System.currentTimeMillis();
        }
    }

    public boolean isEnded() {
        return state == State.ENDED || state == State.FAILED;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public boolean isOnHold() {
        return isOnHold;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }

    void setOutgoing(boolean outgoing) {
        isOutgoing = outgoing;
    }

    public long getDurationMillis() {
        if (startTime == 0) {
            return 0;
        }
        if (endTime != 0) {
            return endTime - startTime;
        }
        return System.currentTimeMillis() - startTime;
    }

    void setPeerConnection(PeerConnection peerConnection) {
        this.peerConnection = peerConnection;
    }

    PeerConnection getPeerConnection() {
        return peerConnection;
    }

    void setLocalStream(MediaStream localStream) {
        this.localStream = localStream;
    }

    public MediaStream getLocalStream() {
        return localStream;
    }

    void setRemoteStream(MediaStream remoteStream) {
        this.remoteStream = remoteStream;
    }

    public MediaStream getRemoteStream() {
        return remoteStream;
    }

    public void setListener(CallSessionListener listener) {
        this.listener = listener;
    }

    CallSessionListener getListener() {
        return listener;
    }

    /**
     * Mute the local audio.
     */
    public void mute() {
        if (localStream != null) {
            for (AudioTrack audioTrack : localStream.audioTracks) {
                audioTrack.setEnabled(false);
            }
            isMuted = true;
        }
    }

    /**
     * Unmute the local audio.
     */
    public void unmute() {
        if (localStream != null) {
            for (AudioTrack audioTrack : localStream.audioTracks) {
                audioTrack.setEnabled(true);
            }
            isMuted = false;
        }
    }

    /**
     * Toggle mute state.
     * @return true if now muted, false if unmuted
     */
    public boolean toggleMute() {
        if (isMuted) {
            unmute();
        } else {
            mute();
        }
        return isMuted;
    }

    /**
     * Enable/disable local video (if video call).
     * @param enabled true to enable video
     */
    public void setVideoEnabled(boolean enabled) {
        if (localStream != null) {
            for (VideoTrack videoTrack : localStream.videoTracks) {
                videoTrack.setEnabled(enabled);
            }
        }
    }

    /**
     * Listener interface for call session events.
     */
    public interface CallSessionListener {
        void onStateChanged(CallSession session, State newState);
        void onRemoteStreamAdded(CallSession session, MediaStream stream);
        void onRemoteStreamRemoved(CallSession session, MediaStream stream);
    }
}
