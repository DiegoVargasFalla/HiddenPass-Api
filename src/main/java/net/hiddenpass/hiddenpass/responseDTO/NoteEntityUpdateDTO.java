package net.hiddenpass.hiddenpass.responseDTO;

public class NoteEntityUpdateDTO {
    private Long id;
    private String title;
    private String content;
    private String masterKey;
    private String aesKey;
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
