package net.hiddenpass.hiddenpass.responseDTO;

public class IvAndSaltDTO {
    private String iv;
    private String salt;

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
