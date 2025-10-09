package com.asd.services.impl;

import com.asd.dto.AuditDto;
import com.asd.model.Audit;
import com.asd.model.Action;
import com.asd.model.ResourceType;
import com.asd.model.User;
import com.asd.repository.AuditRepository;
import com.asd.services.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository repo;

    @Override
    public void recordAction(User actor, Action action, ResourceType resourceType, UUID resourceId) {
        Audit e = new Audit();
        e.setUser(actor);                 // FK -> users.id
        e.setAction(action);
        e.setResourceType(resourceType);  // ✅ pass enum, not String
        e.setResourceId(resourceId != null ? resourceId : UUID.randomUUID());
        e.setRequestId(UUID.randomUUID());
        e.setCreatedAt(Instant.now());    // ✅ make sure Audit.createdAt is Instant
        repo.save(e);
    }

    @Override
    public List<AuditDto> list(Action action, Instant from, Instant to) {
        return repo.findByFilters(action, from, to).stream()
                .map(a -> AuditDto.builder()
                        .auditEventId(a.getAuditEventId())
                        .userId(a.getUser() != null ? a.getUser().getId() : null)  // assuming users.id is Integer
                        .action(a.getAction())
                        .resourceType(a.getResourceType())
                        .resourceId(a.getResourceId())
                        .requestId(a.getRequestId())
                        .createdAt(a.getCreatedAt())  // Instant in DTO
                        .build())
                .collect(Collectors.toList());
    }
}