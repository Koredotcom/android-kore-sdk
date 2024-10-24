package kore.botssdk.viewmodels.footer;

import static kore.botssdk.activity.KaCaptureImageActivity.THUMBNAIL_FILE_PATH;
import static kore.botssdk.utils.BitmapUtils.getBufferSize;
import static kore.botssdk.utils.BitmapUtils.rotateIfNecessary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedOutputStream;
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
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.AsyncTasks;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.view.viewUtils.FileUtils;
import kore.botssdk.viewmodels.BaseViewModel;
import kore.botssdk.websocket.SocketWrapper;

@SuppressWarnings("UnKnownNullness")
public class BotFooterViewModel extends BaseViewModel<ComposeFooterUpdate> {
    WeakReference<Context> context;
    ComposeFooterUpdate chatView;
    final int compressQualityInt = 100;
    final String LOG_TAG = ComposeFooterFragment.class.getName();
    static long totalFileSize;

    public BotFooterViewModel(Context context, ComposeFooterUpdate chatView) {
        this.context = new WeakReference<>(context);
        this.chatView = chatView;
    }

    public void processFileUpload(String jwt, String fileName, String filePath, String extn, String mediaType, String thumbnailFilePath, String orientation) {
        KoreWorker.getInstance().addTask(new UploadBulkFile(fileName, filePath, "bearer " + jwt, SocketWrapper.getInstance(context.get()).getBotUserId(), "workflows", extn, getBufferSize(mediaType), new Messenger(messagesMediaUploadAcknowledgeHandler), thumbnailFilePath, "AT_" + System.currentTimeMillis(), context.get(), mediaType, SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
    }

    public void processVideoResponse(String jwt, Uri selectedImage) {
        String orientation;
        String fileName = null;
        String realPath = KaMediaUtils.getRealPath(context.get(), selectedImage);
        if (realPath != null) {
            if (realPath.length() > 0) {
                int startInd = realPath.lastIndexOf(File.separator) + 1;
                int endInd = realPath.indexOf(".", startInd);
                fileName = realPath.substring(startInd, endInd);
            }

            Bitmap thumbnail;
            String extn = realPath.substring(realPath.lastIndexOf(".") + 1);
            thumbnail = BitmapFactory.decodeResource(context.get().getResources(), R.mipmap.videoplaceholder_left);

            Bitmap hover = BitmapFactory.decodeResource(context.get().getResources(), R.mipmap.btn_video_play_irc);
            thumbnail = overlay(thumbnail, hover);
            orientation = thumbnail.getWidth() > thumbnail.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
            String bmpPath = BitmapUtils.createImageThumbnailForBulk(thumbnail, realPath, compressQualityInt);
            processFileUpload(jwt , fileName, realPath, extn, BitmapUtils.obtainMediaTypeOfExtn(extn), bmpPath, orientation);
        } else {
            try {
                DocumentFile pickFile = DocumentFile.fromSingleUri(context.get(), selectedImage);
                String name = null;
                String type = null;

                if (pickFile != null) {
                    name = pickFile.getName();
                    type = pickFile.getType();
                }

                if (type != null && type.contains("video")) {
                    KaMediaUtils.setupAppDir(context.get(), BundleConstants.MEDIA_TYPE_VIDEO);
                    String filePath = KaMediaUtils.getAppDir() + File.separator + name;
                    new SaveVideoTask(jwt, filePath, name, selectedImage, context.get()).executeAsync();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fOut = Files.newOutputStream(_file.toPath());
                    } else fOut = new FileOutputStream(_file);

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

    private class SaveVideoTask extends AsyncTasks<String, String, String> {

        private final String filePath;
        private String fileName;
        private final Uri fileUri;
        private final WeakReference<Context> mContext;
        private final String jwt;

        SaveVideoTask(String jwt, String filePath, String fileName, Uri fileUri, Context mContext) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.fileUri = fileUri;
            this.mContext = new WeakReference<>(mContext);
            this.jwt = jwt;
        }


        @Override
        protected void doInBackground(String... params) {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);
            if (filePath != null && mContext.get() != null) {
                FileOutputStream fOut = null;
                BufferedOutputStream out = null;
                InputStream in = null;

                try {

                    fOut = new FileOutputStream(filePath);
                    out = new BufferedOutputStream(fOut);
                    in = mContext.get().getContentResolver().openInputStream(fileUri);
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in != null ? in.read(buffer) : 0) >= 0) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, e.toString());
                } finally {
                    if (fOut != null) {
                        try {
                            fOut.getFD().sync();
                            fOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        if (out != null) out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (in != null) in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        @Override
        protected void onPostExecute() {
//            filePath = path;
            if (filePath != null && mContext.get() != null) {
                if (filePath.length() > 0) {
                    int startInd = filePath.lastIndexOf(File.separator) + 1;
                    int endInd = filePath.indexOf(".", startInd);
                    fileName = filePath.substring(startInd, endInd);
                }
                String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                Bitmap hover = BitmapFactory.decodeResource(context.get().getResources(), R.mipmap.btn_video_play_irc);
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                if (thumbnail == null) {
                    thumbnail = BitmapFactory.decodeResource(context.get().getResources(), R.mipmap.videoplaceholder_left);

                }
                thumbnail = overlay(thumbnail, hover);
                String orientation = thumbnail.getWidth() > thumbnail.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
                String bmpPath = BitmapUtils.createImageThumbnailForBulk(thumbnail, filePath, compressQualityInt);
                processFileUpload(jwt, fileName, filePath, extn, BitmapUtils.obtainMediaTypeOfExtn(extn), bmpPath, orientation);
            }
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
}
