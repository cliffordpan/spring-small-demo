package me.hchome.demouser.security.key;

import java.security.Key;
import java.security.KeyPair;
import java.util.Base64;

public final class KeyUtils {

    private final static Base64.Encoder ENCODER = Base64.getEncoder();

    public static String privateKey(final KeyPair pair) {
        return generateKeyText(pair.getPrivate(), "private");
    }

    public static String publicKey(final KeyPair pair) {
        return generateKeyText(pair.getPublic(), "public");
    }

    private static String generateKeyText(Key key, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("-----BEGIN %s %s KEY-----\n", key.getAlgorithm().toUpperCase(), type.toUpperCase()));
        builder.append(ENCODER.encodeToString(key.getEncoded()));
        builder.append(String.format("\n-----END %s %s KEY-----\n", key.getAlgorithm().toUpperCase(), type.toUpperCase()));
        return builder.toString();
    }
}
