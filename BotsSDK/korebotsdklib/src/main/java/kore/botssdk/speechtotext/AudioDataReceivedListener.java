
package kore.botssdk.speechtotext;

/**

 */
public interface AudioDataReceivedListener {
    void onAudioDataToWave();
    void onAudioDataToServer(byte[] data);
    void onRecordingStarted();
    void onRecordingStopped();
}