package net.hiddenpass.hiddenpass.models;

import jakarta.persistence.*;

@Entity
@Table(name = "codes")
public class AccessCodeEntity {

    @Id
    private Long id;

    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
