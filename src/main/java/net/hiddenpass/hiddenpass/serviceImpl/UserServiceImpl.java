package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.models.*;
import net.hiddenpass.hiddenpass.repository.AccessCodeRepository;
import net.hiddenpass.hiddenpass.repository.UserRepository;
import net.hiddenpass.hiddenpass.responseDTO.ExistEmailDTO;
import net.hiddenpass.hiddenpass.responseDTO.UpdateEmailUserDTO;
import net.hiddenpass.hiddenpass.security.jwt.JwtUtils;
import net.hiddenpass.hiddenpass.service.AccessCodeService;
import net.hiddenpass.hiddenpass.service.EncryptionUtilsService;
import net.hiddenpass.hiddenpass.service.KeyStoreService;
import net.hiddenpass.hiddenpass.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AccessCodeService accessCodeService;
    private final AccessCodeRepository accessCodeRepository;
    private final EncryptionUtilsService encryptionUtils;
    private final KeyStoreService keyStoreService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           AccessCodeService accessCodeService,
                           AccessCodeRepository accessCodeRepository,
                           EncryptionUtilsService encryptionUtils,
                           KeyStoreService keyStoreService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.accessCodeService = accessCodeService;
        this.accessCodeRepository = accessCodeRepository;
        this.encryptionUtils = encryptionUtils;
        this.keyStoreService = keyStoreService;
    }

    /**
     * get all user across all db
     * @return all user
     */
    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    /**
     * get a specify user
     * @param email user
     * @return all user info
     */
    @Override
    public Optional<UserEntity> getUser(String email) {
        return userRepository.findByUsername(email);
    }

    /**
     * create user with permission of USER or ADMIN,
     * it depends on whether  you send the code ore not
     * @param user object with all params
     * @return empty
     */
    @Override
    public Optional<UserEntity> createUser(UserEntity user) throws Exception{

        byte[] encryptedName = keyStoreService.exportBase64ToArray(user.getName());
        byte[] encryptedUsername = keyStoreService.exportBase64ToArray(user.getUsername());
        byte[] encryptedPassword = keyStoreService.exportBase64ToArray(user.getPassword());

        String decryptedName = keyStoreService.decryptDataWithRSA(encryptedName);
        String decryptedUsername = keyStoreService.decryptDataWithRSA(encryptedUsername);
        String decryptedPassword = keyStoreService.decryptDataWithRSA(encryptedPassword);

        user.setName(decryptedName);
        user.setUsername(decryptedUsername);
        user.setPassword(decryptedPassword);

        RoleEntity roleEntity = new RoleEntity();
        Set<RoleEntity> roles = new HashSet<>();

        Optional<UserEntity> userExisting = userRepository.findByUsername(user.getUsername());

        if (userExisting.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        } if (user.getAccessCode() != null) {
            Long code = Long.parseLong(user.getAccessCode());

            Optional<AccessCodeEntity> accessCodeEntityOptional = accessCodeRepository.findById(code);
            if (accessCodeEntityOptional.isPresent()) {

                AccessCodeEntity accessCodeEntity = accessCodeEntityOptional.get();
                if (accessCodeEntity.getActive()) {
                    accessCodeEntity.setActive(false);

                    roleEntity.setRole(ERol.ADMIN);
                    roles.add(roleEntity);
                    user.setRoles(roles);

                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setUserSalt(encryptionUtils.ivOrSalt());
                    user.setUserIv(encryptionUtils.ivOrSalt());

                    this.userRepository.save(user);
                    this.accessCodeRepository.save(accessCodeEntity);

                    return Optional.empty();
                }
                else {
                    throw new IllegalArgumentException("Access code is inactive");
                }
            }

        } else if (user.getAccessCode() == null) {
            roleEntity.setRole(ERol.USER);
            roles.add(roleEntity);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserSalt(encryptionUtils.ivOrSalt());
            user.setUserIv(encryptionUtils.ivOrSalt());

            this.userRepository.save(user);
            return Optional.empty();
        }
        return Optional.empty();
    }

    /**
     * update user
     * @param user object with any params
     * @return object UserEntity
     */
    @Override
    public Optional<UserEntity> updateUser(UserEntity user) {
        Optional<UserEntity> existingUser = this.userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            UserEntity existingUserEntity = existingUser.get();

            if(existingUserEntity.getUsername().equals(user.getUsername())) {

                if(existingUserEntity.getName() != null) {
                    existingUserEntity.setName(user.getName());
                }
                if(existingUserEntity.getUsername() != null) {
                    existingUserEntity.setUsername(user.getUsername());
                }
                this.userRepository.save(existingUserEntity);
            }
            return Optional.of(existingUserEntity);
        }
        return Optional.empty();
    }

    /**
     * delete user
     * @param email user to delete
     * @return value type boolean
     */
    @Override
    public boolean deleteUser(String email) {

        Optional<UserEntity> existingUser = this.userRepository.findByUsername(email);
        if (existingUser.isPresent()) {
            UserEntity existingUserEntity = existingUser.get();

            if (existingUserEntity.getUsername().equals(email)) {
                this.userRepository.delete(existingUserEntity);
                return true;
            }
        }
        return false;
    }

    /**
     * delete password from user
     * @param id user to delete specify password
     * @param emailUser email user
     * @return value type boolean
     */
     @Override
     @Transactional
    public boolean deleteIdPassword(Long id, String emailUser) {

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(emailUser);

        if (userEntityOptional.isPresent()) {

            UserEntity existingUserEntity = userEntityOptional.get();
            List<PassWordEntity> listPasswords = existingUserEntity.getListPass();
            for (PassWordEntity passWordEntity : listPasswords) {
                if (passWordEntity.getId().equals(id)) {
                    listPasswords.remove(passWordEntity);
                    existingUserEntity.setListPass(listPasswords);
                    this.userRepository.save(existingUserEntity);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param emailUser email from token
     * @param updateEmailUserDTO new email user
     * @return Optional<UpdateEmailUserDTO>
     */
    //Method to update user email
    @Override
    public Optional<UpdateEmailUserDTO> updateEmailUser(String emailUser, UpdateEmailUserDTO updateEmailUserDTO) {
        Optional<UserEntity> existingUser = this.userRepository.findByUsername(emailUser);
        if (existingUser.isPresent()) {
            UserEntity existingUserEntity = existingUser.get();
            if (!existingUserEntity.getUsername().equals(updateEmailUserDTO.getEmail())) {
                existingUserEntity.setUsername(updateEmailUserDTO.getEmail());
                this.userRepository.save(existingUserEntity);

                UpdateEmailUserDTO userDTO = new UpdateEmailUserDTO();
                userDTO.setEmail(updateEmailUserDTO.getEmail());
                return Optional.of(userDTO);
            }
            throw new IllegalArgumentException("Email already exist");
        }
        return Optional.empty();
    }

    /**
     * extract username from email
     * @param token token user
     * @return username from user
     */
    @Override
    public String getUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String tokenExtract = token.substring(7);
            return jwtUtils.getEmailFromToken(tokenExtract);
        }
        throw new IllegalArgumentException("Invalid token");
    }

    /**
     * check if the token is active
     * @return data ype boolean
     */
    @Override
    public boolean enabledToken(String token) {

        if (token != null && token.startsWith("Bearer ")) {
            String tokenExtract = token.substring(7);
            return jwtUtils.validateToken(tokenExtract);
        }
        throw new IllegalArgumentException("Invalid token");
    }

    /**
     * generate access code to create a user with permissions of ADMIN
     * @return access code
     */
    @Override
    public AccessCodeEntity generateAccessCode() {
        return accessCodeService.generateAccessCode();
    }

    /**
     * method to verify that email exist
     * @param emailDTO object email user to verify
     * @return value type bool
     */
    @Override
    public Boolean existEmail(ExistEmailDTO emailDTO) {
        Optional<UserEntity> existingUser = userRepository.findByUsername(emailDTO.getEmail());

        return existingUser.isPresent();
    }
}
