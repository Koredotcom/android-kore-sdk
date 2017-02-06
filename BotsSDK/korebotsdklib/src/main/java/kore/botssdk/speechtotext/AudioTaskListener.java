package kore.botssdk.speechtotext;

/**
 * Created by Ramachandra Pradeep on 1/24/2017.
 */
public interface AudioTaskListener {
    void onCloseButtonClicked(int resultCode);
    void audioDataToTextView(String text);
}
