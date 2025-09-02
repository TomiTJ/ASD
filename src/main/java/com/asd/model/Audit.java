package com.asd.model;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;
import java.time.Instant;
/* Notes:
 - Audit

 */

enum Action {
    CREATE, UPDATE, DELETE, APPROVE, REJECT, RESET_PASSWORD, APPROVE_PASSWORD, REJECT_PASSWORD
}

enum ResourceType {
    User, Customer, Account, Transaction
}

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "audit_event")
public class Audit {
    //which interaction (was it interaction 1, interaction 2. can be hundreds of interactions to be located from)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "audit_event_id", nullable = false , updatable = false)
    private UUID auditEventId;

    //who did (which user_id did it)
    @Column(name = "actor_user_id", nullable = false , updatable = false)
    private UUID actorUserId;

    //what did (e.g. was the interaction create , delete, approving or rejecting etc)
    @Enumerated(EnumType.STRING)
    @Column(name = "action" , nullable = false, length = 40)
    private Action action;

    //on what (e.g. Customer, User , Account, Transaction)
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type" , nullable = false, length = 40)
    private  ResourceType resourceType;

    //on what specifically (e.g. which customer did it affect)
    @Column(name = "resource_id", nullable = false, length = 40)
    private  UUID resourceId;

    //Track n Trace

    @Column(name = "request_id",nullable = false)
    private  UUID requestId;

    //when did it happen

    @org.springframework.data.annotation.CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;


    public UUID getAuditEventId() {return auditEventId;}
    public void setAuditEventId(UUID auditEventId) {this.auditEventId = auditEventId;}

    public UUID getActorUserId() {return actorUserId;}
    public void setActorUserId(UUID userId) {this.actorUserId = userId;}

    public Action getAction() {return action;}
    public void setAction(Action action) {this.action = action;}

    public ResourceType getResourceType() {return resourceType;}
    public void setResourceType(ResourceType resourceType) {this.resourceType = resourceType;}

    public UUID getResourceId() {return resourceId;}
    public void setResourceId(UUID resourceId) {this.resourceId = resourceId;}

    public UUID getRequestId() {return requestId;}
    public void setRequestId(UUID requestId) {this.requestId = requestId;}

    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}



}


