package com.kore.korefileuploadsdk.ssl;

import android.content.Context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class KoreHttpsUrlConnectionBuilder {

    private HttpsURLConnection httpsURLConnection;
    private String uri;
    private Context mContext;

    public KoreHttpsUrlConnectionBuilder(Context context, String uri){
        this.uri = uri;
        this.mContext = context;
        try {
            URL url = new URL(this.uri);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /*public void pinKoreCertificateToConnection(){
        if(uri.contains("kore.com") && httpsURLConnection != null) {
            SSLContext context = SSLHelper.getSSLContextWithCertificate(mContext, uri);
            httpsURLConnection.setSSLSocketFactory(context.getSocketFactory());
        }
    }*/

    public HttpsURLConnection getHttpsURLConnection(){
        return httpsURLConnection;
    }


    /**
     * An {@link } that doesn't allow {@code SSLv3} only connections
     * <p>fixes https://github.com/koush/ion/issues/386</p>
     */
    /*private static class NoSSLv3SSLSocket extends DelegateSSLSocket {

        private NoSSLv3SSLSocket(SSLSocket delegate) {
            super(delegate);

            String canonicalName = delegate.getClass().getCanonicalName();
            if (!canonicalName.equals("org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl")) {
                // try replicate the code from HttpConnection.setupSecureSocket()
                try {
                    Method msetUseSessionTickets = delegate.getClass().getMethod("setUseSessionTickets", boolean.class);
                    if (null != msetUseSessionTickets) {
                        msetUseSessionTickets.invoke(delegate, true);
                    }
                } catch (NoSuchMethodException ignored) {
                } catch (InvocationTargetException ignored) {
                } catch (IllegalAccessException ignored) {
                }
            }
        }

        @Override
        public void setEnabledProtocols(String[] protocols) {
            if (protocols != null && protocols.length == 1 && "SSLv3".equals(protocols[0])) {
                // no way jose
                // see issue https://code.google.com/p/android/issues/detail?id=78187
                List<String> enabledProtocols = new ArrayList<String>(Arrays.asList(delegate.getEnabledProtocols()));
                if (enabledProtocols.size() > 1) {
                    enabledProtocols.remove("SSLv3");
                } else {
                    Log.d("KHUrlConnectionBuilder","SSL stuck with protocol available for " + String.valueOf(enabledProtocols));
                }
                protocols = enabledProtocols.toArray(new String[enabledProtocols.size()]);
            }
            super.setEnabledProtocols(protocols);
        }

    }*/

    /**
     * {@link javax.net.ssl.SSLSocketFactory} that doesn't allow {@code SSLv3} only connections
     */
    /*private static class NoSSLv3Factory extends SSLSocketFactory {
        private final SSLSocketFactory delegate;

        private NoSSLv3Factory(SSLSocketFactory delegate) {
            this.delegate = delegate;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        private static Socket makeSocketSafe(Socket socket) {
            if (socket instanceof SSLSocket) {
                socket = new NoSSLv3SSLSocket((SSLSocket) socket);
            }
            return socket;
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return makeSocketSafe(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException {
            return makeSocketSafe(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return makeSocketSafe(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return makeSocketSafe(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return makeSocketSafe(delegate.createSocket(address, port, localAddress, localPort));
        }
    }*/
}
