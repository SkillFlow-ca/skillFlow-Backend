package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class PaymentSkillFlow {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idPayment;

    private double amount;
    private Instant paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // Enum: PENDING, PAID, FAILED
    private String transactionId;
    private Instant createdAt;
    private Instant updatedAt;
    @OneToOne
    private Panier panier;
}
