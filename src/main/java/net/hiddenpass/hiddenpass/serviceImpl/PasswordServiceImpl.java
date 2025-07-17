package net.hiddenpass.hiddenpass.serviceImpl;

import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.resources.Configuration;
import me.gosimple.nbvcxz.resources.ConfigurationBuilder;
import me.gosimple.nbvcxz.resources.Feedback;
import me.gosimple.nbvcxz.resources.Generator;
import me.gosimple.nbvcxz.scoring.Result;
import net.hiddenpass.hiddenpass.models.PassWordEntity;
import net.hiddenpass.hiddenpass.models.SecurePasswordEntity;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.repository.UserRepository;
import net.hiddenpass.hiddenpass.repository.PasswordRepository;
//import net.hiddenpass.hiddenpass.responseDTO.MasterKeyDTO;
import net.hiddenpass.hiddenpass.responseDTO.PasswordEntityDTO;
import net.hiddenpass.hiddenpass.responseDTO.PasswordResponseDTO;
import net.hiddenpass.hiddenpass.responseDTO.SecurePasswordDTO;
//import net.hiddenpass.hiddenpass.service.EncryptionUtilsService;
import net.hiddenpass.hiddenpass.service.KeyStoreService;
import net.hiddenpass.hiddenpass.service.PasswordService;
//import net.hiddenpass.hiddenpass.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import javax.crypto.SecretKey;
import java.util.*;

@Service
public class PasswordServiceImpl implements PasswordService {

    public final PasswordRepository passwordRepository;
    public final UserRepository userRepository;
//    private final UserService userService;
//    private final EncryptionUtilsService encryptionUtils;
    private final KeyStoreService keyStoreService;

