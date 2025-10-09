package com.asd.services.impl;

import com.asd.dto.AuditDto;
import com.asd.model.Audit;
import com.asd.model.Action;
import com.asd.repository.AuditRepository;
import com.asd.services.AuditService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public List<AuditDto> list(Action action, LocalDateTime from, LocalDateTime to) {
        return auditRepository.findByFilters(action, from, to)
                .stream()
                .map(a -> AuditDto.builder()
                        .auditEventId(a.getAuditEventId())
                        .userId(a.getUser() != null ? a.getUser().getId() : null)
                        .userName(a.getUser() != null ? a.getUser().getFullName() : "Unknown User")
                        .action(a.getAction())
                        .resourceType(a.getResourceType())
                        .resourceId(a.getResourceId())
                        .requestId(a.getRequestId())
                        .createdAt(a.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}