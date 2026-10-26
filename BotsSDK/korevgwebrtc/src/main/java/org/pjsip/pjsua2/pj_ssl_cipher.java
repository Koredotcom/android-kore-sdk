/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

public final class pj_ssl_cipher {
    public static final int PJ_TLS_UNKNOWN_CIPHER = -1;
    public static final int PJ_TLS_NULL_WITH_NULL_NULL = 0;
    public static final int PJ_TLS_RSA_WITH_NULL_MD5 = 1;
    public static final int PJ_TLS_RSA_WITH_NULL_SHA = 2;
    public static final int PJ_TLS_RSA_WITH_NULL_SHA256 = 59;
    public static final int PJ_TLS_RSA_WITH_RC4_128_MD5 = 4;
    public static final int PJ_TLS_RSA_WITH_RC4_128_SHA = 5;
    public static final int PJ_TLS_RSA_WITH_3DES_EDE_CBC_SHA = 10;
    public static final int PJ_TLS_RSA_WITH_AES_128_CBC_SHA = 47;
    public static final int PJ_TLS_RSA_WITH_AES_256_CBC_SHA = 53;
    public static final int PJ_TLS_RSA_WITH_AES_128_CBC_SHA256 = 60;
    public static final int PJ_TLS_RSA_WITH_AES_256_CBC_SHA256 = 61;
    public static final int PJ_TLS_DH_DSS_WITH_3DES_EDE_CBC_SHA = 13;
    public static final int PJ_TLS_DH_RSA_WITH_3DES_EDE_CBC_SHA = 16;
    public static final int PJ_TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA = 19;
    public static final int PJ_TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA = 22;
    public static final int PJ_TLS_DH_DSS_WITH_AES_128_CBC_SHA = 48;
    public static final int PJ_TLS_DH_RSA_WITH_AES_128_CBC_SHA = 49;
    public static final int PJ_TLS_DHE_DSS_WITH_AES_128_CBC_SHA = 50;
    public static final int PJ_TLS_DHE_RSA_WITH_AES_128_CBC_SHA = 51;
    public static final int PJ_TLS_DH_DSS_WITH_AES_256_CBC_SHA = 54;
    public static final int PJ_TLS_DH_RSA_WITH_AES_256_CBC_SHA = 55;
    public static final int PJ_TLS_DHE_DSS_WITH_AES_256_CBC_SHA = 56;
    public static final int PJ_TLS_DHE_RSA_WITH_AES_256_CBC_SHA = 57;
    public static final int PJ_TLS_DH_DSS_WITH_AES_128_CBC_SHA256 = 62;
    public static final int PJ_TLS_DH_RSA_WITH_AES_128_CBC_SHA256 = 63;
    public static final int PJ_TLS_DHE_DSS_WITH_AES_128_CBC_SHA256 = 64;
    public static final int PJ_TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 = 103;
    public static final int PJ_TLS_DH_DSS_WITH_AES_256_CBC_SHA256 = 104;
    public static final int PJ_TLS_DH_RSA_WITH_AES_256_CBC_SHA256 = 105;
    public static final int PJ_TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 = 106;
    public static final int PJ_TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 = 107;
    public static final int PJ_TLS_DH_anon_WITH_RC4_128_MD5 = 24;
    public static final int PJ_TLS_DH_anon_WITH_3DES_EDE_CBC_SHA = 27;
    public static final int PJ_TLS_DH_anon_WITH_AES_128_CBC_SHA = 52;
    public static final int PJ_TLS_DH_anon_WITH_AES_256_CBC_SHA = 58;
    public static final int PJ_TLS_DH_anon_WITH_AES_128_CBC_SHA256 = 108;
    public static final int PJ_TLS_DH_anon_WITH_AES_256_CBC_SHA256 = 109;
    public static final int PJ_TLS_RSA_EXPORT_WITH_RC4_40_MD5 = 3;
    public static final int PJ_TLS_RSA_EXPORT_WITH_RC2_CBC_40_MD5 = 6;
    public static final int PJ_TLS_RSA_WITH_IDEA_CBC_SHA = 7;
    public static final int PJ_TLS_RSA_EXPORT_WITH_DES40_CBC_SHA = 8;
    public static final int PJ_TLS_RSA_WITH_DES_CBC_SHA = 9;
    public static final int PJ_TLS_DH_DSS_EXPORT_WITH_DES40_CBC_SHA = 11;
    public static final int PJ_TLS_DH_DSS_WITH_DES_CBC_SHA = 12;
    public static final int PJ_TLS_DH_RSA_EXPORT_WITH_DES40_CBC_SHA = 14;
    public static final int PJ_TLS_DH_RSA_WITH_DES_CBC_SHA = 15;
    public static final int PJ_TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA = 17;
    public static final int PJ_TLS_DHE_DSS_WITH_DES_CBC_SHA = 18;
    public static final int PJ_TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA = 20;
    public static final int PJ_TLS_DHE_RSA_WITH_DES_CBC_SHA = 21;
    public static final int PJ_TLS_DH_anon_EXPORT_WITH_RC4_40_MD5 = 23;
    public static final int PJ_TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA = 25;
    public static final int PJ_TLS_DH_anon_WITH_DES_CBC_SHA = 26;
    public static final int PJ_SSL_FORTEZZA_KEA_WITH_NULL_SHA = 28;
    public static final int PJ_SSL_FORTEZZA_KEA_WITH_FORTEZZA_CBC_SHA = 29;
    public static final int PJ_SSL_FORTEZZA_KEA_WITH_RC4_128_SHA = 30;
    public static final int PJ_SSL_CK_RC4_128_WITH_MD5 = 65664;
    public static final int PJ_SSL_CK_RC4_128_EXPORT40_WITH_MD5 = 131200;
    public static final int PJ_SSL_CK_RC2_128_CBC_WITH_MD5 = 196736;
    public static final int PJ_SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5 = 262272;
    public static final int PJ_SSL_CK_IDEA_128_CBC_WITH_MD5 = 327808;
    public static final int PJ_SSL_CK_DES_64_CBC_WITH_MD5 = 393280;
    public static final int PJ_SSL_CK_DES_192_EDE3_CBC_WITH_MD5 = 458944;
}

