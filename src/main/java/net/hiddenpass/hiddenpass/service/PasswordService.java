package net.hiddenpass.hiddenpass.service;

import net.hiddenpass.hiddenpass.models.PassWordEntity;
import net.hiddenpass.hiddenpass.models.SecurePasswordEntity;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.responseDTO.MasterKeyDTO;
import net.hiddenpass.hiddenpass.responseDTO.PasswordEntityDTO;
import net.hiddenpass.hiddenpass.responseDTO.PasswordResponseDTO;
import net.hiddenpass.hiddenpass.responseDTO.SecurePasswordDTO;

import java.util.List;
import java.util.Optional;

public interface PasswordService {
    List<PassWordEntity> getPassWords();
    Optional<PassWordEntity> getPassWord(Long id);
    Optional<PassWordEntity> createPassWord(String email, PasswordResponseDTO passwordResponseDTO) throws Exception;
//    boolean checkPasswordOrUrlUniqueFromUser(String urlOrEmail, UserEntity user, String dataType, String masterKey) throws Exception;
    boolean deletePassWord(Long id, String emailUser);
    List<PasswordResponseDTO> getPassWordsFromUser(String emailUser) throws Exception;
    Optional<?> updatePassWord(String email, PasswordEntityDTO passwordEntityDTO);
    Optional<SecurePasswordEntity> checkSecurePassword(SecurePasswordDTO securePasswordDTO);
    String generateSecurePassword();
}
