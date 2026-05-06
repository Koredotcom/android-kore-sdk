package kore.botssdk.viewmodels.footer;

import static kore.botssdk.activity.KaCaptureImageActivity.THUMBNAIL_FILE_PATH;
import static kore.botssdk.utils.BitmapUtils.getBufferSize;
import static kore.botssdk.utils.BitmapUtils.rotateIfNecessary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.fileupload.core.KoreWorker;
import kore.botssdk.fileupload.core.UploadBulkFile;
import kore.botssdk.fileupload.core.UploadVideoFile;
import kore.botssdk.fragment.footer.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.view.viewUtils.FileUtils;
import kore.botssdk.viewmodels.BaseViewModel;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class BotFooterViewModel extends BaseViewModel<ComposeFooterUpdate> {
    private final WeakReference<Context> context;
    private final ComposeFooterUpdate chatView;
    private final int compressQualityInt = 100;
    private final String LOG_TAG = ComposeFooterFragment.class.getName();
    private static long totalFileSize;
    protected String jwt;

    public BotFooterViewModel(Context context, ComposeFooterUpdate chatView) {
        this.context = new WeakReference<>(context);
        this.chatView = chatView;
    }

    public void processFileUpload(String jwt, String fileName, String filePath, String extn, String mediaType, String thumbnailFilePath, String orientation) {
        KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + jwt, SocketWrapper.getInstance(context.get()).getBotUserId(), "workflows", extn, getBufferSize(mediaType), new Messenger(messagesMediaUploadAcknowledgeHandler), thumbnailFilePath, "AT_" + System.currentTimeMillis(), context.get(), mediaType, SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
    }

    public void processVideoResponse(String jwt, Uri selectedImage) {
        String fileName = getFileName(context.get(), selectedImage);
        String ext = getFileExtensionFromMime(context.get(), selectedImage);
        String thumbnail = loadThumbnailAndGetPath(context.get(), selectedImage, 800);
        processVideoFileUpload(jwt, fileName, selectedImage, ext, BitmapUtils.obtainMediaTypeOfExtn(ext), thumbnail, BitmapUtils.ORIENTATION_LS);
    }

    public static @Nullable String loadThumbnailAndGetPath(
            @NonNull Context context,
            @NonNull Uri uri,
            int sizePx
    ) {
        InputStream in = null;
        FileOutputStream out = null;

        try {
            Bitmap thumbnail;

            // Android 10+ (API 29+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                thumbnail = context.getContentResolver()
                        .loadThumbnail(uri, new Size(sizePx, sizePx), null);
            } else {
                // Fallback for older versions
                in = context.getContentResolver().openInputStream(uri);
                if (in == null) return null;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                thumbnail = BitmapFactory.decodeStream(in, null, options);
            }

            if (thumbnail == null)
                thumbnail = BitmapFactory.decodeResource(context.getResources(), R.drawable.videoplaceholder_left);

            File cacheDir = new File(context.getCacheDir(), "thumbnails");
            if (!cacheDir.exists()) cacheDir.mkdirs();

            String fileName = "thumb_" + System.currentTimeMillis() + ".jpg";
            File thumbFile = new File(cacheDir, fileName);

            out = new FileOutputStream(thumbFile);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 85, out);
            out.flush();

            return thumbFile.getAbsolutePath();

        } catch (Exception e) {
            Log.e("Thumbnail", "Failed to load/save thumbnail", e);
            return null;

        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException ignored) {}
        }
    }

    public static String getFileName(Context context, Uri uri) {
        try (Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    return cursor.getString(nameIndex);
                }
            }
        }
        return null;
    }

    public void processVideoFileUpload(String jwt, String fileName, Uri filePath, String ext, String mediaType, String thumbnailFilePath, String orientation) {
        KoreWorker.getInstance().addTask(new UploadVideoFile(
                fileName,
                filePath,
                "bearer " + jwt,
                SocketWrapper.getInstance(context.get()).getBotUserId(),
                "workflows",
                ext,
                getBufferSize(mediaType),
                new Messenger(messagesMediaUploadAcknowledgeHandler),
                thumbnailFilePath,
                "AT_" + System.currentTimeMillis(),
                context.get(),
                mediaType,
                SDKConfiguration.Server.SERVER_URL,
                orientation,
                true,
                SDKConfiguration.Client.isWebHook,
                SDKConfiguration.Client.bot_id)
        );
    }

    public static String getFileExtensionFromMime(Context context, Uri uri) {
        String mime = context.getContentResolver().getType(uri);
        if (mime != null) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mime);
        }
        return null;
    }

    public void processImageResponse(String jwt, Intent data) {
        String filePath = data.getStringExtra("filePath");
        String fileName;
        String filePathThumbnail;
        String orientation = null;
        if (filePath != null) {
            fileName = data.getStringExtra("fileName");
            filePathThumbnail = data.getStringExtra(THUMBNAIL_FILE_PATH);
            String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
            Bitmap thePic = BitmapUtils.decodeBitmapFromFile(filePath, 800, 600, false);
            OutputStream fOut = null;
            if (thePic != null) {
                try {
                    // compress the image
                    File _file = new File(filePath);

                    LogUtils.d(LOG_TAG, " file.exists() ---------------------------------------- " + _file.exists());
                    fOut = Files.newOutputStream(_file.toPath());

                    thePic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
                    thePic = rotateIfNecessary(filePath, thePic);
                    orientation = thePic.getWidth() > thePic.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
                    fOut.flush();
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, e.toString());
                } finally {
                    try {
                        assert fOut != null;
                        fOut.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + jwt, SocketWrapper.getInstance(context.get()).getBotUserId(), "workflows", extn, KoreMedia.BUFFER_SIZE_IMAGE, new Messenger(messagesMediaUploadAcknowledgeHandler), filePathThumbnail, "AT_" + System.currentTimeMillis(), context.get(), BitmapUtils.obtainMediaTypeOfExtn(extn), SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        } else {
            ToastUtils.showToast(context.get(), "Unable to attach file!");
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler messagesMediaUploadAcknowledgeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle reply = msg.getData();

            if (reply.getBoolean(UploadBulkFile.isFileSizeMore_key, false)) {
                chatView.showFreemiumDialog();
                return;
            }

            if (reply.getBoolean("success", true)) {
                long fileSizeBytes = reply.getLong(UploadBulkFile.fileSizeBytes_key);
                totalFileSize = totalFileSize + fileSizeBytes;
                String mediaFilePath = reply.getString("filePath");
                String MEDIA_TYPE = reply.getString("fileExtn");
                String mediaFileId = reply.getString("fileId");
                String mediaFileName = reply.getString("fileName");
                String componentType = reply.getString("componentType");
                String thumbnailURL = reply.getString("thumbnailURL");
                String fileSize = reply.getString("fileSize");
                KoreComponentModel koreMedia = new KoreComponentModel();
                koreMedia.setMediaType(BitmapUtils.getAttachmentType(componentType));

                HashMap<String, Object> cmpData = new HashMap<>(1);
                cmpData.put("fileName", mediaFileName);
                koreMedia.setComponentData(cmpData);

                if (componentType != null) koreMedia.setMediaFileName(getComponentId(componentType));

                koreMedia.setMediaFilePath(mediaFilePath);
                koreMedia.setFileSize(fileSize);

                koreMedia.setMediafileId(mediaFileId);
                koreMedia.setMediaThumbnail(thumbnailURL);

                HashMap<String, String> attachmentKey = new HashMap<>();
                String keyExtension = FileUtils.VideoTypes().contains(MEDIA_TYPE) ? "." + MEDIA_TYPE : "";
                attachmentKey.put("fileName", mediaFileName + keyExtension);
                attachmentKey.put("fileType", componentType);
                attachmentKey.put("fileId", mediaFileId);
                attachmentKey.put("localFilePath", mediaFilePath);
                attachmentKey.put("fileExtn", MEDIA_TYPE);
                attachmentKey.put("thumbnailURL", thumbnailURL);
                chatView.addAttachmentToAdapter(attachmentKey);
            } else {
                String errorMsg = reply.getString(UploadBulkFile.error_msz_key);
                if (!TextUtils.isEmpty(errorMsg)) {
                    ToastUtils.showToast(context.get(), errorMsg);
                }
            }
        }
    };

    String getComponentId(String componentType) {
        if (componentType.equalsIgnoreCase(BundleConstants.MEDIA_TYPE_IMAGE)) {
            return "image_" + System.currentTimeMillis();
        } else if (componentType.equalsIgnoreCase(BundleConstants.MEDIA_TYPE_VIDEO)) {
            return "video_" + System.currentTimeMillis();
        } else {
            return "doc_" + System.currentTimeMillis();
        }
    }

    public Bitmap overlay(Bitmap bitmap1, Bitmap bitmap2) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = bitmap2.getWidth();
        int bitmap2Height = bitmap2.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, marginLeft, marginTop, null);
        return overlayBitmap;
    }

    public void setJwtToken(String jwt) {
        this.jwt = jwt;
    }
}
