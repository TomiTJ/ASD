package com.asd.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_event")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audit_event_id")
    private UUID auditEventId;

    // FK to users.id (the existing user model)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Action action;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 40)
    private ResourceType resourceType;

    @Column(name = "resource_id", nullable = false)
    private UUID resourceId;

    @Column(name = "request_id", nullable = false)
    private UUID requestId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}