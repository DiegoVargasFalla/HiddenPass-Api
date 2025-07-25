package net.hiddenpass.hiddenpass.responseDTO;

import net.hiddenpass.hiddenpass.models.RoleEntity;

import java.util.Set;

public class UserLoginDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
