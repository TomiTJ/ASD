package com.asd.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.asd.model.Action;
import com.asd.model.ResourceType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditDto {
    private UUID auditEventId;
    private Integer userId;
    private String userName;
    private Action action;
    private ResourceType resourceType;
    private UUID resourceId;
    private UUID requestId;
    private LocalDateTime createdAt;
}