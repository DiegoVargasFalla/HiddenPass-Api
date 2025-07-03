package net.hiddenpass.hiddenpass.service;

import javax.crypto.SecretKey;

public interface EncryptionUtilsService {
    String encrypt(String plainText, SecretKey key, byte[] iv) throws Exception;
    String decrypt(String cipherText, SecretKey key, byte[] iv) throws Exception;
    byte[] ivOrSalt();
    SecretKey deriveKey(String password, byte[] salt) throws Exception;
}
