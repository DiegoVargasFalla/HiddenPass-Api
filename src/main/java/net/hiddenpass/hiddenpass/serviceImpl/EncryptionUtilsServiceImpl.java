package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.service.EncryptionUtilsService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionUtilsServiceImpl implements EncryptionUtilsService {


    @Override
    public SecretKey deriveKey(String password, byte[] salt) throws Exception {
        int iterations = 600000;
        int keyLength = 256; //bits

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return factory.generateSecret(spec);
    }

    @Override
    public String encrypt(String plainText, SecretKey key, byte[] salt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(salt);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    @Override
    public String decrypt(String cipherText, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decode = Base64.getDecoder().decode(cipherText);
        return new String(cipher.doFinal(decode));
    }

    @Override
    public byte[] ivOrSalt() {
        byte[] ivOrSalt = new byte[16];
        new SecureRandom().nextBytes(ivOrSalt);
        return ivOrSalt;
    }
}
