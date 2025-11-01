package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.enumerations.ERol;
import net.hiddenpass.hiddenpass.enumerations.ETypeUser;
import net.hiddenpass.hiddenpass.models.*;
import net.hiddenpass.hiddenpass.repository.EventSubscriberRepository;
import net.hiddenpass.hiddenpass.repository.RoleRepository;
import net.hiddenpass.hiddenpass.repository.UserRepository;
import net.hiddenpass.hiddenpass.responseDTO.*;
import net.hiddenpass.hiddenpass.security.jwt.JwtUtils;
import net.hiddenpass.hiddenpass.service.RegisterLinkService;
import net.hiddenpass.hiddenpass.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EventSubscriberRepository eventSubscriberRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           EventSubscriberRepository eventSubscriberRepository,
                           RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.eventSubscriberRepository = eventSubscriberRepository;
        this.roleRepository = roleRepository;
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
     * @param registerDTO object with all params
     * @return empty
     */
    @Override
    @Transactional
    public Optional<UserEntity> createUser(UserRegisterDTO registerDTO, ETypeUser typeUser) {

        if(typeUser == ETypeUser.USER) {
            Optional<UserEntity> user = userRepository.findByUsername(registerDTO.getUsername());
            Optional<RoleEntity> roleExisting = roleRepository.findByRole(ERol.USER);

            if(user.isPresent()) {
                UserEntity userEntity = user.get();

                if(roleExisting.isPresent()) {
                    RoleEntity roleEntity = roleExisting.get();
                    Set<RoleEntity> roles = new HashSet<>();
                    roles.add(roleEntity);

                    userEntity.setRoles(roles);
                    userEntity.setName(registerDTO.getName());
                    userEntity.setUsername(registerDTO.getUsername());
                    userEntity.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                    userEntity.setUserIv(registerDTO.getUserIv());
                    userEntity.setUserSalt(registerDTO.getUserSalt());
                    userEntity.setCreationDate(LocalDate.now());

                    userEntity.setAccountNonExpired(true);
                    userEntity.setAccountNonLocked(true);
                    userEntity.setCredentialsNonExpired(true);
                    userEntity.setEnabled(true);

                    userRepository.save(userEntity);
                }
            } else {
                if(roleExisting.isPresent()) {
                    RoleEntity roleEntity = roleExisting.get();
                    Set<RoleEntity> roles = new HashSet<>();
                    roles.add(roleEntity);

                    UserEntity userTemp = new UserEntity();
                    userTemp.setUsername(registerDTO.getUsername());
                    userTemp.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                    userTemp.setAccountNonExpired(true);
                    userTemp.setAccountNonLocked(true);
                    userTemp.setCredentialsNonExpired(true);
                    userTemp.setEnabled(true);

                    userTemp.setRoles(roles);
                    userRepository.save(userTemp);
                    return Optional.of(userTemp);
                }
            }
        } else if (typeUser == ETypeUser.COMPANY) {
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
    public Optional<?> checkStatusUser(String token) {

        String tokenExtract = jwtUtils.extractToken(token);

        if (tokenExtract != null) {

            if(jwtUtils.validateToken(tokenExtract)) {

                String email = jwtUtils.getEmailFromToken(tokenExtract);
                Optional<UserEntity> userExisting =  userRepository.findByUsername(email);

                if(userExisting.isPresent()) {
                    UserStatusDTO userStatusDTO = getUserStatusDTO(userExisting);
                    return Optional.of(userStatusDTO);
                }
//                throw new IllegalArgumentException("User does not exist");
            }
        }
        throw new IllegalArgumentException("Invalid token");
    }

    @Override
    public boolean enabledToken(String token) {
        return jwtUtils.validateToken(jwtUtils.extractToken(token));
    }

    private static UserStatusDTO getUserStatusDTO(Optional<UserEntity> userExisting) {

        if(userExisting.isPresent()) {
            UserEntity existingUserEntity = userExisting.get();

            UserStatusDTO userStatusDTO = new UserStatusDTO();
            userStatusDTO.setEnabled(existingUserEntity.isEnabled());
            userStatusDTO.setAccountNonExpired(existingUserEntity.isAccountNonExpired());
            userStatusDTO.setCredentialsNonExpired(existingUserEntity.isCredentialsNonExpired());
            userStatusDTO.setAccountNonLocked(existingUserEntity.isAccountNonLocked());
            return userStatusDTO;
        }
        throw new IllegalArgumentException("User does not exist");
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

    @Override
    public IvAndSaltDTO getIvAndSalt(String email) {
        Optional<UserEntity> userExisting = userRepository.findByUsername(email);

        if(userExisting.isPresent()) {
            UserEntity userEntity = userExisting.get();

            IvAndSaltDTO ivAndSaltDTO = new IvAndSaltDTO();
            ivAndSaltDTO.setSalt(userEntity.getUserSalt());
            ivAndSaltDTO.setIv(userEntity.getUserIv());
            return ivAndSaltDTO;
        }
        throw new IllegalArgumentException("User not found");
    }

    @Override
    public boolean subscribeUser(EventSubscriberDTO subscriberDTO) {
        if(subscriberDTO.getEmail() != null) {
            EventSubscriber eventSubscriber = new EventSubscriber();
            eventSubscriber.setEmail(subscriberDTO.getEmail());
            eventSubscriberRepository.save(eventSubscriber);
            return true;
        }
        return false;
    }
}
