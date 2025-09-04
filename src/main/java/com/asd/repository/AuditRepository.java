package com.asd.repository;

import com.asd.model.Action;
import com.asd.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditRepository extends JpaRepository<Audit, UUID> {

    // Find by primary key field name on the entity
    Optional<Audit> findByAuditEventId(UUID auditEventId);

    // Optional: basic filtering used by the service/controller
    @Query("""
        select a from Audit a
        where (:action is null or a.action = :action)
          and (:fromTs is null or a.createdAt >= :fromTs)
          and (:toTs is null or a.createdAt <= :toTs)
        order by a.createdAt desc
    """)
    List<Audit> findByFilters(@Param("action") Action action,
                              @Param("fromTs") Instant fromTs,
                              @Param("toTs") Instant toTs);
}


