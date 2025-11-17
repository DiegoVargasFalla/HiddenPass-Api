package net.hiddenpass.hiddenpass.serviceImpl;

import jakarta.mail.MessagingException;
import net.hiddenpass.hiddenpass.enumerations.ETypeUser;
import net.hiddenpass.hiddenpass.models.RoleEntity;
import net.hiddenpass.hiddenpass.repository.RoleRepository;
import net.hiddenpass.hiddenpass.responseDTO.UserRegisterDTO;
import net.hiddenpass.hiddenpass.security.jwt.JwtUtils;
import net.hiddenpass.hiddenpass.service.RegisterLinkService;
import net.hiddenpass.hiddenpass.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RegisterLinkServiceImpl implements RegisterLinkService {

    private final JwtUtils jwtUtils;
    public final GmailServiceImpl gmailServiceImpl;
    private final UserService userService;

    public RegisterLinkServiceImpl( JwtUtils jwtUtils, GmailServiceImpl gmailServiceImpl, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.gmailServiceImpl = gmailServiceImpl;
        this.userService = userService;
    }
    /**
     * generate access code to give permission of ADMIN
     */
    @Override
    public void generateAccessToken(String email, Long expirationTime) throws Exception {

        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername(email);
        registerDTO.setPassword("tempPassword");
        userService.createUser(registerDTO, ETypeUser.USER);


        CompletableFuture.supplyAsync(() -> {
            try {
                gmailServiceImpl.sendMail(
                        email,
                        "Link de registro",
                        "https://hiddenpass.net/register?token=" + jwtUtils.generateAccessToken(email, expirationTime)
                );
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return null;
        })
                .exceptionally(error -> {
                    System.out.println("Error sending mail: " + error.getMessage());
                    return null;
                });
    }
}
