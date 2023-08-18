package kore.botssdk.fileupload.managers;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.net.ssl.HttpsURLConnection;

import kore.botssdk.fileupload.configurations.Constants;
import kore.botssdk.fileupload.configurations.FileUploadEndPoints;
import kore.botssdk.fileupload.listeners.FileTokenListener;
import kore.botssdk.fileupload.ssl.KoreHttpsUrlConnectionBuilder;
import kore.botssdk.fileupload.utils.NetworkUtility;
import kore.botssdk.utils.LogUtils;


public class FileTokenManager{
    private final Context _mContext;
    public FileTokenManager(String host, FileTokenListener listener, String accessToken,
                            Context context, String userOrTeamId, boolean isAnonymousUser,
                            boolean isWebHook, String botId) {

        _mContext = context;

        String _Url;
        if (isAnonymousUser)
        {
            if(!isWebHook)
                _Url = host + String.format(FileUploadEndPoints.ANONYMOUS_FILE_TKN);
            else
                _Url = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_FILE_TKN, botId, "ivr");
        }
        else
        {
            if(!isWebHook)
                _Url = host + String.format(FileUploadEndPoints.FILE_TKN, userOrTeamId);
            else
                _Url = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_FILE_TKN, botId, "ivr");
        }

        if (NetworkUtility.isNetworkConnectionAvailable(_mContext)) {

            try {
                String resp = getFileToken(_Url, accessToken);
                System.out.println("The Final resp is " + resp);
                JSONObject _json1 = new JSONObject(resp);
                final String fileToken = _json1.getString("fileToken");
                final String expireOn = _json1.getString("expiresOn");
                Hashtable<String, String> hsh = new Hashtable<>();
                hsh.put("fileToken", fileToken);
                hsh.put("expiresOn", expireOn);
                try {
                    if (listener != null && !resp.equalsIgnoreCase(""))
                        listener.fileTokenRecievedSuccessfully(hsh);
                    else {
                        if (listener != null)
                            listener.fileTokenRecievedWithFailure(Constants.SERVER_ERROR, "Unable to reach server");
                    }
                } catch (Exception e) {
                    LogUtils.e("FILE_TOKEN_SERVICE", e.toString());
                }
            } catch (Exception ex) {
                if (listener != null)
                    listener.fileTokenRecievedWithFailure(Constants.SERVER_ERROR, ex.toString());
            }


        } else {
            if (listener != null)
                listener.fileTokenRecievedWithFailure(Constants.NETWORK_FAILURE, "No network available.");
        }

    }


    private  String  getFileToken(String Url, String header) {

        String text = "";
        BufferedReader reader = null;
        HttpsURLConnection conn = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        // Send data
        try {
            // Send POST data request
            KoreHttpsUrlConnectionBuilder koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(_mContext, Url);
//            koreHttpsUrlConnectionBuilder.pinKoreCertificateToConnection();
            conn = koreHttpsUrlConnectionBuilder.getHttpsURLConnection();
            conn.setDoOutput(true);
            if (header != null) conn.addRequestProperty("Authorization", "bearer " + header);
            conn.addRequestProperty("User-Agent", Constants.getUserAgent());
            System.out.println(" The userAgent in FileToken Service is " + Constants.getUserAgent());

            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);

            // Get the server response
            reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line).append("\n");
            }
            text = sb.toString();
        } catch (Exception ex) {
            LogUtils.d("FileTokenService", ex.toString());
        } finally {
            try {
                if(conn != null)
                    conn.disconnect();

                if (reader != null)
                    reader.close();

                if (inputStream != null)
                    inputStream.close();

                if (inputStreamReader != null)
                    inputStreamReader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return text;
        
    }

}
