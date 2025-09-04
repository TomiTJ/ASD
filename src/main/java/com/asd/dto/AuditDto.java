package com.asd.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;
import java.time.Instant;
import java.util.stream.Collector;
import com.asd.model.Audit;
import com.asd.model.Action;
import com.asd.model.ResourceType;

import lombok.Data;

@Data
@Builder
public class AuditDto {
    private UUID auditEventId;
    private UUID actorUserId;
    private Action action;
    private ResourceType resourceType;
    private  UUID resourceId;
    private  UUID requestId;
    private Instant createdAt;

}
