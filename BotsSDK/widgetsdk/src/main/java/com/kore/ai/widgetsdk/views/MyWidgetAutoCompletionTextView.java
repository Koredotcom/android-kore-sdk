package com.kore.ai.widgetsdk.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import com.kore.ai.widgetsdk.listeners.ActionListener;


public class MyWidgetAutoCompletionTextView extends AppCompatMultiAutoCompleteTextView {


    public MyWidgetAutoCompletionTextView(Context context) {
        super(context);
    }

    public MyWidgetAutoCompletionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWidgetAutoCompletionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ActionListener _listener;

    public ActionListener getListener() {
        return _listener;
    }

    public void setListener(ActionListener _listener) {
        this._listener = _listener;
    }


    private CharSequence mText;
    private int mIndex;
    private long mDelay = 150; // in ms

    private final Handler mHandler = new Handler();
    private final Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);

                if(mIndex == mText.length()){
                    if(_listener != null){
                        _listener.onActionCompleted(1);
                    }
                }
            }
        }
    };
    public void animateText(CharSequence txt) {
        mText = txt;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }
    public void setCharacterDelay(long m) {
        mDelay = m;
    }


}