    public PasswordServiceImpl(PasswordRepository passwordRepository,
                               UserRepository userRepository,
//                               UserService userService,
//                               EncryptionUtilsService encryptionUtils,
                               KeyStoreService keyStoreService) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
//        this.userService = userService;
//        this.encryptionUtils = encryptionUtils;
        this.keyStoreService = keyStoreService;
    }

    /**
     * get all passwords
     * @return return all passwords
     */
    @Override
    public List<PassWordEntity> getPassWords() {
        return passwordRepository.findAll();
    }


    //Get password of one user in across all db

    /**
     *Get password a user in across all db
     * @param id id password
     * @return one object PasswordEntity
     */
    @Override
    public Optional<PassWordEntity> getPassWord(Long id) {
        return passwordRepository.findById(id);
    }

    //Add password specific user

    /**
     * Method to add passwords to a user
     * @param email user email  to add password
     * @param passwordResponseDTO object to add password
     * @return object password
     */
    @Override
    @Transactional
    public Optional<PassWordEntity> createPassWord(String email, PasswordResponseDTO passwordResponseDTO) throws Exception{

//        PasswordEntityDTO passWordDTO = keyStoreService.decryptAllDataPassword(passwordEntityDTO);

        Optional<UserEntity> userExisting = userRepository.findByUsername(email);

        if (userExisting.isPresent()) {
            UserEntity user = userExisting.get();

//            boolean checkPass = checkPasswordOrUrlUniqueFromUser(passWordDTO.getPassWord().getPassword(), user, "P", passWordDTO.getMasterKey());
//            boolean checkUrl = checkPasswordOrUrlUniqueFromUser(passWordDTO.getPassWord().getUrl(), user, "U", passWordDTO.getMasterKey());

//            if (!checkPass) {
//                throw new IllegalArgumentException("The password already exists");
//            }
//            if (!checkUrl) {
//                throw new IllegalArgumentException("The url already exists");
//            }

            //get salt and iv from user
//            String salt = user.getUserSalt();
//            String iv = user.getUserIv();

//            SecretKey derivedKey =  encryptionUtils.deriveKey(passWordDTO.getMasterKey(), salt);
//            String encryptedPassword = encryptionUtils.encrypt(passWordDTO.getPassWord().getPassword(), derivedKey, iv);

//            passWordDTO.getPassWord().setPassword(user.getPassword());

            PassWordEntity passWordEntity = new PassWordEntity();
            passWordEntity.setPassword(passwordResponseDTO.getPassword());
            passWordEntity.setUsername(passwordResponseDTO.getUsername());
            passWordEntity.setUrl(passwordResponseDTO.getUrl());
            passWordEntity.setNote(passwordResponseDTO.getNote());
            passWordEntity.setUser(user);
//            passWordDTO.getPassWord().setUser(user);

            PassWordEntity passwordSaved = passwordRepository.save(passWordEntity);

            passWordEntity.setId(passwordSaved.getId());
            return Optional.of(passWordEntity);
        }
        throw new IllegalArgumentException("The user does not exist");
    }

    //Get password from a specific user

    /**
     * get password of specify user
     * @param emailUser user email to get passwords
     * @return listPasswordEntity, list of password from user
     */
    @Override
    @Transactional
    public List<PasswordResponseDTO> getPassWordsFromUser(String emailUser){

//        byte[] decryptedAES = keyStoreService.decryptAES(masterKeyDTO.getAesKey());
//        byte[] ivFront = keyStoreService.exportBase64ToArray(masterKeyDTO.getIvFront());
//        byte[] encryptedMasterKey = keyStoreService.exportBase64ToArray(masterKeyDTO.getMasterKey());

//        String masterKey = keyStoreService.decryptMasterKey(encryptedMasterKey);

        Optional<UserEntity> userExisting = userRepository.findByUsername(emailUser);
        if (userExisting.isPresent()) {

            UserEntity user = userExisting.get();
//            byte[] salt = user.getUserSalt();
//            byte[] iv = user.getUserIv();

//            SecretKey derivedKey = encryptionUtils.deriveKey(masterKey, salt);
            ArrayList<PasswordResponseDTO> passwordResponseListDTO = new ArrayList<>();

            for (PassWordEntity password: user.getListPass()) {
                try {
                    PasswordResponseDTO passwordResponseDTO = new PasswordResponseDTO();
//                    String passwordDecrypted = encryptionUtils.decrypt(password.getPassword(), derivedKey, iv);

//                    String passwordEncryptedWithAES = keyStoreService.encryptDataWithAES(passwordDecrypted, decryptedAES, ivFront);

                    passwordResponseDTO.setId(password.getId());
                    passwordResponseDTO.setUsername(password.getUsername());
                    passwordResponseDTO.setPassword(password.getPassword());
                    passwordResponseDTO.setUrl(password.getUrl());
                    passwordResponseDTO.setNote(password.getNote());

                    passwordResponseListDTO.add(passwordResponseDTO);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return passwordResponseListDTO;
        }
        return new ArrayList<>();
    }

    //This method verifies unique password or url from user

//    /**
//     * check password or url if exist
//     * @param passwordOrUrl password or email to change
//     * @param user object type UserEntity
//     * @param dataType letter to identify if the param is password or email
//     * @return boolean type to handle in the controller
//     */
//    @Override
    //@Transactional
//    public boolean checkPasswordOrUrlUniqueFromUser(String passwordOrUrl, UserEntity user, String dataType, String masterKey) throws Exception{
//
//        // get sal, iv and derived key
//        byte[] salt = user.getUserSalt();
//        byte[] iv = user.getUserIv();
//        SecretKey key =  encryptionUtils.deriveKey(masterKey, salt);
//
//        // get user existing
//        List<PassWordEntity> passWordEntityList = user.getListPass();
//
//        //Map each entity in a list encrypted passwords
//        //List<String> passwordsListEncrypted = passWordEntityList.stream().map(PassWordEntity::getPassword).toList();
//
//        //new list decrypted
//        List<String> passwordsListDecrypt = new ArrayList<>();
//        List<String> urlsList = new ArrayList<>();
//
//        // decrypt alls passwords
//        for (PassWordEntity password: passWordEntityList) {
//            try {
//                encryptionUtils.decrypt(password.getPassword(), key, iv);
//                passwordsListDecrypt.add(password.getPassword());
//                urlsList.add(password.getUrl());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        if (dataType.equals("P")) {
//            return !passwordsListDecrypt.contains(passwordOrUrl);
//        } else if (dataType.equals("U")) {
//            return !urlsList.contains(passwordOrUrl);
//        }
//        return false;
//    }

    /**
     * update object password
     * @param passwordEntityDTO new password to update
     * @param emailUser email to identify owner user of passwords,
     * this param must be from token
     * @return object to handle the response in the controller, pd: predefined
     */
    @Override
    @Transactional
    public Optional<PasswordResponseDTO> updatePassWord(String emailUser, PasswordEntityDTO passwordEntityDTO) {

//        PasswordResponseDTO passwordResponseDTO = new PasswordResponseDTO();
//
//        passwordResponseDTO.setId(passwordEntityDTO.getPassWord().getId());
//        passwordResponseDTO.setUsername(passwordEntityDTO.getPassWord().getUsername());
//        passwordResponseDTO.setPassword(passwordEntityDTO.getPassWord().getPassword());
//        passwordResponseDTO.setUrl(passwordEntityDTO.getPassWord().getUrl());
//        passwordResponseDTO.setNote(passwordEntityDTO.getPassWord().getNote());

//        PasswordEntityDTO decryptedPasswordEntityDTO = keyStoreService.decryptAllDataPassword(passwordEntityDTO);

        Optional<UserEntity> existingUser = userRepository.findByUsername(emailUser);
        if (existingUser.isPresent()) {

//            UserEntity user = existingUser.get();
//            byte salt = user.getUserSalt();
//            byte[] iv = user.getUserIv();


            Optional<PassWordEntity> ExistingPassWordEntity =  passwordRepository.findById(passwordEntityDTO.getPassWord().getId());
//            SecretKey key = encryptionUtils.deriveKey(decryptedPasswordEntityDTO.getMasterKey(), salt);


            if (ExistingPassWordEntity.isPresent()) {

                PassWordEntity passWordEntity = ExistingPassWordEntity.get();

                if (passWordEntity.getId().equals(passwordEntityDTO.getPassWord().getId())) {

                    if (passwordEntityDTO.getPassWord().getUsername() != null) {
                        passWordEntity.setUsername(passwordEntityDTO.getPassWord().getUsername());
                    }
                    if (passwordEntityDTO.getPassWord().getPassword() != null) {
//                        if (checkPasswordOrUrlUniqueFromUser(decryptedPasswordEntityDTO.getPassWord().getPassword(), user, "P", decryptedPasswordEntityDTO.getMasterKey())) {
//                            String encrypt = encryptionUtils.encrypt(decryptedPasswordEntityDTO.getPassWord().getPassword(), key, iv);
                        passWordEntity.setPassword(passwordEntityDTO.getPassWord().getPassword());
                    } else {
                        throw new IllegalArgumentException("Password already exists");
                    }
                }
                if (passwordEntityDTO.getPassWord().getUrl() != null) {
                    passWordEntity.setUrl(passwordEntityDTO.getPassWord().getUrl());
//                        if(checkPasswordOrUrlUniqueFromUser(decryptedPasswordEntityDTO.getPassWord().getUrl(), user, "U", decryptedPasswordEntityDTO.getMasterKey())) {
//
//                        } else {
//                            throw new IllegalArgumentException("Url already exists");
//                        }
                }
                if (passwordEntityDTO.getPassWord().getNote() != null) {
                    passWordEntity.setNote(passwordEntityDTO.getPassWord().getNote());
                }
                passwordRepository.save(passWordEntity);
                return Optional.of(new PasswordResponseDTO());
            }
        }
        return Optional.empty();
    }

    /**
     * delete password object from user
     * @param id password to delete
     * @param emailUser email to identify owner user of passwords, this param should be from token
     * @return boolean type to handle in the controller
     */
    @Override
    @Transactional
    public boolean deletePassWord(Long id, String emailUser) {

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(emailUser);

        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();

            Iterator<PassWordEntity> iterator = user.getListPass().iterator();
            while (iterator.hasNext()) {
                PassWordEntity passWordEntity = iterator.next();
                if (passWordEntity.getId().equals(id)) {
                    iterator.remove();
                    userRepository.save(user);
                    return true;
                }
            }
        }
        throw new IllegalArgumentException("User does not exist");
    }

    /**
     * verify if it's secure
     * @param securePasswordDTO object secure password with attributes
     * entropy, feedback and attempts needed to crack the password
     * @return object SecurePassword
     */
    @Override
    public Optional<SecurePasswordEntity> checkSecurePassword(SecurePasswordDTO securePasswordDTO) {
        Configuration configuration = new ConfigurationBuilder()
                .setLocale(Locale.forLanguageTag(securePasswordDTO.getLanguage()))
                .setMinimumEntropy(55d)
                .createConfiguration();
        Nbvcxz nbvcxz = new Nbvcxz(configuration);
        Result result = nbvcxz.estimate(securePasswordDTO.getPassword());
        Feedback feedback = result.getFeedback();

        List<String> listFeedback = new ArrayList<>();
        listFeedback.add(feedback.getWarning());
        listFeedback.addAll(feedback.getSuggestion());

        return Optional.of(new SecurePasswordEntity(result.getEntropy(), listFeedback, result.getGuesses()));
    }

    /**
     * generate secure password
     * @return secure password
     */
    public String generateSecurePassword() {
        return Generator.generateRandomPassword(Generator.CharacterTypes.ALPHANUMERICSYMBOL, 22);
    }
}
