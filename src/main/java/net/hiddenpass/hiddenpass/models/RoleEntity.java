package net.hiddenpass.hiddenpass.models;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private ERol role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ERol getRole() {
        return role;
    }

    public void setRole(ERol role) {
        this.role = role;
    }
}
