package net.hiddenpass.hiddenpass.service;

import net.hiddenpass.hiddenpass.responseDTO.MasterKeyDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityDTO;
import net.hiddenpass.hiddenpass.responseDTO.PasswordEntityDTO;

public interface KeyStoreService {
    String getPublicKey() throws Exception;
    void unlockStore() throws Exception;
    byte[] decryptAES(String keyAES) throws Exception;
    String decryptMasterKey(byte[] masterKey) throws Exception  ;
    String decryptDataWithAES(byte[] data, byte[] aesKeyBytes, byte[] ivBytes) throws Exception;
    String encryptDataWithAES(String data, byte[] aesKeyBytes, byte[] ivBytes) throws Exception;
    byte[] exportBase64ToArray(String value) throws Exception;
    NoteEntityDTO decryptAllDataNote(NoteEntityDTO noteDTO) throws Exception;
    PasswordEntityDTO decryptAllDataPassword(PasswordEntityDTO passwordDTO) throws Exception;
    String decryptDataWithRSA(byte[] data) throws Exception;
}
