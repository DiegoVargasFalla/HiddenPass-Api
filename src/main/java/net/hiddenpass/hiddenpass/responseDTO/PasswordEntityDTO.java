package net.hiddenpass.hiddenpass.responseDTO;

import net.hiddenpass.hiddenpass.models.PassWordEntity;

public class PasswordEntityDTO {
    private PassWordEntity passWord;
    private String masterKey;
    private String aesKey;
    private String ivFront;

    public PassWordEntity getPassWord() {
        return passWord;
    }

    public void setPassWord(PassWordEntity passWord) {
        this.passWord = passWord;
    }

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

    public void setIvFront(String ivFront) {
        this.ivFront = ivFront;
    }
}
