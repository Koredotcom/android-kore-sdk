package kore.botssdk.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

import kore.botssdk.R;

public class DownloadUtils {
    public static void downloadFile(Context context, String downloadUrl, DownloadProgressListener listener) {
        ToastUtils.showToast(context, context.getString(R.string.downloading));
        Handler handler = new Handler(Looper.getMainLooper());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();

                String mimeType = connection.getContentType();
                String contentDisposition = connection.getHeaderField("Content-Disposition");
                connection.disconnect();

                handler.post(() -> download(context, downloadUrl, contentDisposition, mimeType, listener));
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    ToastUtils.showToast(context, context.getString(R.string.downloading_failed));
                    if (listener != null) listener.onProgress(-1);
                });
            }
        });
    }

    private static void download(Context context, String url, String contentDisposition, String mimeType, DownloadProgressListener listener) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        request.setTitle(fileName);
        request.setDescription(context.getString(R.string.downloading_file));

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "KoreMedia/" + fileName);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
        ToastUtils.showToast(context, context.getString(R.string.downloading_file_name, fileName), Toast.LENGTH_LONG);
        if (listener != null) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);

                    if (cursor != null && cursor.moveToFirst()) {
                        int bytesDownloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytesTotal = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        if (bytesTotal > 0) {
                            int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                            listener.onProgress(progress);
                        }

                        int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            cursor.close();
                            return;
                        }
                        cursor.close();
                    }
                    handler.postDelayed(this, 500);
                }
            };

            handler.post(runnable);
        }
    }

    public interface DownloadProgressListener {
        void onProgress(int progress);
    }
}
