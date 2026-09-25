package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.ContraindictionSeverity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient_contraindications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientContraindication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    Integer ruleId; // SERIAL PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_a_id")
    ActiveIngredient ingredientA; // first ingredient in the conflict pair

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_b_id")
    ActiveIngredient ingredientB; // second ingredient in the conflict pair

    @Enumerated(EnumType.STRING)
    @Column(name = "severity_level", length = 20)
    ContraindictionSeverity severityLevel;

    @Column(name = "conflict_reason", columnDefinition = "TEXT", nullable = false)
    String conflictReason;

    @Column(name = "recommended_interval_days")
    @Builder.Default
    Integer recommendedIntervalDays = 0; // default 0

    @Column(name = "action_guidelines", columnDefinition = "TEXT")
    String actionGuidelines;
}
