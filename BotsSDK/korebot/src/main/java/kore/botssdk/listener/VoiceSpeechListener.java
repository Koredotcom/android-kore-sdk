package kore.botssdk.listener;

public interface VoiceSpeechListener {

   void onSpeechStart();
   void onSpeechDone();
   void onSpeechError();

}
