package com.aseubel.types.util;

import org.springframework.core.io.DefaultResourceLoader;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;

public class SslUtil {

    public static SSLContext createSSLContext(String type, String path, String sslPassword) throws Exception {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        InputStream inputStream = Files.newInputStream(Paths.get(path));
        char[] passArray = sslPassword.toCharArray();
        SSLContext sslContext = SSLContext.getInstance("SSLv3"); // 这里TLS或者SSLv3都可以
        KeyStore ks = KeyStore.getInstance(type);
        // 加载keytool 生成的文件
        ks.load(inputStream, passArray);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, passArray);
        sslContext.init(kmf.getKeyManagers(), null, null);
        inputStream.close();
        return sslContext;
    }

}
