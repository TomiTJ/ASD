package com.asd.model;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;
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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "audit_event_id", nullable = false , updatable = false)
    private java.util.UUID auditEventId;


    @Enumerated(EnumType.STRING)
    @Column(name = "action" , nullable = false, length = 40)
    private aciton action;


    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type" , nullable = false, length = 40)
    private transactionType action;




}


