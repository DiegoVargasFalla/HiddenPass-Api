package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.responseDTO.NoteEntityDTO;
import net.hiddenpass.hiddenpass.responseDTO.PasswordEntityDTO;
import net.hiddenpass.hiddenpass.service.KeyStoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    @Value("${keystore.path}")
    private String PATH_STORE;

    @Value("${keystore.password}")
    private String PASS_STORE;

    @Value("${key.alias}")
    private String ALIAS_STORE;

    private KeyPair keyPair;


    @Override
    public void unlockStore() throws Exception {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(PATH_STORE)) {
            keyStore.load(fis, PASS_STORE.toCharArray());
        } catch (Exception e) {
            throw new Exception(e);
        }

        Key privateKey = keyStore.getKey(ALIAS_STORE, PASS_STORE.toCharArray());

        if (privateKey == null) {
            throw new IllegalStateException("Clave privada no encontrada para el alias proporcionado.");
        }

        Certificate certificate = keyStore.getCertificate(ALIAS_STORE);
        PublicKey publicKey = certificate.getPublicKey();

        keyPair = new KeyPair(publicKey, (PrivateKey) privateKey);
    }

    @Override
    public String getPublicKey() throws Exception {
        if (keyPair == null) {
            unlockStore();
        }
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    @Override
    public byte[] decryptAES(String keyAES) throws Exception {

        byte[] encryptedBytes = Base64.getDecoder().decode(keyAES);

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate(), oaepParameterSpec);

        return cipher.doFinal(encryptedBytes);
    }

    @Override
    public String decryptMasterKey(byte[] masterKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate(), oaepParameterSpec);

        return new String(cipher.doFinal(masterKey), StandardCharsets.UTF_8);
    }

    @Override
    public String decryptDataWithRSA(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate(), oaepParameterSpec);

        return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
    }

    @Override
    public byte[] exportBase64ToArray(String value) throws Exception {
        return Base64.getDecoder().decode(value);
    }

    @Override
    public String decryptDataWithAES(byte[] data, byte[] aesKeyBytes, byte[] ivBytes) throws Exception {
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);

        byte[] originalData = aesCipher.doFinal(data);
        return new String(originalData, StandardCharsets.UTF_8);
    }

    @Override
    public String encryptDataWithAES(String data, byte[] aesKeyBytes, byte[] ivBytes) throws Exception {
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);

        byte[] encryptedBytes = aesCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return  Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public NoteEntityDTO decryptAllDataNote(NoteEntityDTO noteDTO) throws Exception {

        byte[] encryptedTitle = this.exportBase64ToArray(noteDTO.getTitle());
        byte[] encryptedContent = this.exportBase64ToArray(noteDTO.getContent());
        byte[] encryptedMasterKey = this.exportBase64ToArray(noteDTO.getMasterKey());
        byte[] AesIv = this.exportBase64ToArray(noteDTO.getIvFront());
        byte[] decryptedAESKeyFront = this.decryptAES(noteDTO.getEncryptedAesKey());

        String masterKey = this.decryptMasterKey(encryptedMasterKey);
        String title = this.decryptDataWithAES(encryptedTitle, decryptedAESKeyFront, AesIv);
        String content = this.decryptDataWithAES(encryptedContent, decryptedAESKeyFront, AesIv);

        noteDTO.setTitle(title);
        noteDTO.setContent(content);
        noteDTO.setMasterKey(masterKey);
        return noteDTO;
    }

    @Override
    public PasswordEntityDTO decryptAllDataPassword(PasswordEntityDTO passwordDTO) throws Exception {
        byte[] ivFront = exportBase64ToArray(passwordDTO.getIvFront());
        byte[] decryptedAesKeyFront = decryptAES(passwordDTO.getAesKey());


        if (passwordDTO.getPassWord().getPassword() != null) {
            byte[] encryptedPassword = exportBase64ToArray(passwordDTO.getPassWord().getPassword());
            String decryptPassword = decryptDataWithAES(encryptedPassword, decryptedAesKeyFront, ivFront);
            passwordDTO.getPassWord().setPassword(decryptPassword);
        }
        if (passwordDTO.getPassWord().getUsername() != null) {
            byte[] encryptedUsername = exportBase64ToArray(passwordDTO.getPassWord().getUsername());
            String decryptUsername = decryptDataWithAES(encryptedUsername, decryptedAesKeyFront, ivFront);
            passwordDTO.getPassWord().setUsername(decryptUsername);
        }
        if (passwordDTO.getPassWord().getUrl() != null) {
            byte[] encryptedUrl = exportBase64ToArray(passwordDTO.getPassWord().getUrl());
            String decryptUrl = decryptDataWithAES(encryptedUrl, decryptedAesKeyFront, ivFront);
            passwordDTO.getPassWord().setUrl(decryptUrl);
        }
        if (passwordDTO.getPassWord().getNote() != null) {
            byte[] encryptedNote = exportBase64ToArray(passwordDTO.getPassWord().getNote());
            String decryptNote = decryptDataWithAES(encryptedNote, decryptedAesKeyFront, ivFront);
            passwordDTO.getPassWord().setNote(decryptNote);

        }

        byte[] encryptedMasterKey = exportBase64ToArray(passwordDTO.getMasterKey());

        String masterKey = this.decryptMasterKey(encryptedMasterKey);
        passwordDTO.setMasterKey(masterKey);

        return passwordDTO;
    }
}
