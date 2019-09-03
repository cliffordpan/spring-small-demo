package me.hchome.demouser.security.key;

import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public final class ClientUsersAuthenticationKeyGenerator implements InitializingBean {

    public KeyPair generateKeyPair() {

        return generator.generateKeyPair();
    }

    private void initialize() throws NoSuchAlgorithmException {
        generator = KeyPairGenerator.getInstance(DEFAULT_ALGORITHM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    @Setter
    @NonNull
    private String algorithm = DEFAULT_ALGORITHM;
    @Setter
    private int keySize = DEFAULT_KEY_SIZE;
    private KeyPairGenerator generator;


    private final static String DEFAULT_ALGORITHM = "RSA";
    private final static int DEFAULT_KEY_SIZE = 2048;
}
