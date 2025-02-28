package org.worksale.models.appointments;

import java.util.UUID;

public class AppointmentParticipantDTO {
    private UUID id;
    private String name;
    private UUID userId; // Represents the user_id if the participant is a User
    private UUID customerId; // Represents the customer_id if the participant is a Customer
    private AppointmentParticipentRole role;

    public AppointmentParticipantDTO(UUID id, String name, UUID userId, UUID customerId,
            AppointmentParticipentRole role) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.customerId = customerId;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public AppointmentParticipentRole getRole() {
        return role;
    }

    public void setRole(AppointmentParticipentRole role) {
        this.role = role;
    }
}