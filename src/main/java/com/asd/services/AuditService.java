package com.asd.services;

import com.asd.dto.AuditDto;
import com.asd.model.Action;
import com.asd.model.Audit;
import com.asd.model.ResourceType;
import com.asd.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditService {
    void recordAction(User actor, Action action, ResourceType resourceType, UUID resourceId);
    List<AuditDto> list(Action action, Instant from, Instant to);
}