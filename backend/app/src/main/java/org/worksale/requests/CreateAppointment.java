package org.worksale.requests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreateAppointment {

    public LocalDateTime startDate;
    public LocalDateTime endDate;

    public List<UUID> userIds;
    public List<UUID> customerIds;

    public UUID organizerId;

    public String description;

    public CreateAppointment() {
    }
}
