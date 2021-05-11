package com.activedge.usermgt.security.certs;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Trustmanager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[0];
    }
}
