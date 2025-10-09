package com.asd.repository;

import com.asd.model.Action;
import com.asd.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditRepository extends JpaRepository<Audit, UUID> {

    @Query("""
SELECT a FROM Audit a
WHERE (:action IS NULL OR a.action = :action)
  AND (:fromTs IS NULL OR a.createdAt >= CAST(:fromTs AS timestamp))
  AND (:toTs IS NULL OR a.createdAt <= CAST(:toTs AS timestamp))
ORDER BY a.createdAt DESC
""")
    List<Audit> findByFilters(@Param("action") Action action,
                              @Param("fromTs") LocalDateTime fromTs,
                              @Param("toTs") LocalDateTime toTs);
}