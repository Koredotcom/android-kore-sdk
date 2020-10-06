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
    boolean isFromComposebar;
    public UploadExecutor(Context context, String fileName, String fileToken, String accessToken,String userOrTeamId,byte[] dataToPost,
                          int chunkNo, ChunkUploadListener listener, String host,boolean isFromComposebar) {

        this.fileName=fileName;
        this.fileToken = fileToken;
        this.accessToken=accessToken;
        this.userOrTeamId = userOrTeamId;
        this.dataToSet = dataToPost;
        this.chunkNo = chunkNo;
        this.mListener = listener;
        this.context = context;
        this.host = host;
        this.isFromComposebar=isFromComposebar;
    }

    @Override
    public void run() {
        String serverResponse = null;

        try {

            Log.d(LOG_TAG, "About to send chunks" + chunkNo + "for file" + fileName);
            String FULL_URL = null;
//            if(isTeam)
//                FULL_URL = host + String.format(FileUploadEndPoints.TEAM_CHUNK_UPLOAD_END_POINT, userOrTeamId,fileToken);
//            else
                if(isFromComposebar)
                 FULL_URL = host + String.format(FileUploadEndPoints.COMPOSEBAR_CHUNK_UPLOAD_END_POINT,fileToken);
                else
                FULL_URL = host + String.format(FileUploadEndPoints.CHUNK_UPLOAD_END_POINT, userOrTeamId,fileToken);
            KoreHttpsUrlConnectionBuilder koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, FULL_URL);
//            koreHttpsUrlConnectionBuilder.pinKoreCertificateToConnection();
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

            // TODO Don't delete, this code was used previously
           /* HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 60000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpClient httpClient = null;
            httpClient = new DefaultHttpClient(httpParameters);
            HttpContext httpContext = new BasicHttpContext();

            HttpPost httpPost = new HttpPost(FULL_URL);
            HttpResponse response = null;
            KoreLogger.debugLog(LOG_TAG, "hostAddress " + BASE_URL);
            KoreLogger.debugLog(LOG_TAG, "uploadUrl "+ FULL_URL);


            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("chunkNo", new StringBody(chunkNo+""));

            reqEntity.addPart("fileToken", new StringBody(fileToken));
            reqEntity.addPart("chunk", new ByteArrayBody(dataToSet, fileName));
//        			reqEntity.addPart("fileName", new StringBody(fileName));
//        			reqEntity.addPart("fileExtn", new StringBody(fileExtn));
            httpPost.setHeader("User-Agent", Constants.getUserAgent());
            httpPost.setHeader("Authorization", accessToken);
            httpPost.setHeader("Cache-Control", "no-cache");
            if(Cookie != null && !Cookie.equalsIgnoreCase("")){
                httpPost.setHeader("Cookie", Cookie);
            }
            httpPost.setEntity(reqEntity);

            response = httpClient.execute(httpPost, httpContext);

            int statusCode = response.getStatusLine().getStatusCode();


            KoreLogger.debugLog(LOG_TAG,"status code for chunks"+chunkNo+ "is"+statusCode);
            serverResponse = EntityUtils.toString(response.getEntity());

            KoreLogger.debugLog(LOG_TAG,"Got serverResponse for chunk upload"+ serverResponse);*/
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