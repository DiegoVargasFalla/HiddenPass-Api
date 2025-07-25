package net.hiddenpass.hiddenpass.responseDTO;

import net.hiddenpass.hiddenpass.models.AccessCodeEntity;

public class UserRegisterDTO {
    private String name;
    private String username;
    private AccessCodeEntity accessCode;
    private String password;
    private String userSalt;
    private String userIv;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccessCodeEntity getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(AccessCodeEntity accessCode) {
        this.accessCode = accessCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public String getUserIv() {
        return userIv;
    }

    public void setUserIv(String userIv) {
        this.userIv = userIv;
    }
}
