package com.kore.korefileuploadsdk.core;

import android.content.Context;
import android.util.Log;

import com.kore.korefileuploadsdk.configurations.Constants;
import com.kore.korefileuploadsdk.configurations.FileUploadEndPoints;
import com.kore.korefileuploadsdk.listeners.ChunkUploadListener;
import com.kore.korefileuploadsdk.ssl.KoreHttpsUrlConnectionBuilder;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;



public class UploadExecutor implements Runnable{

    public final String LOG_TAG="UploadExecutor";

    /*Intent params*/
    private String fileName;
    private String accessToken;
    private String fileToken;
    private String userOrTeamId;
    private byte[] dataToSet = null;
    private int chunkNo = 0;
    private Context context;
    private ChunkUploadListener mListener = null;
    private String host;
    private boolean isAnonymousUser;
    private boolean isWebhook;
    private String botId;

    public UploadExecutor(Context context, String fileName, String fileToken, String accessToken,String userOrTeamId,byte[] dataToPost,
                          int chunkNo, ChunkUploadListener listener, String host, boolean isAnonymousUser, boolean isWebHook, String botId) {

        this.fileName=fileName;
        this.fileToken = fileToken;
        this.accessToken=accessToken;
        this.userOrTeamId = userOrTeamId;
        this.dataToSet = dataToPost;
        this.chunkNo = chunkNo;
        this.mListener = listener;
        this.context = context;
        this.host = host;
        this.isAnonymousUser = isAnonymousUser;
        this.isWebhook = isWebHook;
        this.botId = botId;
    }

    @Override
    public void run() {
        String serverResponse = null;

        try {

            Log.d(LOG_TAG, "About to send chunks" + chunkNo + "for file" + fileName);
            String FULL_URL = null;
            if(isAnonymousUser)
            {
                if(!isWebhook)
                    FULL_URL = host + String.format(FileUploadEndPoints.ANONYMOUS_CHUNK_UPLOAD, userOrTeamId,fileToken);
                else
                    FULL_URL = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_CHUNK_UPLOAD, botId, "ivr", fileToken);
            }
            else
            {
                if(!isWebhook)
                    FULL_URL = host + String.format(FileUploadEndPoints.CHUNK_UPLOAD, userOrTeamId,fileToken);
                else
                    FULL_URL = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_CHUNK_UPLOAD, botId, "ivr", fileToken);
            }
            KoreHttpsUrlConnectionBuilder koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, FULL_URL);
            HttpsURLConnection httpsURLConnection = koreHttpsUrlConnectionBuilder.getHttpsURLConnection();
            httpsURLConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("User-Agent", Constants.getUserAgent());
            httpsURLConnection.setRequestProperty("Authorization", accessToken);
            httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpsURLConnection.setReadTimeout(Constants.CONNECTION_READ_TIMEOUT);

            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("chunkNo", new StringBody(chunkNo+""));
            reqEntity.addPart("fileToken", new StringBody(fileToken));
            reqEntity.addPart("chunk", new ByteArrayBody(dataToSet, fileName));
            httpsURLConnection.setRequestProperty(reqEntity.getContentType().getName(),reqEntity.getContentType().getValue());
            DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            reqEntity.writeTo(dataOutputStream);
            dataOutputStream.close();

            //Real upload starting here -->>
            Log.d("upload new", "good so far");
            BufferedReader input = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

            serverResponse = "";
            for( int c = input.read(); c != -1; c = input.read() ) {
                serverResponse = serverResponse + (char)c;
            }
            input.close();
            httpsURLConnection.disconnect();
            Log.d(LOG_TAG,"Got serverResponse for chunk upload"+ serverResponse);
            int statusCode = httpsURLConnection.getResponseCode();
            Log.e(LOG_TAG,"status code for chunks"+chunkNo+ "is"+statusCode);

            String chunkNo = null;

            if (statusCode == 200) {

                JSONObject jsonObject = new JSONObject(serverResponse);

                if (jsonObject.get("chunkNo")!=null) {
                    chunkNo = (String) jsonObject.get("chunkNo");
                    if(mListener != null)
                        mListener.notifyChunkUploadCompleted(chunkNo, fileName);
                    Log.e(LOG_TAG,"Response for chunk ::::"+chunkNo + "for file"+fileName);
                }

            } else {
                if(mListener != null)
                    mListener.notifyChunkUploadCompleted(chunkNo, fileName);
                throw new Exception("Response code not 200");
            }


        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception in uploading chunk " + e.toString());
            e.printStackTrace();
//            if(!(e instanceof NoNetworkException)){
                if(mListener != null)
                    mListener.notifyChunkUploadCompleted(chunkNo+"", fileName);
//            }
            Log.e(LOG_TAG, "Failed to post message for chunk no:: " + this.chunkNo);
        }

    }

}