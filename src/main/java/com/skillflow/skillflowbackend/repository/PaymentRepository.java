package com.skillflow.skillflowbackend.repository;

import com.skillflow.skillflowbackend.model.PaymentSkillFlow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentSkillFlow, Long> {
    public boolean existsByTransactionId(String transactionId);

    @Query("SELECT p FROM PaymentSkillFlow p order by p.createdAt desc")
    public Page<PaymentSkillFlow> findAllPayments(Pageable pageable);
}
