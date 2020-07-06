package com.kore.ai.widgetsdk.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class  ExtendedEditText extends AppCompatEditText {
	
	private KeyImeChange keyImeChangeListener;
	
	public ExtendedEditText(Context context) {
		super(context, null);
    }

    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		 if(keyImeChangeListener != null){
	            keyImeChangeListener.onKeyIme(keyCode, event);
	            
	    return true;  // So it is not propagated.
	  }
	  return super.dispatchKeyEvent(event);
	}
	
	public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }
	
	public interface KeyImeChange {
        void onKeyIme(int keyCode, KeyEvent event);
    }
}   