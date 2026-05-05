package kore.botssdk.fileupload.core;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.HttpsURLConnection;

import kore.botssdk.fileupload.configurations.Constants;
import kore.botssdk.fileupload.configurations.FileUploadEndPoints;
import kore.botssdk.fileupload.listeners.ChunkUploadListener;
import kore.botssdk.fileupload.listeners.FileTokenListener;
import kore.botssdk.fileupload.listeners.FileUploadedListener;
import kore.botssdk.fileupload.managers.BotDBManager;
import kore.botssdk.fileupload.managers.FileTokenManager;
import kore.botssdk.fileupload.models.ChunkInfo;
import kore.botssdk.fileupload.models.FileUploadInfo;
import kore.botssdk.fileupload.models.MissingChunks;
import kore.botssdk.fileupload.models.UploadError;
import kore.botssdk.fileupload.services.KoreFileUploadServiceExecutor;
import kore.botssdk.fileupload.ssl.KoreHttpsUrlConnectionBuilder;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class UploadVideoFile implements Work, FileTokenListener, ChunkUploadListener {
    private static final String BOUNDARY = "*****";
    private static final String LINE_FEED = "\r\n";
    private static final String CHARSET = "UTF-8";
    private static final DecimalFormat df2 = new DecimalFormat("###.##");
    private final String LOG_TAG = getClass().getSimpleName();
    private final String fileName;
    private final Uri outFilePath;
    private final String accessToken;
    private final String userOrTeamId;
    private final String fileContext;
    private String fileToken = null;
    private final String fileExtn;
    private final String orientation;
    private final int BUFFER_SIZE;
    private final Messenger messenger;
    private final String thumbnailFilePath;
    private final String messageId;
    final Context context;
    private FileUploadedListener listener;
    private final String componentType;
    private int noOfMergeAttempts;
    private final String host;
    final BotDBManager helper;
    final FileUploadInfo uploadInfo = new FileUploadInfo();
    public static final long FILE_SIZE_20MB = 20 * 1024 * 1024;
    final ExecutorService executor = KoreFileUploadServiceExecutor.getInstance().getExecutor();
    ProgressDialog pDialog;
    private final boolean isAnonymousUser;
    private final boolean isWebHook;
    private final String botId;

    public UploadVideoFile(String fileName, Uri outFilePath, String accessToken, String userId, String fileContext,
                           String fileExtn, int BUFFER_SIZE, Messenger messenger,
                           String thumbnailFilePath, String messageId, Context context, String componentType,
                           String host, String orientation, boolean isAnonymousUser, boolean isWebHook, String botId) {
        this.fileName = fileName;
        this.outFilePath = outFilePath;
        this.accessToken = accessToken;
//		this.userId = userId;
        this.fileContext = fileContext;
        this.fileExtn = fileExtn;
        this.BUFFER_SIZE = BUFFER_SIZE;
        this.messenger = messenger;
        this.thumbnailFilePath = thumbnailFilePath;
        this.messageId = messageId;
        this.context = context;
//		this.isTeam = isTeam;
        this.componentType = componentType;
        this.host = host;
        this.orientation = orientation;
        this.isAnonymousUser = isAnonymousUser;
        this.isWebHook = isWebHook;
        this.botId = botId;
        userOrTeamId = userId;

        helper = BotDBManager.getInstance();
    }

    private void upLoadProgressState(final int progress, final boolean show) {
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = () -> {
            if (show) {
                if (pDialog == null) {
                    pDialog = new ProgressDialog(context);
                    pDialog.setCancelable(false);
                    pDialog.setIndeterminate(false);
                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setMax(100);
                }
                pDialog.setMessage("Uploading...");
                pDialog.setProgress(progress);
                if (!pDialog.isShowing()) {
                    pDialog.show();
                }
            } else {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        };
        LogUtils.d("progress", progress + " done");
        mainHandler.post(myRunnable);

    }

    @Override
    public synchronized void fileTokenReceivedSuccessfully(Hashtable<String, String> hsh) throws IOException {
        fileToken = hsh.get("fileToken");
        startUpload(outFilePath);
    }

    @Override
    public void fileTokenReceivedWithFailure(String errCode, String reason) {
        LogUtils.d(LOG_TAG, "file upload failed because of getting file token and the reason is " + reason);
        sendUploadFailedNotice(false);
    }

    private File copyUriToCache(@NonNull Uri uri) throws IOException {

        File cacheFile = new File(
                context.getCacheDir(),
                "upload_" + System.currentTimeMillis()
        );

        ParcelFileDescriptor pfd =
                context.getContentResolver().openFileDescriptor(uri, "r");

        if (pfd == null) {
            throw new FileNotFoundException("Unable to open file descriptor");
        }

        try (InputStream in = new FileInputStream(pfd.getFileDescriptor());
             OutputStream out = new FileOutputStream(cacheFile)) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } finally {
            pfd.close();
        }

        return cacheFile;
    }



    private synchronized void startUpload(@NonNull Uri fileUri) throws IOException {

        noOfMergeAttempts = 1;
        ByteArrayOutputStream chunkbaos = new ByteArrayOutputStream();

        int dataSizeRead;
        int totalBytesWritten = 0;
        byte[] buffer = new byte[BUFFER_SIZE];

        InputStream inputStream = null;
        File tempCacheFile = null;

        try {
            ContentResolver resolver = context.getContentResolver();

            try {
                // 🔹 Primary attempt (SAF)
                inputStream = resolver.openInputStream(fileUri);
            } catch (SecurityException se) {
                // 🔥 Downloads / OEM fallback
                tempCacheFile = copyUriToCache(fileUri);
                inputStream = new FileInputStream(tempCacheFile);
            }

            if (inputStream == null) {
                sendUploadFailedNotice(false);
                showToastMsg("File not available");
                return;
            }

            long fileSize = (tempCacheFile != null)
                    ? tempCacheFile.length()
                    : getFileSize(resolver, fileUri);

            if (fileSize <= 0) {
                sendUploadFailedNotice(false);
                showToastMsg("Unable to read file size");
                return;
            }

            if (fileSize > FILE_SIZE_20MB) {
                sendUploadFailedNotice(false);
                showToastMsg("File size can't be more than 20 MB!");
                return;
            }

            int chunkCount = (int) (fileSize / BUFFER_SIZE);
            if (fileSize % BUFFER_SIZE > 0) chunkCount++;
            setChunkCount(chunkCount);

            int chunkNo = 0;

            while ((dataSizeRead = inputStream.read(buffer)) > 0) {

                chunkbaos.write(buffer, 0, dataSizeRead);
                chunkbaos.flush();

                totalBytesWritten += dataSizeRead;

                ChunkInfo chunkInfo = new ChunkInfo();
                chunkInfo.setFileName(fileName);
                chunkInfo.setNumber(chunkNo);
                chunkInfo.setOffset(totalBytesWritten - dataSizeRead);
                chunkInfo.setSize(dataSizeRead);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    helper.getChunkInfoMap()
                            .computeIfAbsent(fileToken, k -> new HashMap<>())
                            .put(chunkNo, chunkInfo);
                }

                upLoadProgressState(0, true);

                executor.execute(
                        new UploadExecutor(
                                context,
                                fileName,
                                fileToken,
                                accessToken,
                                userOrTeamId,
                                chunkbaos.toByteArray(),
                                chunkNo,
                                this,
                                host,
                                isAnonymousUser,
                                isWebHook,
                                botId
                        )
                );

                chunkNo++;
                chunkbaos.reset();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showToastMsg("Unable to attach file");
            sendUploadFailedNotice(false);

        } finally {
            if (inputStream != null) inputStream.close();
            chunkbaos.close();

            // 🧹 Cleanup cache
            if (tempCacheFile != null && tempCacheFile.exists()) {
                tempCacheFile.delete();
            }
        }
    }



    private long getFileSize(ContentResolver resolver, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (sizeIndex != -1) {
                    return cursor.getLong(sizeIndex);
                }
            }
        }
        catch (Exception e){e.printStackTrace();}finally {
            if (cursor != null) cursor.close();
        }
        return -1;
    }

    private void showToastMsg(final String msg) {
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = () -> {
            Toast toastView = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();

        };
        mainHandler.post(myRunnable);
    }

    private synchronized void startUploadFailedChunks(ArrayList<String> failedChunkList) throws IOException {
        ByteArrayOutputStream chunkbaos = new ByteArrayOutputStream();

        int dataSizeRead = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream fis = null;

        try {
            fis = new FileInputStream("");
            if (fis.available() > 0) {
                int chunkNo = 0;
                while ((dataSizeRead = fis.read(buffer)) > 0) {
                    chunkbaos.write(buffer, 0, dataSizeRead);
                    chunkbaos.flush();
                    LogUtils.d(LOG_TAG, "6########################The chunk number is " + chunkNo);

                    if (failedChunkList.contains(chunkNo + "")) {
                        executor.execute(new UploadExecutor(context, fileName, fileToken, accessToken, userOrTeamId, chunkbaos.toByteArray(), chunkNo, this, host, isAnonymousUser, isWebHook, botId));
                    }
                    chunkNo++;
                    chunkbaos.reset(); /*chunk size doubles in next iteration*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (fis != null)
                fis.close();

            chunkbaos.close();
        }
    }

    @Override
    public synchronized void notifyChunkUploadCompleted(String chunkNo, String fileName) {
        try {
            uploadInfo.setUploadCount(uploadInfo.getUploadCount() + 1);
            upLoadProgressState(uploadInfo.getUploadCount() * 100 / uploadInfo.getTotalChunks(), true);
            ChunkInfo chInfo = Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).get(Integer.parseInt(chunkNo));
            assert chInfo != null;
            chInfo.setUploaded(true);
            Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).put(Integer.parseInt(chunkNo), chInfo);
            helper.getFileUploadInfoMap().put(fileToken, uploadInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (uploadInfo.getTotalChunks() == uploadInfo.getUploadCount()) {

            LogUtils.d(LOG_TAG, "Sending merge request" + fileName + "as no failed chunks in recorded");
            try {
                sendMergeSignal(uploadInfo.getTotalChunks(), fileContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initiateFileUpload(FileUploadedListener listener) {
        this.listener = listener;
        new FileTokenManager(host, this, accessToken.trim(), context, userOrTeamId, isAnonymousUser, isWebHook, botId);
    }

    void setChunkCount(int n) {
        uploadInfo.setTotalChunks(n);
        try {
            helper.getFileUploadInfoMap().put(fileToken, uploadInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StringFormatTrivial")
    private void sendMergeSignal(final int dataCount, String fileContext) throws IOException {
        LogUtils.d(LOG_TAG, "(((( re attempt count " + noOfMergeAttempts);
        if (noOfMergeAttempts > Constants.MERGE_RE_ATTEMPT_COUNT) {
            sendUploadFailedNotice(true);
            return;
        }
        noOfMergeAttempts++;
        String fileID = "";
        HttpsURLConnection httpsURLConnection = null;
        InputStream fis = null;
        ByteArrayOutputStream thumbBaos = null;
        BufferedReader input = null;
        InputStreamReader inputStreamReader = null;
        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;
        InputStream inputStream = null;

        try {
            KoreHttpsUrlConnectionBuilder koreHttpsUrlConnectionBuilder;
            if (isAnonymousUser) {
                if (!isWebHook)
                    koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, host + String.format(FileUploadEndPoints.ANONYMOUS_MERGE, userOrTeamId, fileToken));
                else
                    koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_MERGE, botId, fileToken));
            } else {
                if (!isWebHook)
                    koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, host + String.format(FileUploadEndPoints.MERGE_FILE, userOrTeamId, fileToken));
                else
                    koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_MERGE, botId, fileToken));
            }

            httpsURLConnection = koreHttpsUrlConnectionBuilder.getHttpsURLConnection();
            httpsURLConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setReadTimeout(Constants.CONNECTION_READ_TIMEOUT);
            httpsURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            httpsURLConnection.setRequestProperty("User-Agent", Constants.getUserAgent());
            httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpsURLConnection.setRequestProperty("Authorization", accessToken);
            httpsURLConnection.setRequestMethod("PUT");

            LogUtils.d(LOG_TAG, "M#####The chunk number is " + dataCount);

            LogUtils.d(LOG_TAG, "***********AccessToken in SendMergeSignal**********" + accessToken);

            outputStream = httpsURLConnection.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            addFormField(dataOutputStream, "totalChunks", String.valueOf(dataCount));

            addFormField(dataOutputStream, "totalChunks", String.valueOf(dataCount));
            addFormField(dataOutputStream, "fileExtension", String.valueOf(fileExtn));
            addFormField(dataOutputStream, "fileToken", String.valueOf(fileToken));
            if (!StringUtils.isNullOrEmpty(fileName))
                addFormField(dataOutputStream, "filename", fileName);

            if (fileContext != null) {
                LogUtils.d(LOG_TAG, "Adding fileContext  !!!");
                addFormField(dataOutputStream, "fileContext", fileContext);
            } else {
                LogUtils.d(LOG_TAG, "There is no fileContext !!");
            }
            if (thumbnailFilePath != null && !thumbnailFilePath.equalsIgnoreCase("") && Objects.requireNonNull(fileContext).equalsIgnoreCase("knowledge")) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    fis = Files.newInputStream(Paths.get(thumbnailFilePath));
                } else
                    fis = new FileInputStream(thumbnailFilePath);

                if (fis.available() > 0) {
                    thumbBaos = new ByteArrayOutputStream();
                    int startInd = thumbnailFilePath.lastIndexOf(File.separator) + 1;
                    int endInd = thumbnailFilePath.indexOf(".", startInd);
                    String thfileName = thumbnailFilePath.substring(startInd, endInd);

                    int dataRead;
                    byte[] buff = new byte[2 * 1024];
                    /*file size is less than BUFFER_SIZE..just send the file*/
                    while ((dataRead = fis.read(buff)) != -1) {
                        thumbBaos.write(buff, 0, dataRead);
                    }
                    addFormField(dataOutputStream, "thumbnailUpload", String.valueOf(true));
                    addFilePart(dataOutputStream, thumbBaos.toByteArray(), thfileName);
                    addFormField(dataOutputStream, "thumbnailExtension", "png");
                    addFormField(dataOutputStream, "thumbnailUpload", String.valueOf(true));
                    addFormField(dataOutputStream, "thumbnailUpload", String.valueOf(true));
                    LogUtils.d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP     Thumbnail uploaded");

                } else {
                    addFormField(dataOutputStream, "thumbnailUpload", "false");
                }
            } else {
                addFormField(dataOutputStream, "thumbnailUpload", "false");
                LogUtils.d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP   No Thumbnail ");
            }

            // End of multipart/form-data
            dataOutputStream.writeBytes("--" + BOUNDARY + "--" + LINE_FEED);
            dataOutputStream.flush();

            inputStream = httpsURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            input = new BufferedReader(inputStreamReader);

            StringBuilder serverResponse = new StringBuilder();
            for (int c = input.read(); c != -1; c = input.read()) {
                serverResponse.append((char) c);
            }

            upLoadProgressState(100, false);
            LogUtils.d(LOG_TAG, "Got serverResponse for merge " + serverResponse);
            int statusCode = httpsURLConnection.getResponseCode();

            LogUtils.d(LOG_TAG, "status code for merge" + statusCode);
            if (statusCode == 200) {
                uploadInfo.setUploaded(true);
                JSONObject jsonObject = new JSONObject(serverResponse.toString());
                String thumbnailURL = null;
                jsonObject.get("fileId");
                fileID = (String) jsonObject.get("fileId");

                if (jsonObject.has("thumbnailURL")) {
                    thumbnailURL = (String) jsonObject.get("thumbnailURL");
                }
                uploadInfo.setFileId(fileID);

                uploadInfo.setMergeTriggered(true);

                Message msg = Message.obtain();
                Bundle data = new Bundle();
                data.putBoolean("success", true);
                data.putString(Constants.MESSAGE_ID, messageId);
                data.putString("fileExtn", fileExtn);
                data.putString("filePath", outFilePath.getPath());
                data.putString("fileId", fileID);
                data.putString("fileName", fileName);
                data.putString("componentType", componentType);
                data.putString("fileSize", ""+getFileSize(context.getContentResolver(), outFilePath));
                data.putString("thumbnailURL", thumbnailURL);
                data.putString("orientation", orientation);
                msg.setData(data); //put the data here
                messenger.send(msg);
                if (listener != null)
                    listener.fileUploaded(/*comp, messageId, fileExtn,messenger*/);
                LogUtils.d(LOG_TAG, "The fileId is " + fileID + " is listener is null " + (listener == null ? "True " : "false"));
            } else {
                LogUtils.d(LOG_TAG, "The Resp is " + serverResponse);
                sendUploadFailedNotice(true);
            }

        } catch (Exception e) {
            if (e instanceof SocketTimeoutException) {
                sendUploadFailedNotice(true);
            } else if (httpsURLConnection != null) {
                int resCode = -1;
                try {
                    resCode = httpsURLConnection.getResponseCode();
                } catch (Exception ignored) {
                }
                LogUtils.d(LOG_TAG, "Hi res code is " + resCode);
                if (resCode == Constants.UPLOAD_ERROR_CODE_404) {
                    handleErr404(httpsURLConnection.getErrorStream(), fileName);
                } else {
                    sendUploadFailedNotice(true);
                }
            } else {
                sendUploadFailedNotice(true);
            }

            e.printStackTrace();
            LogUtils.e(LOG_TAG, "Failed to send the merge initiation message " + e);
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();

                if (inputStream != null)
                    inputStream.close();

                if (dataOutputStream != null)
                    dataOutputStream.close();

                if (inputStreamReader != null)
                    inputStreamReader.close();

                if (httpsURLConnection != null)
                    httpsURLConnection.disconnect();

                if (fis != null)
                    fis.close();

                if (thumbBaos != null)
                    thumbBaos.close();

                if (input != null)
                    input.close();

                helper.getFileUploadInfoMap().put(fileToken, uploadInfo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addFormField(DataOutputStream dataOutputStream, String name, String value) throws IOException {
        dataOutputStream.writeBytes("--" + BOUNDARY + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=" + CHARSET + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.writeBytes(value + LINE_FEED);
        dataOutputStream.flush();
    }

    private void addFilePart(DataOutputStream dataOutputStream, byte[] uploadFileBytes, String fileName) throws IOException {
        dataOutputStream.writeBytes("--" + BOUNDARY + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + "thumbnail" + "\"; filename=\"" + fileName + "\"" + LINE_FEED);
        dataOutputStream.writeBytes("Content-Type: " + "image/png" + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);

        dataOutputStream.write(uploadFileBytes);
        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.flush();
    }

    private String getFileSizeMegaBytes(File file) {
        if (file.length() < (1024 * 1024)) {
            return df2.format((double) file.length() / (1024)) + "kb";
        } else
            return df2.format((double) file.length() / (1024 * 1024)) + "mb";
    }

    private void handleErr404(InputStream errorStream, String fileName) {
        LogUtils.d(LOG_TAG, "Starting upload failed chunks");
        StringBuilder response = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream))) {
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            if (!StringUtils.isNullOrEmpty(response.toString())) {
                LogUtils.d(LOG_TAG, "Failed chunks are " + response);
                MissingChunks msc = new Gson().fromJson(response.toString(), MissingChunks.class);
                if (msc != null) {
                    UploadError uE = msc.getErrors().get(0);
                    ArrayList<String> missingChunks = new ArrayList<String>(Arrays.asList(uE.getMsg().substring(1, uE.getMsg().length() - 1).split("\\s*,\\s*")));
                    changeUploadInfoForFailedChunks(missingChunks, fileName);
                    startUploadFailedChunks(missingChunks);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeUploadInfoForFailedChunks(ArrayList<String> chunkNumbers, String fileName) {
        try {
            uploadInfo.setUploadCount(uploadInfo.getUploadCount() - chunkNumbers.size());
            for (String chunkNo : chunkNumbers) {
                ChunkInfo chInfo = Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).get(chunkNo);
                if (chInfo != null) {
                    chInfo.setUploaded(false);
                    Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).put(Integer.parseInt(chunkNo), chInfo);
                }
            }
            uploadInfo.setUploaded(false);
            helper.getFileUploadInfoMap().put(fileToken, uploadInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUploadFailedNotice(boolean isFromMergeSignal) {
        uploadInfo.setUploaded(false);
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString(Constants.MESSAGE_ID, messageId);
        data.putString("fileExtn", fileExtn);
        data.putString("fileId", Constants.LOCAL_FILE_ID);
        data.putString("fileName", fileName);
        data.putString("filePath", outFilePath.getPath());
        data.putString("componentType", componentType);
        data.putBoolean("success", false);
        msg.setData(data); //put the data here

        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            LogUtils.d("error", "error");
        }

        if (listener != null) {
            listener.fileUploaded(/*comp, messageId, fileExtn,messenger*/);
        }

        upLoadProgressState(100, false);
        uploadInfo.setMergeTriggered(isFromMergeSignal);
    }
}
