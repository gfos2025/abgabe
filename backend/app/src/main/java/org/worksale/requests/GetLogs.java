package org.worksale.requests;

import java.time.LocalDateTime;
import java.util.UUID;

import org.worksale.models.LogType;

public class GetLogs {
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public String userId;
    public LogType logType;
}
