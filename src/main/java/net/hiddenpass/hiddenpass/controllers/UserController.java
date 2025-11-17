package net.hiddenpass.hiddenpass.controllers;

import jakarta.validation.Valid;
import net.hiddenpass.hiddenpass.enumerations.ETypeUser;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.responseDTO.*;
import net.hiddenpass.hiddenpass.security.jwt.JwtUtils;
import net.hiddenpass.hiddenpass.service.KeyStoreService;
import net.hiddenpass.hiddenpass.service.RegisterLinkService;
import net.hiddenpass.hiddenpass.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final KeyStoreService keyStoreService;
    private final RegisterLinkService registerLinkService;

    public UserController(UserService userService, JwtUtils jwtUtils, KeyStoreService keyStoreService, RegisterLinkService registerLinkService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.keyStoreService = keyStoreService;
        this.registerLinkService = registerLinkService;
    }

    //This endpoint only access to rol [ADMIN].
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> userEntityList = this.userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userEntityList);
    }

    //This endpoint only access to rol [ADMIN].
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<UserEntity>> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(email));
    }

    @PostMapping("/create")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterDTO registerDTO) throws Exception {
        if (userService.createUser(registerDTO, ETypeUser.USER).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    //This endpoint only access to rol [USER, ADMIN].
    @PatchMapping("/update-user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Optional<UserEntity>> updateUser( @RequestBody UserEntity user) {

        Optional<UserEntity> userEntity = this.userService.updateUser(user);

        if (userEntity.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userEntity);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //¡¡¡¡¡IMPORTANT CORRECT !!!!!!!
    //change Pathvariable to RequestBody DTO
    // This endpoint only access to rol [ADMIN]
    @DeleteMapping("/delete-user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable String email) {
        if (userService.deleteUser(email)) {
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //This method update the user email
    //receive the email in token
    //The email here is the subject in the token
    @PatchMapping("/update-email")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updateUserEmail(@Valid @RequestBody UpdateEmailUserDTO updateEmailUserDTO, @RequestHeader("Authorization") String token) {

        if (jwtUtils.validateToken(token)) {
            String userEmail = userService.getUsernameFromToken(token);

            Optional<UpdateEmailUserDTO> userDTO = userService.updateEmailUser(userEmail, updateEmailUserDTO);
            if (userDTO.isPresent()) {
                UpdateEmailUserDTO result = userDTO.get();
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            throw new IllegalArgumentException("User not found");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //method to check if token is enabled
    @GetMapping("/enabled-token")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Boolean> enabledToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.enabledToken(token));
    }

    @GetMapping("/checktoken")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Optional<?>> checkToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.checkStatusUser(token));
    }

    @GetMapping("/generate-register-token/{email}")
    public ResponseEntity<String> generateAccessToken(@PathVariable String email) throws Exception {
        registerLinkService.generateAccessToken(email, 900000L);
        return ResponseEntity.status(HttpStatus.OK).body("Register link generated successfully");
    }

    @GetMapping("/check-register-token")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<Boolean> checkRegisterToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.enabledToken(token));
    }

    @PostMapping("/checkmail")
    public ResponseEntity<Boolean> checkEmail(@Valid @RequestBody ExistEmailDTO emailDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.existEmail(emailDTO));
    }

    @GetMapping("/pk")
    public ResponseEntity<String> getPublicKey() throws Exception {
        //    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        return ResponseEntity.status(HttpStatus.OK).body(keyStoreService.getPublicKey());
    }

    @GetMapping("/salt")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'SUPER_ADMI')")
    public ResponseEntity<IvAndSaltDTO> getSalt(@RequestHeader("Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            String tokenExtract = token.substring(7);

            if(jwtUtils.validateToken(tokenExtract)) {
                String email = userService.getUsernameFromToken(token);
                return ResponseEntity.status(HttpStatus.OK).body(userService.getIvAndSalt(email));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/subscriber")
    public ResponseEntity<String> subscriber(@RequestBody EventSubscriberDTO eventSubscriberDTO) {
        if(userService.subscribeUser(eventSubscriberDTO)) {
            return ResponseEntity.status(HttpStatus.OK).body("User subscribed successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
