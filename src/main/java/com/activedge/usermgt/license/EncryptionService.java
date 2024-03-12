package com.activedge.usermgt.license;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Base64;

@Component
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    @Value("${license.encryption.secret}")
    private String licenseSecretKey;

    public String encrypt(Object data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey secretKeySpec = new SecretKeySpec(licenseSecretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Convert data to String based on its type
        String dataString;
        if (data instanceof Number || data instanceof Double) {
            // For numeric types, convert to String
            dataString = String.valueOf(data);
        } else if (data instanceof String) {
            // For String, keep it as is
            dataString = (String) data;
        } else {
                // Handle the case when it's not convertible to a Number
                throw new IllegalArgumentException("Unsupported data type: " + data.getClass().getSimpleName());
            }
        byte[] encryptedBytes = cipher.doFinal(dataString.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey secretKeySpec = new SecretKeySpec(licenseSecretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
