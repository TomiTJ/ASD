
package com.asd.services;

import com.asd.dto.AuditDto;
import com.asd.model.Action;
import com.asd.model.ResourceType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuditService {

    /**
     * List audit events, optionally filtered by action and date range (inclusive of both from and to dates).
     */
    List<AuditDto> list(Action action, LocalDate from, LocalDate to);

    /**
     * Convenience method for callers that just want everything.
     */
    default List<AuditDto> findAllAudits() {
        return list(null, null, null);
    }

    /**
     * Persist a new audit event and return it as a DTO.
     */
    AuditDto record(UUID actorUserId,
                    Action action,
                    ResourceType resourceType,
                    UUID resourceId,
                    UUID requestId);
}
