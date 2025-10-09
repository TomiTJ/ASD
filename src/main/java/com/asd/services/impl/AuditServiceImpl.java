package com.asd.services.impl;


import com.asd.model.Action;
import com.asd.model.Audit;
import com.asd.model.ResourceType;
import com.asd.services.AuditService;
import com.asd.dto.AuditDto;
import com.asd.repository.AuditRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public List<AuditDto> list(Action action, LocalDate from, LocalDate to) {
        LocalDateTime fromTs = (from == null) ? null : from.atStartOfDay();
        LocalDateTime toTs   = (to == null) ? null : to.atTime(LocalTime.MAX);;
        List<Audit> audits = auditRepository.findByFilters(action, fromTs, toTs);
        return audits.stream().map(this::mapToAuditDto).collect(Collectors.toList());
    }

    @Override
    public AuditDto record(UUID actorUserId, Action action, ResourceType resourceType, UUID resourceId, UUID requestId) {
        Audit e = new Audit();
        e.setActorUserId(actorUserId);
        e.setAction(action);
        e.setResourceType(resourceType);
        e.setResourceId(resourceId);
        e.setRequestId(requestId);
        e.setCreatedAt(LocalDateTime.now()); // Let Spring Data auditing handle this
        auditRepository.save(e);
        return mapToAuditDto(e);
    }

    private AuditDto mapToAuditDto(Audit audit) {
      return AuditDto.builder()
              .auditEventId(audit.getAuditEventId())
              .actorUserId(audit.getActorUserId())
              .action(audit.getAction())
              .resourceType(audit.getResourceType())
              .resourceId(audit.getResourceId())
              .requestId(audit.getRequestId())
              .createdAt(audit.getCreatedAt())
              .build();
    }

}
