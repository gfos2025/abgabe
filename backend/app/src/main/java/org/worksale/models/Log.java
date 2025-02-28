package org.worksale.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

@Table(name = "logs")
@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LogType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = true)
    private UUID userId;

    public Log() {
    }

    public Log(String message, LogType level, LocalDateTime timestamp, UUID userId) {
        this.message = message;
        this.type = level;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LogType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(LogType level) {
        this.type = level;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
