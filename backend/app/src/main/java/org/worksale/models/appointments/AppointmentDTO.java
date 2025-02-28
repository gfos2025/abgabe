package org.worksale.models.appointments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppointmentDTO {
    private UUID id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime endTime;
    private String description;

    private List<AppointmentParticipantDTO> participants;

    public AppointmentDTO(UUID id, LocalDateTime start_time, LocalDateTime end_time, String description,
            List<AppointmentParticipantDTO> participants) {
        this.id = id;
        this.startTime = start_time;
        this.endTime = end_time;
        this.description = description;
        this.participants = participants;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime start_time) {
        this.startTime = start_time;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime end_time) {
        this.endTime = end_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AppointmentParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<AppointmentParticipantDTO> participants) {
        this.participants = participants;
    }
}
