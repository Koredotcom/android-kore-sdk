package kore.botssdk.ssl;
//public class SSLHelper {
//
//    public static String certificateType = "X.509";
//
//    public static Certificate getCertificate(Context mContext, String serverUrl){
//        Certificate ca = null;
//        try {
//            CertificateFactory cf;
//            cf = CertificateFactory.getInstance(certificateType);
//
//            InputStream caInput;
//            caInput = new BufferedInputStream(mContext.getAssets().open(getCertificatePath(serverUrl)));
//
//            ca = cf.generateCertificate(caInput);
//        } catch (SecurityException e){
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ca;
//    }
//
//    public static KeyStore getKeyStoreWithCertificate(Context mContext, String serverUrl){
//        KeyStore keyStore = null;
//        try {
//            Certificate ca = getCertificate(mContext, serverUrl);
//
//            String keyStoreType = KeyStore.getDefaultType();
//            keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//        } catch (KeyStoreException e){
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return keyStore;
//    }
//
//    public static TrustManagerFactory getTrustManagerFactoryWithCertificate(Context mContext, String serverUrl){
//        TrustManagerFactory tmf = null;
//        try {
//            KeyStore keyStore = getKeyStoreWithCertificate(mContext, serverUrl);
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//        } catch (KeyStoreException e){
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return tmf;
//    }
//
//    public static SSLContext getSSLContextWithCertificate(Context mContext, String serverUrl){
//        SSLContext context = null;
//        try {
////            TrustManagerFactory tmf = getTrustManagerFactoryWithCertificate(mContext, serverUrl);
//
//            TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        public X509Certificate[] getAcceptedIssuers() {
//                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
//                            return myTrustedAnchors;
//                        }
//
//                        @Override
//                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                        }
//                    }
//            };
//
//            context = SSLContext.getInstance("TLS");
//            context.init(null, trustAllCerts, null);
//        } catch (NoSuchAlgorithmException e){
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        return context;
//    }
//
//    public static X509TrustManager systemDefaultTrustManager() {
//
//        try
//        {
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init((KeyStore) null);
//            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
//            }
//            return (X509TrustManager) trustManagers[0];
//        } catch (GeneralSecurityException e) {
//            throw new AssertionError(); // The system has no TLS. Just give up.
//        }
//
//    }
//
//    public static String getCertificatePath(String serverURL)
//    {
//        return "certificates/"+ SDKConfiguration.SSLConfig.sslCertificatePath;
//    }
//}
