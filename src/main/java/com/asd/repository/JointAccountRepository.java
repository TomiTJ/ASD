package com.asd.repository;

import com.asd.model.JointAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JointAccountRepository extends JpaRepository<JointAccount, UUID> {

    List<JointAccount> findByAccountId(Long accountId);

    List<JointAccount> findByCustomerId(Long customerId);

    boolean existsByAccountIdAndCustomerId(Long accountId, Long customerId);

    void deleteByAccountIdAndCustomerId(Long accountId, Long customerId);

    void deleteByAccountId(Long accountId);
}