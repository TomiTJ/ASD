package com.asd.repository;

import com.asd.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AuditRepository extends JpaRepository<Audit, UUID>{
}

