package net.hiddenpass.hiddenpass.responseDTO;

import jakarta.validation.constraints.Email;

public class ExistEmailDTO {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
