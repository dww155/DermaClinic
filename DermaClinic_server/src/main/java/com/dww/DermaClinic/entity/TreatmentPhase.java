package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.TreatmentPhaseStatus;
import com.dww.DermaClinic.enums.TreatmentPhaseType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "treatment_phases")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phase_id")
    Long phaseId;

    /** Parent treatment plan — cascade delete handled at DB level */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id")
    TreatmentPlan treatmentPlan;

    /** Clinical phase type (attack / recovery / maintenance) */
    @Enumerated(EnumType.STRING)
    @Column(name = "phase_type", length = 30)
    TreatmentPhaseType phaseType;

    /** Ordering index within the plan */
    @Column(name = "phase_order", nullable = false)
    Integer phaseOrder;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    /** Number of sessions targeted in this phase */
    @Column(name = "target_sessions", nullable = false)
    Integer targetSessions;

    /** Progress status of this phase */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    TreatmentPhaseStatus status;
}
