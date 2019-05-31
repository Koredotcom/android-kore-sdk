/*
package com.kore.korefileuploadsdk.ssl;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class SSLHelper {

    public static String certificateType = "X.509";

    public static Certificate getCertificate(Context mContext, String serverUrl){
        Certificate ca = null;
        try {
            CertificateFactory cf;
            cf = CertificateFactory.getInstance(certificateType);

            InputStream caInput;
            caInput = new BufferedInputStream(mContext.getAssets().open(getCertificatePath(serverUrl)));

            ca = cf.generateCertificate(caInput);
        } catch (SecurityException e){
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ca;
    }

    public static KeyStore getKeyStoreWithCertificate(Context mContext, String serverUrl){
        KeyStore keyStore = null;
        try {
            Certificate ca = getCertificate(mContext, serverUrl);

            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e){
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyStore;
    }

    public static TrustManagerFactory getTrustManagerFactoryWithCertificate(Context mContext, String serverUrl){
        TrustManagerFactory tmf = null;
        try {
            KeyStore keyStore = getKeyStoreWithCertificate(mContext, serverUrl);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (KeyStoreException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tmf;
    }

    public static SSLContext getSSLContextWithCertificate(Context mContext, String serverUrl){
        SSLContext context = null;
        try {
            TrustManagerFactory tmf = getTrustManagerFactoryWithCertificate(mContext, serverUrl);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return context;
    }

    public static String getCertificatePath(String serverURL){
        if(serverURL.contains("kore.com")) {
            return "certificates/KoreDomain.cer";
        }else  if( serverURL.contains("qa1.kore.ai")){
            return "certificates/qa1-koreai.crt";//star_kore_new_ai.crt";//star_kore_ai.crt";//star_kore_ai.cer";
        }else  if( serverURL.contains("qa.kore.ai")) {
            return "certificates/qa.ai-cert.cer";//star_kore_new_ai.crt";//star_kore_ai.crt";//star_kore_ai.cer";
        }else if(serverURL.contains("kore.ai")){
            return "certificates/koreai.crt";
        }else if (serverURL.contains("kore-files.com")) {
            return "certificates/Kore-FilesDomain.cer";
        }
        else if(serverURL.contains("kore.net")){
            return "certificates/kore-net.cer";
        }
        else {
            return null;
        }
    }
}
*/
