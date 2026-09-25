package com.dww.DermaClinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinical_safety_alerts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClinicalSafetyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    Long alertId; // BIGSERIAL PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    TreatmentSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    IngredientContraindication rule;

    @Column(name = "alert_type", nullable = false, length = 100)
    String alertType;

    @Column(name = "alert_message", columnDefinition = "TEXT", nullable = false)
    String alertMessage;

    @Column(name = "overridden_by_doctor", nullable = false)
    @Builder.Default
    Boolean overriddenByDoctor = false; // default false

    @Column(name = "doctor_override_reason", columnDefinition = "TEXT")
    String doctorOverrideReason;

    @Column(name = "created_at", updatable = false)
    Instant createdAt; // set on create only

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now(); // stamp creation time
        }
    }
}
