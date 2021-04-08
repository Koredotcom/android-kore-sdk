package kore.botssdk.speech;

public class GoogleVoiceTypingDisabledException extends Exception{
    public GoogleVoiceTypingDisabledException() {
        super("Google voice typing must be enabled");
    }
}
