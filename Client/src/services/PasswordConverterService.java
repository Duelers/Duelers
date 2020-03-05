package services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class PasswordConverterService {

    private MessageDigest digest;

    public PasswordConverterService(MessageDigest digest) {
        this.digest = digest;
    }

    public String convert(String login, String password) {
        String originalString = login + "_" + password;
        byte[] hash = this.digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
