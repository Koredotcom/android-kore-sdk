/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

public final class pj_ssl_cert_verify_flag_t {
    public static final int PJ_SSL_CERT_ESUCCESS = 0;
    public static final int PJ_SSL_CERT_EISSUER_NOT_FOUND = 1;
    public static final int PJ_SSL_CERT_EUNTRUSTED = 2;
    public static final int PJ_SSL_CERT_EVALIDITY_PERIOD = 4;
    public static final int PJ_SSL_CERT_EINVALID_FORMAT = 8;
    public static final int PJ_SSL_CERT_EINVALID_PURPOSE = 16;
    public static final int PJ_SSL_CERT_EISSUER_MISMATCH = 32;
    public static final int PJ_SSL_CERT_ECRL_FAILURE = 64;
    public static final int PJ_SSL_CERT_EREVOKED = 128;
    public static final int PJ_SSL_CERT_ECHAIN_TOO_LONG = 256;
    public static final int PJ_SSL_CERT_EIDENTITY_NOT_MATCH = 0x40000000;
    public static final int PJ_SSL_CERT_EUNKNOWN = Integer.MIN_VALUE;
}

