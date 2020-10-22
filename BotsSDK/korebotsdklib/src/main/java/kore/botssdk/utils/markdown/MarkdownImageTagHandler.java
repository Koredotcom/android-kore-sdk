package kore.botssdk.utils.markdown;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
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
    MarkDownImageClick markDownImageClick;
    public MarkdownImageTagHandler(Context context, TextView htmlTextViewRemote, String htmlStringRemote,MarkDownImageClick markDownImageClick) {
        this.context = context;
        this.htmlTextViewRemote = htmlTextViewRemote;
        this.htmlStringRemote = htmlStringRemote;
        this.markDownImageClick=markDownImageClick;
    }

    @Override
    public Drawable getDrawable(String source) {
        HttpGetDrawableTask httpGetDrawableTask = new HttpGetDrawableTask(context, htmlTextViewRemote, htmlStringRemote,markDownImageClick);
        httpGetDrawableTask.execute(source);
        return null;
    }
}

class HttpGetDrawableTask extends AsyncTask<String, Void, Drawable> {

    private Context context;
    private TextView taskTextView;
    private String taskHtmlString;
    MarkDownImageClick markDownImageClick;
    String url;
    HttpGetDrawableTask(Context context, TextView v, String s,MarkDownImageClick markDownImageClick) {
        this.context = context;
        taskTextView = v;
        taskHtmlString = s;
        this.markDownImageClick=markDownImageClick;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        Drawable drawable = null;
        URL sourceURL;
        try {
            sourceURL = new URL(params[0]);
            url=params[0];
            URLConnection urlConnection = sourceURL.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    inputStream);
            Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);

            // convert Bitmap to Drawable
            drawable = new BitmapDrawable(context.getResources(), bm);
if(bm !=null) {
    drawable.setBounds(0, 0, bm.getWidth(), bm.getHeight());
}
        } catch (IOException e) {
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


            if(markDownImageClick!=null)
            {

                      //  markDownImageClick.imageClicked(url);


            }
        }


    }

}
