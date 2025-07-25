package net.hiddenpass.hiddenpass.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import net.hiddenpass.hiddenpass.enumerations.EBusyStatus;
import net.hiddenpass.hiddenpass.enumerations.EPayMethod;

import java.time.LocalDate;

@Entity
public class BuysEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float price;
    private EPayMethod payMethod;
    private EBusyStatus busyStatus;
    private LocalDate payDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserEntity user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public EPayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(EPayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public EBusyStatus getBusyStatus() {
        return busyStatus;
    }

    public void setBusyStatus(EBusyStatus busyStatus) {
        this.busyStatus = busyStatus;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
