package com.asd.repository;

import com.asd.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditRepository extends JpaRepository<Audit, Long>{
}

