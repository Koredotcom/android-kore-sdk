package com.kore.korefileuploadsdk.managers;

import android.content.Context;
import android.util.Log;

import com.kore.korefileuploadsdk.configurations.Constants;
import com.kore.korefileuploadsdk.configurations.FileUploadEndPoints;
import com.kore.korefileuploadsdk.listeners.FileTokenListener;
import com.kore.korefileuploadsdk.ssl.KoreHttpsUrlConnectionBuilder;
import com.kore.korefileuploadsdk.utils.NetworkUtility;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Hashtable;



public class FileTokenManager{

	private FileTokenListener _listener;
	private String _Url;
	private String _accessToken;
	private Context _mContext;
	private boolean isWebHook;
	private String botId;

    public FileTokenManager(String host, FileTokenListener listener, String accessToken,
                            Context context, String userOrTeamId, boolean isAnonymousUser,
                            boolean isWebHook, String botId) {

        _accessToken = accessToken;
        _mContext = context;
        _listener = listener;
        this.isWebHook = isWebHook;
        this.botId = botId;

        if (isAnonymousUser)
        {
            if(!isWebHook)
                _Url = host + String.format(FileUploadEndPoints.ANONYMOUS_FILE_TOKEN);
            else
                _Url = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_FILE_TOKEN, botId, "ivr");
        }
        else
        {
            if(!isWebHook)
                _Url = host + String.format(FileUploadEndPoints.FILE_TOKEN, userOrTeamId);
            else
                _Url = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_FILE_TOKEN, botId, "ivr");
        }

        if (NetworkUtility.isNetworkConnectionAvailable(_mContext)) {

            try {
                String resp = getFileToken(null, _Url, _accessToken);
                System.out.println("The Final resp is " + resp);
                JSONObject _json1 = new JSONObject(resp);
                final String fileToken = _json1.getString("fileToken");
                final String expireOn = _json1.getString("expiresOn");
                Hashtable<String, String> hsh = new Hashtable<String, String>();
                hsh.put("fileToken", fileToken);
                hsh.put("expiresOn", expireOn);
                try {
                    if (_listener != null && resp != null && !resp.equalsIgnoreCase(""))
                        _listener.fileTokenRecievedSuccessfully(hsh);
                    else {
                        if (_listener != null)
                            _listener.fileTokenRecievedWithFailure(Constants.SERVER_ERROR, "Unable to reach server");
                    }
                } catch (Exception e) {
                    Log.e("FILE_TOKEN_SERVICE", e.toString());
                }
            } catch (Exception ex) {
                if (_listener != null)
                    _listener.fileTokenRecievedWithFailure(Constants.SERVER_ERROR, ex.toString());
            }


        } else {
            if (_listener != null)
                _listener.fileTokenRecievedWithFailure(Constants.NETWORK_FAILURE, "No network available.");
        }

    }


    private  String  getFileToken(String data,String Url, String header)  throws  UnsupportedEncodingException{
          
          String text = "";
          BufferedReader reader=null;
          URLConnection conn = null;
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
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            if (data != null) {
                wr.write(data);
                wr.flush();
            }

       // Get the server response
        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        // Read Server Response
        while((line = reader.readLine()) != null){
                   // Append server response in string
                   sb.append(line + "\n");
            }
            text = sb.toString();
        }
        catch(Exception ex){
             Log.d("FileTokenService", ex.toString());
        }
        finally{
            try{
                reader.close();
            }
            catch(Exception ex) {}
        }
              
      return text;
        
    }

}
