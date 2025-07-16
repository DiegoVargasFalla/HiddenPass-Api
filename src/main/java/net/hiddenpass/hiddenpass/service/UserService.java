package net.hiddenpass.hiddenpass.service;

import net.hiddenpass.hiddenpass.models.AccessCodeEntity;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.responseDTO.ExistEmailDTO;
import net.hiddenpass.hiddenpass.responseDTO.UpdateEmailUserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> getUsers();
    Optional<UserEntity> getUser(String email);
    Optional<UserEntity> createUser(UserEntity user) throws Exception;
    Optional<UserEntity> updateUser(UserEntity user);
    boolean deleteUser(String email);
    boolean deleteIdPassword(Long id, String emailUser);
    Optional<UpdateEmailUserDTO> updateEmailUser(String email, UpdateEmailUserDTO updateEmailUserDTO);
    boolean enabledToken(String token);
    String getUsernameFromToken(String token);
    AccessCodeEntity generateAccessCode();
    Boolean existEmail(ExistEmailDTO email);
}
