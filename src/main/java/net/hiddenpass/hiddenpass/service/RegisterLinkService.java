package net.hiddenpass.hiddenpass.service;

import java.util.Optional;

public interface RegisterLinkService {
    void generateAccessToken(String email, Long expirationTime) throws Exception;
}
