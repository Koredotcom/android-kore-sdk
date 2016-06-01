package websocket.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import android.content.Context;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.*;
import android.util.Log;


import codebutler.android_websockets.WebSocketClient;
import websocket.async.util.ThreadPoolExecutorForPresence;

/**
 * 
 * Created by Vinay S Shenoy on 07/09/2013
 */
public class AsyncHttpClient {

    private Context mContext = null;
    private final String LOG_TAG = AsyncHttpClient.this.getClass().getSimpleName();

    public AsyncHttpClient(Context mContext) {
        this.mContext = mContext;
    }

    public static class SocketIORequest {

        private String mUri;
        private String mEndpoint;

        public SocketIORequest(String uri) {
            this(uri, null);
        }

        public SocketIORequest(String uri, String endpoint) {

            mUri = Uri.parse(uri).buildUpon().encodedPath("/socket.io/1/").build().toString();
            mEndpoint = endpoint;
        }

        public String getUri() {

            return mUri;
        }

        public String getEndpoint() {

            return mEndpoint;
        }
    }

    public static interface StringCallback {
        public void onCompleted(final Exception e, String result);
    }

    public static interface WebSocketConnectCallback {
        public void onCompleted(Exception ex, WebSocketClient webSocket);
    }

    public void executeString(final SocketIORequest socketIORequest, final StringCallback stringCallback) {

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
            	if(socketIORequest.getUri().toLowerCase().contains("https"))
            	{
            		// Code block for determining HTTP or https
            		HttpURLConnection conn = null;
            		try{
            	    
            	    URL url = new URL(socketIORequest.getUri());
            	        trustAllHosts();
                        HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                        https.setHostnameVerifier(DO_NOT_VERIFY);
                        conn = https;
            		}catch(Exception e){
            			Log.d(LOG_TAG,"The Exception  is" + e.toString());
                        e.printStackTrace();
            		}
//            		HttpPost post = null;
            		
            		try{
//            			post = new HttpPost(socketIORequest.getUri());
            			
            			String res = convertStreamToString(conn.getInputStream());
            			
            			if (stringCallback != null) {
	                        stringCallback.onCompleted(null, res);
	                    }
            		}catch(Exception e){
            			Log.d(LOG_TAG,"The Exception  is" + e.toString());
                        e.printStackTrace();
                        if (stringCallback != null) {
	                        stringCallback.onCompleted(e, null);
	                    }
	                } finally {
	                    conn.disconnect();
	                    conn = null;
	                }
	 
            	}
            	else
            	{
	            	AndroidHttpClient httpClient = null;
	            	HttpPost post = null;
	            	try{
	                httpClient = AndroidHttpClient.newInstance("android-websockets-2.0");
	                post = new HttpPost(socketIORequest.getUri());
	                Log.d(LOG_TAG, "The URI is" + socketIORequest.getUri());
	            	}catch(Exception e){
	            		Log.d(LOG_TAG,"The Exception  is" + e.toString());
                        e.printStackTrace();
	            	}
	                try {
	                    HttpResponse res = httpClient.execute(post);
	                    String responseString = readToEnd(res.getEntity().getContent());
	
	                    if (stringCallback != null) {
	                        stringCallback.onCompleted(null, responseString);
	                    }
	
	                } catch (IOException e) {
	
	                    if (stringCallback != null) {
	                        stringCallback.onCompleted(e, null);
	                    }
	                } finally {
	                    httpClient.close();
	                    httpClient = null;
	                }
	                
            	}
            	return null;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(ThreadPoolExecutorForPresence.getInstance().getExecutor());
        } else {
            asyncTask.execute();
        }
    }

    private byte[] readToEndAsArray(InputStream input) throws IOException {
        DataInputStream dis = new DataInputStream(input);
        byte[] stuff = new byte[1024];
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        int read = 0;
        while ((read = dis.read(stuff)) != -1) {
            buff.write(stuff, 0, read);
        }

        return buff.toByteArray();
    }

    private String readToEnd(InputStream input) throws IOException {
        return new String(readToEndAsArray(input));
    }

    /*private DefaultHttpClient getDefaultHttpClient() throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException{
    	SchemeRegistry schemeRegistry = new SchemeRegistry ();

    	schemeRegistry.register (new Scheme ("http",
    	    PlainSocketFactory.getSocketFactory (), 80));
    	schemeRegistry.register (new Scheme ("https",
    	    new CustomSSLSocketFactory (), 443));

    	BasicHttpParams bhttpParams = new BasicHttpParams();
    	bhttpParams.setParameter("userAgent", "android-websockets-2.0");
    	ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(bhttpParams, schemeRegistry);


    	return new DefaultHttpClient(cm, bhttpParams);
    }*/
    
    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
          public boolean verify(String hostname, SSLSession session) {
              return true;
          }
   };
    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
              // Create a trust manager that does not validate certificate chains
              TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                              return new java.security.cert.X509Certificate[] {};
                      }

                      public void checkClientTrusted(X509Certificate[] chain,
                                      String authType) throws CertificateException {
                      }

                      public void checkServerTrusted(X509Certificate[] chain,
                                      String authType) throws CertificateException {
                      }
              } };

              // Install the all-trusting trust manager
              try {
                      SSLContext sc = SSLContext.getInstance("TLS");
                      sc.init(null, trustAllCerts, new java.security.SecureRandom());
                      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
              } catch (Exception e) {
                      e.printStackTrace();
              }
      }
    
    
    
    
   /* public class CustomSSLSocketFactory extends org.apache.http.conn.ssl.SSLSocketFactory
    {
    private SSLSocketFactory FACTORY = HttpsURLConnection.getDefaultSSLSocketFactory ();

    public CustomSSLSocketFactory () throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException
        {
        super(null);
        try
            {
            SSLContext context = SSLContext.getInstance ("TLS");
            TrustManager[] tm = new TrustManager[] { new FULLX509TrustManager () };
            context.init (null, tm, new SecureRandom ());

            FACTORY = context.getSocketFactory ();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            }
        }

    public Socket createSocket() throws IOException
    {
        return FACTORY.createSocket();
    }

     //  add other methods like createSocket() and getDefaultCipherSuites().
     // Hint: they all just make a call to member FACTORY 
    }*/
    
    /*class FULLX509TrustManager implements X509TrustManager{

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			 for (X509Certificate cert : chain) {
                 if (cert != null && cert.getCriticalExtensionOIDs() != null)
                     cert.getCriticalExtensionOIDs().remove("2.5.29.15");
             }
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			 return new X509Certificate[0];
		}
    	
    }*/
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
