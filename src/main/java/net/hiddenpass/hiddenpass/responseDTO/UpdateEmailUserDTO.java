package net.hiddenpass.hiddenpass.responseDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateEmailUserDTO {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format invalid")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
