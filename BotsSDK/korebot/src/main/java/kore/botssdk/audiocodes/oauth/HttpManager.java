package kore.botssdk.audiocodes.oauth;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpManager {

    private static final String TAG = "HttpManager";

    public static HttpURLConnection getConnection(String url) throws IOException {
        Log.d(TAG, "URL: " + url);
        HttpURLConnection urlConnection = (HttpURLConnection)new URL(url).openConnection();
        if(urlConnection instanceof HttpsURLConnection){
            try {
                SSLContext context = SSLContext.getInstance("SSL");
                context.init((KeyManager[]) null, new TrustManager[]{new AcceptAllTrustManager()}, null);
                ((HttpsURLConnection)urlConnection).setSSLSocketFactory(context.getSocketFactory());
                ((HttpsURLConnection)urlConnection).setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                Log.d(TAG, "cs_trust_all_certificates");
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
        return urlConnection;
    }

    public static void setBody(HttpURLConnection urlConnection, String body) throws IOException {
        Log.d(TAG, "BODY: " + body);
        if(body != null){
            urlConnection.setRequestProperty("Content-length", String.valueOf(body.getBytes("UTF-8").length));
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(body.getBytes("UTF-8"));
            outputStream.close();
        }
    }

    public static JSONObject make(HttpURLConnection urlConnection) throws IOException, JSONException {
        urlConnection.connect();
        String response = handleResponse(urlConnection);
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }

    private static String handleResponse(HttpURLConnection urlConnection) throws IOException {
        int responseCode = urlConnection.getResponseCode();
        Log.d(TAG, "responseCode: " + responseCode);
        Log.d(TAG, "responseMessage: " + urlConnection.getResponseMessage());
        InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
        }
        catch(IOException exception) {
            inputStream = urlConnection.getErrorStream();
        }
        String lastResponse = readStream(inputStream);
        if(responseCode < 400){
            //handleCoockies(urlConnection);
            String filePath = null;
            Log.d(TAG, "filePath: " + filePath);
            if(filePath != null){
                stringToFile(lastResponse, filePath);
            }
            else{
                printLongMsg("response: " + lastResponse);
            }
//            for (Map.Entry<String,String> header: responseHeaders.entrySet()) {
//                String key = header.getKey();
//                responseHeaders.put(key, urlConnection.getHeaderField(key));
//            }
        }
        else{
            printLongMsg("response: " + lastResponse);
        }
        return lastResponse;
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return response.toString();
    }

    private static void saveInputStream(InputStream inputStream, String filePath) {
        Log.d(TAG, "saveInputStream");
        try {
            File dbFile = new File(filePath);
            if (!dbFile.exists()) {
                dbFile.mkdirs();
                dbFile.delete();
            }
            OutputStream out = new FileOutputStream(new File(filePath));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            Log.d(TAG, "file saved successfully");
        } catch (IOException e) {
            Log.e(TAG, "saveInputStream ERROR", e);
        }
    }

    private static void stringToFile(String input, String filePath){
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter(filePath));
            writer.write(input);
            Log.d(TAG, "file saved successfully");
        }
        catch ( IOException e)
        {
            Log.e(TAG, "stringToFile ERROR", e);
        }
        finally
        {
            try
            {
                if ( writer != null)
                    writer.close( );
            }
            catch ( IOException e)
            {
            }
        }
    }

    private static void printLongMsg(String msg) {
        if (msg.length() > 4000) {
            Log.d(TAG,""+msg.substring(0,4000));
            printLongMsg(msg.substring(4000));
        } else {
            Log.d(TAG,""+msg);

        }
    }

    private static class AcceptAllTrustManager implements X509TrustManager {
        public static boolean debugMode = true;

        public AcceptAllTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] x509Certificates, String arg1) throws CertificateException {
            if (!debugMode) {
                try {
                    x509Certificates[0].checkValidity();
                } catch (Exception var4) {
                    throw new CertificateException("Certificate not valid or trusted.");
                }
            }

        }

        public void checkServerTrusted(X509Certificate[] x509Certificates, String arg1) throws CertificateException {
            if (!debugMode) {
                try {
                    x509Certificates[0].checkValidity();
                } catch (Exception var4) {
                    throw new CertificateException("Certificate not valid or trusted.");
                }
            }

        }

        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
        }
    }

}
