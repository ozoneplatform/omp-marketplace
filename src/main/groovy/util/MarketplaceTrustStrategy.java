package util;

import org.apache.http.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MarketplaceTrustStrategy implements TrustStrategy{
    @Override
    public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        return true;
    }
}
