package net.hiddenpass.hiddenpass.controllers;

import jakarta.validation.Valid;
import net.hiddenpass.hiddenpass.models.AccessCodeEntity;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.responseDTO.ExistEmailDTO;
import net.hiddenpass.hiddenpass.responseDTO.UpdateEmailUserDTO;
import net.hiddenpass.hiddenpass.security.jwt.JwtUtils;
import net.hiddenpass.hiddenpass.service.KeyStoreService;
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

    public UserController(UserService userService, JwtUtils jwtUtils, KeyStoreService keyStoreService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.keyStoreService = keyStoreService;
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
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserEntity user) throws Exception {
        if (userService.createUser(user).isPresent()) {
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
    @DeleteMapping("delete-user/{email}")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> enabledToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.enabledToken(token));
    }

    //this method access only admin
    @GetMapping("/generate-access")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<AccessCodeEntity> generateAccess() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.generateAccessCode());
    }

    @GetMapping("/checktoken")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> checkToken(@RequestHeader("Authorization") String token) {
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getSalt(@RequestHeader("Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            String tokenExtract = token.substring(7);

            if(jwtUtils.validateToken(tokenExtract)) {
                String email = userService.getUsernameFromToken(token);
                return ResponseEntity.status(HttpStatus.OK).body(userService.getIvAndSalt(email));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
