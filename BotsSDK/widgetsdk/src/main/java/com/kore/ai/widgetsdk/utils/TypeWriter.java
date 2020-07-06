
package com.kore.ai.widgetsdk.utils;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.kore.ai.widgetsdk.listeners.ActionListener;

public class TypeWriter extends AppCompatTextView {
    private CharSequence mText;
    private int mIndex;
    private long mDelay = 150; // in ms

    private ActionListener _listener;

    public ActionListener getListener() {
        return _listener;
    }

    public void setListener(ActionListener _listener) {
        this._listener = _listener;
    }

    public TypeWriter(Context context) {
        super(context);
    }
    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);

                if(mIndex == mText.length()){
                    if(_listener != null){
                        _listener.onActionCompleted(0);
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
