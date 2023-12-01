package com.activedge.usermgt.license;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.util.Base64;
public class FileDecryptor {

        public static void main(String[] args) {
            String encryptedFilePath = "path/to/encrypted/file";
            String decryptedFilePath = "path/to/decrypted/file";
            String decryptionKey = "yourDecryptionKey";

            try {
                byte[] encryptedData = Files.readAllBytes(Paths.get(encryptedFilePath));

                // Convert the decryption key from Base64 to byte array
                byte[] keyBytes = Base64.getDecoder().decode(decryptionKey);
                Key secretKey = new SecretKeySpec(keyBytes, "AES");

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

                byte[] decryptedData = cipher.doFinal(encryptedData);

                // Write the decrypted data to a new file
                Path decryptedPath = Paths.get(decryptedFilePath);
                Files.write(decryptedPath, decryptedData, StandardOpenOption.CREATE);

                System.out.println("File decrypted successfully.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
