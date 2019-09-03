package me.hchome.demouser.security.key;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class ClientUsersAuthenticationKeyGenerator implements InitializingBean {

    public KeyPair generateKeyPair() {
        return generator.generateKeyPair();
    }

    public PublicKey getPublic(byte[] bytes) throws InvalidKeySpecException {
        return factory.generatePublic(new X509EncodedKeySpec(bytes));
    }

    private void initialize() throws NoSuchAlgorithmException {
        generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(keySize);
        factory = KeyFactory.getInstance(algorithm);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    @Getter
    @Setter
    @NonNull
    private String algorithm = DEFAULT_ALGORITHM;
    @Setter
    private int keySize = DEFAULT_KEY_SIZE;
    private KeyPairGenerator generator;

    private KeyFactory factory;


    private final static String DEFAULT_ALGORITHM = "RSA";
    private final static int DEFAULT_KEY_SIZE = 2048;
}
