package com.skillflow.skillflowbackend.model;

import com.skillflow.skillflowbackend.model.enume.StatusPanier;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class Panier {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long idPanier;

    private double totalAmount;
    private Instant createdAt;
    private Instant updatedAt;
    @Enumerated(EnumType.STRING)
    private StatusPanier statusPanier; // Enum: PENDING, PAID, FAILED



    @OneToOne
    private PaymentSkillFlow paymentSkillFlow;
    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "panierList")
    private List<Course> courseList;
}
