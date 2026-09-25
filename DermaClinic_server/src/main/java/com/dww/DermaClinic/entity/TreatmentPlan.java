package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.TreatmentPlanStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "treatment_plans",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_treatment_plans_code", columnNames = "plan_code")
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    Long planId;

    /** Owning patient — cascade delete handled at DB level */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    /** Responsible doctor */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    User doctor;

    /** Human-readable unique plan code */
    @Column(name = "plan_code", unique = true, nullable = false, length = 50)
    String planCode;

    /** ICD-10 diagnosis code */
    @Column(name = "diagnosis_icd10", length = 50)
    String diagnosisIcd10;

    /** Full diagnosis description */
    @Column(name = "diagnosis_description", nullable = false, columnDefinition = "TEXT")
    String diagnosisDescription;

    /** Total number of sessions planned */
    @Column(name = "total_estimated_sessions", nullable = false)
    Integer totalEstimatedSessions;

    /** Lifecycle status of this plan */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    TreatmentPlanStatus status;

    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @Column(name = "expected_end_date")
    LocalDate expectedEndDate;

    /** Set on first persist */
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    /** Set on each update */
    @Column(name = "updated_at")
    Instant updatedAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now(); // stamp creation time
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now(); // stamp last modification time
    }
}
