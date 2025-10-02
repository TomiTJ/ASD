package com.asd;

import com.asd.dto.AuditDto;
import com.asd.model.Action;
import com.asd.model.Audit;
import com.asd.model.ResourceType;
import com.asd.repository.AuditRepository;
import com.asd.services.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AuditServiceImplTest {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditRepository auditRepository;

    @Test
    void testRecordActionCreatesAuditEvent() {
        UUID actorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        AuditDto dto = auditService.record(actorId, Action.CREATE, ResourceType.User, resourceId, requestId);

        // check return value
        assertNotNull(dto.getAuditEventId());
        assertEquals(Action.CREATE, dto.getAction());
        assertEquals(ResourceType.User, dto.getResourceType());

        // check it actually saved
        Audit saved = auditRepository.findById(dto.getAuditEventId()).orElseThrow();
        assertEquals(actorId, saved.getActorUserId());
        assertEquals(resourceId, saved.getResourceId());
    }
}