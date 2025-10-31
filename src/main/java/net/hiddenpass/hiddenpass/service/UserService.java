package net.hiddenpass.hiddenpass.service;

import net.hiddenpass.hiddenpass.enumerations.ETypeUser;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.responseDTO.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> getUsers();
    Optional<UserEntity> getUser(String email);
    Optional<UserEntity> createUser(UserRegisterDTO registerDTO, ETypeUser typeUser) throws Exception;
    Optional<UserEntity> updateUser(UserEntity user);
    boolean deleteUser(String email);
    boolean deleteIdPassword(Long id, String emailUser);
    Optional<UpdateEmailUserDTO> updateEmailUser(String email, UpdateEmailUserDTO updateEmailUserDTO);
    Optional<?> checkStatusUser(String token);
    boolean enabledToken(String token);
    String getUsernameFromToken(String token);
    Boolean existEmail(ExistEmailDTO email);
    IvAndSaltDTO getIvAndSalt(String email);
    boolean subscribeUser(EventSubscriberDTO subscriberDTO);
}
