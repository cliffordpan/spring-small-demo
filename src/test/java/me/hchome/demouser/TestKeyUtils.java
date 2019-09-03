package me.hchome.demouser;

import me.hchome.demouser.security.key.KeyUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class TestKeyUtils {

    private KeyPairGenerator generator;

    @Before
    public void generate() throws NoSuchAlgorithmException {
        generator= KeyPairGenerator.getInstance("RSA");
    }

    @Test
    public void testNotNull(){
        Assert.assertNotNull(generator);
    }

    @Test
    public void testGenerateKeys(){
        KeyPair pair = generator.generateKeyPair();
        System.out.println(KeyUtils.publicKey(pair));
        System.out.println(KeyUtils.privateKey(pair));
    }
}
