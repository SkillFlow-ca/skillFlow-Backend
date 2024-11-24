package com.skillflow.skillflowbackend.model;
import com.skillflow.skillflowbackend.model.enume.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Payment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idPayment;

    private double amount;
    private Date paymentDate;
    private PaymentStatus paymentStatus; // Enum: PENDING, PAID, FAILED
    private String transactionId;
    private Date createdAt;
    private Date updatedAt;
    @ManyToOne
    private User user;

    @OneToOne
    private Course course;
}
