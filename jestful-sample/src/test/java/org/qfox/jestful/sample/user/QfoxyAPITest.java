package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.cache.CacheController;
import org.qfox.jestful.client.cache.impl.FileDataStorage;
import org.qfox.jestful.client.cache.impl.http.HttpCacheManager;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.client.retry.RetryController;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.commons.Base64;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.CountDownLatch;

public class QfoxyAPITest {

    private static final String ALGORITHM = "RSA/NONE/NoPadding";

    public void encrypt(String publicKeyEncoded, int keysize, InputStream in, OutputStream out) throws Exception {
        byte[] publicKeyDecoded = Base64.decode(publicKeyEncoded.toCharArray());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyDecoded);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        cipher.init(Cipher.PUBLIC_KEY, publicKey);
        int length;
        byte[] segment = new byte[keysize / 8 - 11];
        while ((length = in.read(segment)) != -1) {
            out.write(cipher.doFinal(segment, 0, length));
        }
    }

    public void decrypt(String privateKeyEncoded, int keysize, InputStream in, OutputStream out) throws Exception {
        byte[] privateKeyDecoded = Base64.decode(privateKeyEncoded.toCharArray());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyDecoded);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        cipher.init(Cipher.PRIVATE_KEY, privateKey);
        int length;
        byte[] segment = new byte[keysize / 8];
        while ((length = in.read(segment)) != -1) {
            out.write(cipher.doFinal(segment, 0, length));
        }
    }

    @Test
    public void index() throws Exception {
        CacheController cacheController;
        QfoxyAPI qfoxyAPI = NioClient.builder()
                .setProtocol("https")
                .setHostname("fex.bdstatic.com")
                .setKeepAlive(true)
                .setIdleTimeout(10)
                .build()
                .creator()
                .addBackPlugins(Redirector.builder().build())
                .addBackPlugins(Authenticator.builder().build())
                .addBackPlugins(RetryController.builder().build())
                .addBackPlugins(cacheController = CacheController.builder().setCacheManager(new HttpCacheManager(new FileDataStorage())).build())
                .create(QfoxyAPI.class);

        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 1; i++) {
            qfoxyAPI.jQuery(new CallbackAdapter<String>() {
                @Override
                public void onCompleted(boolean success, String result, Exception exception) {
                    if (success) System.out.println(result.length());
                    else exception.printStackTrace();
                    latch.countDown();
                }
            }, false);
        }
        latch.await();
        System.out.println(cacheController.hits() + ":" + cacheController.misses() + ":" + cacheController.updates());
    }

}
