package net.hiddenpass.hiddenpass.responseDTO;

public class NoteEntityDTO {
    private Long id;
    private String title;
    private String content;
    private String isoDate;
    private String zoneDateClient;
    private String masterKey;
    private String encryptedAesKey;
    private String ivFront;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getZoneDateClient() {
        return zoneDateClient;
    }

    public void setZoneDateClient(String zoneDateClient) {
        this.zoneDateClient = zoneDateClient;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public String getEncryptedAesKey() {
        return encryptedAesKey;
    }

    public void setEncryptedAesKey(String encryptedAesKey) {
        this.encryptedAesKey = encryptedAesKey;
    }

    public String getIvFront() {
        return ivFront;
    }

    public void setIvFront(String ivFront) {
        this.ivFront = ivFront;
    }

    public String getIsoDate() {
        return isoDate;
    }

    public void setIsoDate(String isoDate) {
        this.isoDate = isoDate;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }
}
