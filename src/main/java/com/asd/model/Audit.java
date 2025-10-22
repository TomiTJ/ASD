
package com.asd.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "audit_event_id")
    private UUID auditEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="action", nullable = false, length = 50)
    private Action action;

    @Enumerated(EnumType.STRING)
    @Column(name="resource_type",nullable = false,length = 20)
    private ResourceType resourceType;

    @Column(name="resource_id" )
    private UUID resourceId;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "created_at")
    private Instant createdAt;
}
