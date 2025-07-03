package net.hiddenpass.hiddenpass.responseDTO;

import jakarta.validation.constraints.NotBlank;

public class MasterKeyDTO {

    @NotBlank(message = "cannot be empty")
    private String masterKey;

    private String aesKey;
    private String ivFront;

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getIvFront() {
        return ivFront;
    }

    public void setIvFront(String iv) {
        this.ivFront = iv;
    }

}
