package com.asd.repository;

import com.asd.model.Action;
import com.asd.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<Audit, UUID> {

    // When Audit has: @ManyToOne User user;
    List<Audit> findByUser_Id(Integer userId);

    @Query("""
        select a from Audit a
        where (:action is null or a.action = :action)
          and (a.createdAt >= coalesce(:fromTs, a.createdAt))
          and (a.createdAt <= coalesce(:toTs,   a.createdAt))
        order by a.createdAt desc
    """)
    List<Audit> findByFilters(@Param("action") Action action,
                              @Param("fromTs") Instant fromTs,
                              @Param("toTs") Instant toTs);
}