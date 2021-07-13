package kore.botssdk.utils.markdown;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Base64;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;



public class MarkdownImageTagHandler implements Html.ImageGetter {

    private Context context;
    private TextView htmlTextViewRemote;
    private String htmlStringRemote;

    public MarkdownImageTagHandler(Context context, TextView htmlTextViewRemote, String htmlStringRemote) {
        this.context = context;
        this.htmlTextViewRemote = htmlTextViewRemote;
        this.htmlStringRemote = htmlStringRemote;
    }

    @Override
    public Drawable getDrawable(String source) {
        HttpGetDrawableTask httpGetDrawableTask = new HttpGetDrawableTask(context, htmlTextViewRemote, htmlStringRemote);
        httpGetDrawableTask.execute(source);
        return null;
    }
}

class HttpGetDrawableTask extends AsyncTask<String, Void, Drawable> {

    private Context context;
    private TextView taskTextView;
    private String taskHtmlString;
    private int dp1;

    HttpGetDrawableTask(Context context, TextView v, String s) {
        this.context = context;
        taskTextView = v;
        taskHtmlString = s;
        dp1 = (int)Resources.getSystem().getDisplayMetrics().density;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        Drawable drawable = null;
        URL sourceURL;
        String base64;
        try
        {
            if(params[0].contains("base64,"))
            {
                base64 = params[0].substring(params[0].indexOf(",") + 1);
                byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                // convert Bitmap to Drawable
                drawable = new BitmapDrawable(context.getResources(), bm);

                drawable.setBounds(0, 0, bm.getWidth(), bm.getHeight());
            }
            else
            {
                sourceURL = new URL(params[0]);
                URLConnection urlConnection = sourceURL.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(
                        inputStream);
                Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);

                // convert Bitmap to Drawable
                drawable = new BitmapDrawable(context.getResources(), bm);

                drawable.setBounds(0, 0, bm.getWidth(), bm.getHeight());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable result) {

        final Drawable taskDrawable = result;

        if (taskDrawable != null) {
            taskTextView.setText(Html.fromHtml(taskHtmlString,
                new Html.ImageGetter() {

                    @Override
                    public Drawable getDrawable(String source) {
                        return taskDrawable;
                    }
                }, null));
        }
    }
}
