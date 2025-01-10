package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.PaymentSkillFlow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentSkillFlow, Long> {
    public boolean existsByTransactionId(String transactionId);
}
