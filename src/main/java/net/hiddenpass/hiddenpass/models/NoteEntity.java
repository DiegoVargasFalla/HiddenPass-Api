package net.hiddenpass.hiddenpass.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String title;

    @Lob
    @Column( nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column( nullable = false, updatable = false)
    private Instant creationDateServer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDateClient;

    @Column(nullable = false, updatable = false)
    private String zoneDateTimeClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserEntity user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreationDateServer() {
        return creationDateServer;
    }

    public void setCreationDateServer(Instant creationDateServer) {
        this.creationDateServer = creationDateServer;
    }

    public LocalDateTime getCreationDateClient() {
        return creationDateClient;
    }

    public void setCreationDateClient(LocalDateTime creationDateClient) {
        this.creationDateClient = creationDateClient;
    }

    public String getZoneDateTimeClient() {
        return zoneDateTimeClient;
    }

    public void setZoneDateTimeClient(String zoneDateTimeClient) {
        this.zoneDateTimeClient = zoneDateTimeClient;
    }

    public void setDateTimeClient(String isoDateTime, String zone) {
        if(zoneDateTimeClient != null) {
            Instant instant = Instant.parse(isoDateTime);
            ZonedDateTime zoneDateTime = instant.atZone(ZoneId.of(zone));
            creationDateClient = zoneDateTime.toLocalDateTime();
        }
    }
}
