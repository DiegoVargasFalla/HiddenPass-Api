package net.hiddenpass.hiddenpass.controllers;


import jakarta.validation.Valid;
import net.hiddenpass.hiddenpass.models.PassWordEntity;
import net.hiddenpass.hiddenpass.models.SecurePasswordEntity;
import net.hiddenpass.hiddenpass.responseDTO.PasswordEntityDTO;
import net.hiddenpass.hiddenpass.responseDTO.DeleteIdPasswordFromUserDTO;
import net.hiddenpass.hiddenpass.responseDTO.MasterKeyDTO;
import net.hiddenpass.hiddenpass.responseDTO.SecurePasswordDTO;
import net.hiddenpass.hiddenpass.service.PasswordService;
import net.hiddenpass.hiddenpass.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class PasswordController {

    private final PasswordService passwordService;
    private final UserService userService;

    public PasswordController(PasswordService passwordService, UserService userService) {
        this.passwordService = passwordService;
        this.userService = userService;
    }

    /**
     * endpoint to get all user password
     * @return all users password
     */
    //This method access [ADMIN]
    @GetMapping("/passwords")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PassWordEntity>> getAllPasswords() {
        return ResponseEntity.status(HttpStatus.OK).body(passwordService.getPassWords());
    }

    /**
     * get a specify user password with id
     * @param id  user
     * @return all password information
     */
    //This method access [ADMIN]
    @GetMapping("/password/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<PassWordEntity>> getPassword(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(passwordService.getPassWord(id));
    }

    /**
     * add password to a user, This method access [USER, ADMIN]
     * @param passwordDTO Object from PasswordEntity
     * @param token in request from user and extract email
     * @return password created
     */
    @PostMapping("/add-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> createPassword(@RequestBody PasswordEntityDTO passwordDTO, @RequestHeader("Authorization") String token) throws Exception{
        String username = userService.getUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No email user found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordService.createPassWord(username, passwordDTO));
    }

    /**
     * This method access [USER, ADMIN], deleted user password and received
     * a object DeleteIdPasswordFromUserDTO that have two attributes
     * @param deleteIdPasswordFromUser object received
     * @return response status OK
     */
    @DeleteMapping("/delete-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> deletePassword( @RequestHeader("Authorization") String token, @Valid @RequestBody DeleteIdPasswordFromUserDTO deleteIdPasswordFromUser) {

        String emailUser = userService.getUsernameFromToken(token);
        if(emailUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No email user found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(passwordService.deletePassWord(deleteIdPasswordFromUser.getIdPassword(), emailUser));
    }

    /**
     * get a specify user password with token
     * @param token in the request
     * @return object PasswordEntity
     */
    @PostMapping("/passwords-user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    //change the arriving email attribute in the token
    public ResponseEntity<?> getPasswordsUser(@RequestHeader("Authorization") String token, @Valid @RequestBody MasterKeyDTO masterKeyDTO) throws Exception{
        String userEmail = userService.getUsernameFromToken(token);
        if(userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No email user found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(passwordService.getPassWordsFromUser(userEmail, masterKeyDTO));
    }


    //IMPORTANT CORRECT
    //delete variables Object
    /**
     * This method access [USER, ADMIN]
     * @param passwordEntityDTO manage the email with the token
     * @return Object PasswordEntity or error
     */
    @PatchMapping("/update-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String token, @Valid @RequestBody PasswordEntityDTO passwordEntityDTO) throws Exception {

        String userEmail = userService.getUsernameFromToken(token);
        if(userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No email user found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(passwordService.updatePassWord(userEmail, passwordEntityDTO));
    }

    /**
     * Check the password if it´s secure
     * @param securePasswordDTO object
     * @return object with data about password; entropy,
     * feedback and how many attempts are needed to crack the password
     */
    @PostMapping("/check-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> checkPassword(@Valid @RequestBody SecurePasswordDTO securePasswordDTO) {
        Optional<SecurePasswordEntity> securePasswordExisting = passwordService.checkSecurePassword(securePasswordDTO);
        if (securePasswordExisting.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(securePasswordExisting);
        } else {
            throw new NullPointerException("An error occurred");
        }
    }

    /**
     * generate secure password
     * @return secure password
     */
    @GetMapping("/generate-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> generatePassword() {
        return ResponseEntity.status(HttpStatus.OK).body(passwordService.generateSecurePassword());
    }
}
