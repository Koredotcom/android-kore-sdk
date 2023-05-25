package jp.wasabeef.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kore.ai.widgetsdk.utils.NetworkUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Copyright (C) 2017 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RichEditor extends WebView {

    public enum Type {
        BOLD,
        ITALIC,
        SUBSCRIPT,
        SUPERSCRIPT,
        STRIKETHROUGH,
        UNDERLINE,
        H1,
        H2,
        H3,
        H4,
        H5,
        H6,
        ORDEREDLIST1,
        UNORDEREDLIST,
        JUSTIFYCENTER,
        JUSTIFYFULL,
        JUSTUFYLEFT,
        JUSTIFYRIGHT
    }

    public interface OnTextChangeListener {

        void onTextChange(String text);
    }

    public interface OnDecorationStateListener {

        void onStateChangeListener(String text, List<Type> types);
    }

    public interface AfterInitialLoadListener {

        void onAfterInitialLoad(boolean isReady);
    }
    public interface WebActivityLauncher{
        void launchWebView(String url);
    }

    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    private static final String CALLBACK_SCHEME = "re-callback://";
    private static final String STATE_SCHEME = "re-state://";
    private boolean isReady = false;
    private String mContents;
    private OnTextChangeListener mTextChangeListener;
    private OnDecorationStateListener mDecorationStateListener;
    private AfterInitialLoadListener mLoadListener;

    public WebActivityLauncher getWebActivityLauncher() {
        return webActivityLauncher;
    }

    public void setWebActivityLauncher(WebActivityLauncher webActivityLauncher) {
        this.webActivityLauncher = webActivityLauncher;
    }

    private WebActivityLauncher webActivityLauncher;

    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
//        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("MyApplication", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );
                return true;
            }
        });
        setWebViewClient(new EditorWebViewClient());
        loadUrl(SETUP_HTML);
        applyAttributes(context, attrs);
    }

    protected EditorWebViewClient createWebviewClient() {
        return new EditorWebViewClient();
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        mTextChangeListener = listener;
    }

    public void setOnDecorationChangeListener(OnDecorationStateListener listener) {
        mDecorationStateListener = listener;
    }

    public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        mLoadListener = listener;
    }

    private void callback(String text) {
        mContents = text.replaceFirst(CALLBACK_SCHEME, "");
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChange(mContents);
        }
    }

    private void stateCheck(String text) {
        String state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH);
        List<Type> types = new ArrayList<>();
        for (Type type : Type.values()) {
            if (TextUtils.indexOf(state, type.name()) != -1) {
                types.add(type);
            }
        }

        if (mDecorationStateListener != null) {
            mDecorationStateListener.onStateChangeListener(state, types);
        }
    }




    private void applyAttributes(Context context, AttributeSet attrs) {
        final int[] attrsArray = new int[] {
                android.R.attr.gravity
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        int gravity = ta.getInt(0, NO_ID);
        switch (gravity) {
            case Gravity.LEFT:
                exec("javascript:RE.setTextAlign(\"left\")");
                break;
            case Gravity.RIGHT:
                exec("javascript:RE.setTextAlign(\"right\")");
                break;
            case Gravity.TOP:
                exec("javascript:RE.setVerticalAlign(\"top\")");
                break;
            case Gravity.BOTTOM:
                exec("javascript:RE.setVerticalAlign(\"bottom\")");
                break;
            case Gravity.CENTER_VERTICAL:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                break;
            case Gravity.CENTER_HORIZONTAL:
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
            case Gravity.CENTER:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
        }

        ta.recycle();
    }

    public void setHtml(String contents) {
        if (contents == null) {
            contents = "";
        }
        try {
            exec("javascript:RE.prepareInsert();");
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
        } catch (UnsupportedEncodingException e) {
            // No handling
        }
        mContents = contents;
    }

    public String getHtml() {
        return mContents;
    }

    public void setEditorFontColor(int color) {
        String hex = convertHexColorString(color);
        exec("javascript:RE.setBaseTextColor('" + hex + "');");
    }

    public void setEditorFontSize(int px) {
        exec("javascript:RE.setBaseFontSize('" + px + "px');");
    }

    public void scrollViewToBottom(){
        exec("javascript:RE.scrollViewToBottom(" + 500 + ","+50+");");
    }

    @Override public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    @Override public void setPaddingRelative(int start, int top, int end, int bottom) {
        // still not support RTL.
        setPadding(start, top, end, bottom);
    }

    public void setEditorBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    @Override public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override public void setBackgroundResource(int resid) {
        Bitmap bitmap = Utils.decodeResource(getContext(), resid);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    @Override public void setBackground(Drawable background) {
        Bitmap bitmap = Utils.toBitmap(background);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    public void setBackground(String url) {
        exec("javascript:RE.setBackgroundImage('url(" + url + ")');");
    }

    public void setEditorWidth(int px) {
        exec("javascript:RE.setWidth('" + px + "px');");
    }

    public void setEditorHeight(int px) {
        exec("javascript:RE.setHeight('" + px + "px');");
    }

    public void setEditorMinHeight(int px){
        exec("javascript:RE.setMinHeight('" + px + "px');");
    }

    public void setPlaceholder(String placeholder) {
        exec("javascript:RE.setPlaceholder('" + placeholder + "');");
    }

    public void setInputEnabled(Boolean inputEnabled) {
        exec("javascript:RE.setInputEnabled(" + inputEnabled + ")");
    }

    public void loadCSS(String cssFile) {
        String jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    public void undo() {
        exec("javascript:RE.undo();");
    }

    public void redo() {
        exec("javascript:RE.redo();");
    }

    public void setBold() {
        exec("javascript:RE.setBold();");
    }

    public void setItalic() {
        exec("javascript:RE.setItalic();");
    }

    public void setSubscript() {
        exec("javascript:RE.setSubscript();");
    }

    public void setSuperscript() {
        exec("javascript:RE.setSuperscript();");
    }

    public void setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();");
    }

    public void setUnderline() {
        exec("javascript:RE.setUnderline();");
    }

    public void setTextColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextColor('" + hex + "');");
    }

    public void setTextBackgroundColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
    }

    public void setFontSize(int fontSize) {
        if (fontSize > 7 || fontSize < 1) {
            Log.e("RichEditor", "Font size should have a value between 1-7");
        }
        exec("javascript:RE.setFontSize('" + fontSize + "');");
    }

    public void removeFormat() {
        exec("javascript:RE.removeFormat();");
    }

    public void setHeading(int heading) {
        exec("javascript:RE.setHeading('" + heading + "');");
    }

    public void setIndent() {
        exec("javascript:RE.setIndent();");
    }

    public void setOutdent() {
        exec("javascript:RE.setOutdent();");
    }

    public void setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();");
    }

    public void setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();");
    }

    public void setAlignRight() {
        exec("javascript:RE.setJustifyRight();");
    }

    public void setBlockquote() {
        exec("javascript:RE.setBlockquote();");
    }

    public void setBullets() {
        exec("javascript:RE.setBullets();");
    }

    public void setNumbers() {
        exec("javascript:RE.setNumbers();");
    }

    public void insertImage(String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImage('" + url + "', '" + alt + "');");
    }

    public void insertKoraImage(String fileId, int width, String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertKoraImage('" + fileId + "','" + width + "','" + url + "', '" + alt + "');");
    }

    public void insertKoraMediaFullWidth(String fileId, String type, String compId, String orientation, String url, String alt){
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertKoraMediaFullWidth('" + fileId + "','" + type + "','" + compId + "', '" + orientation + "', '" + url + "', '" + alt + "');");
    }
    public void urlLinkUI(String response,String token){
//        response = StringEscapeUtils.escapeEcmaScript(response);
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.urlLinkUI('" + response + "','"+token+"');");
    }
    public void insertKoraDocument(String fileId, String filename, String componentSize, String componentId) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.getFilesThumbnail('" + fileId + "','" + filename + "', '" + componentSize + "','" + componentId + "');");
    }

    public void insertLink(String href, String title) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertLink('" + href + "', '" + title + "');");
    }

    public void insertHTML(String text) {
//        text = StringEscapeUtils.escapeEcmaScript(text);
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertHTML('" + text + "');");
    }

    public void getImageUrlAndTitle(){
        exec("javascript:RE.setImageUrlAndTitle();");
    }

    public void insertText(String text) {
//        text = StringEscapeUtils.escapeEcmaScript(text);
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertText('" + text + "');");
    }
    public void getAttachmentsCount(){
        exec("javascript:RE.getAttachmentsCount();");
    }
    public void insertFilesHTML(String fileName,String fileSize,String extnPath) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertFilesHTML('" + fileName + "', '" + fileSize + "','" + extnPath + "');");
    }

    public void insertTodo() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setTodo('" + Utils.getCurrentTime() + "');");
    }

    public void focusEditor() {
        requestFocus();
        exec("javascript:RE.focus();");
    }

    public void clearFocusEditor() {
        exec("javascript:RE.blurFocus();");
    }

    private String convertHexColorString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    protected void exec(final String trigger) {
        if (isReady) {
            load(trigger);
        } else {
            postDelayed(new Runnable() {
                @Override public void run() {
                    exec(trigger);
                }
            }, 100);
        }
    }

    public void getSelectionText(){
        exec("javascript:RE.getSelectedText();");
    }

    private void load(String trigger) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
        } else {
            loadUrl(trigger);
        }
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if (inputConnection == null) {
            return null;
        }
        return new MyCustomInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    /*@Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_NULL;
        final InputConnection con = new MyCustomInputConnection(this,false);
        InputConnectionWrapper public_con = new InputConnectionWrapper(
                super.onCreateInputConnection(outAttrs), true) {
            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                if (beforeLength == 1 && afterLength == 0) {
                    return this.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                            && this.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                }
                return super.deleteSurroundingText(beforeLength, afterLength);
            }

            @Override
            public boolean sendKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    return con.sendKeyEvent(event);
                } else {
                    return super.sendKeyEvent(event);
                }
            }
        };
        try {
            return public_con ;
        }catch (Exception e){
            return super.onCreateInputConnection(outAttrs) ;
        }
    }*/

    protected final class EditorWebViewClient extends WebViewClient {
        @Override public void onPageFinished(WebView view, String url) {
            isReady = url.equalsIgnoreCase(SETUP_HTML);
            if (mLoadListener != null) {
                mLoadListener.onAfterInitialLoad(isReady);
            }
        }

        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String decode;
            try {
                decode = URLDecoder.decode(url, "UTF-8");
            } catch (Exception e) {
                // No handling
                decode = url;
            }

            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode);
                return true;
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode);
                return true;
            }else if(TextUtils.indexOf(url,"http") == 0 || TextUtils.indexOf(url,"www") == 0) {
                if(webActivityLauncher != null){
                    webActivityLauncher.launchWebView(url);
                }else {
                    if(NetworkUtility.isNetworkConnectionAvailable(getContext())) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        intent.putExtra("url", url);
//                        intent.putExtra("header", getResources().getString(R.string.app_name));
                        getContext().startActivity(browserIntent);
                    }else{
                        Toast.makeText(getContext(),"Check your internet connection and please try again",Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }
    public void runEditorState(){
        exec("javascript:RE.enabledEditingItems();");
    }
    public class MyCustomInputConnection extends InputConnectionWrapper {

        public MyCustomInputConnection(@NonNull InputConnection target, boolean fullEditor) {
            super(target, fullEditor);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (beforeLength == 1 && afterLength == 0) {
                // backspace
                return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }
}