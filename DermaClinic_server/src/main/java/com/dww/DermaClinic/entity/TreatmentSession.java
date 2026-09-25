package com.dww.DermaClinic.entity;

import com.dww.DermaClinic.enums.SessionStatus;
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
@Table(name = "treatment_sessions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    Long sessionId;

    /** The treatment plan this session belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    TreatmentPlan treatmentPlan;

    /** The phase this session is part of */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id")
    TreatmentPhase treatmentPhase;

    /** Patient receiving treatment */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    /** Doctor overseeing the session */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    User doctor;

    /** Technician performing the session */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id", referencedColumnName = "id")
    User technician;

    /** Sequential number within the plan */
    @Column(name = "session_number", nullable = false)
    Integer sessionNumber;

    /** Booked time */
    @Column(name = "scheduled_at", nullable = false)
    Instant scheduledAt;

    /** Actual execution time, null until performed */
    @Column(name = "performed_at")
    Instant performedAt;

    /** Doctor's clinical observations */
    @Column(name = "clinical_notes", columnDefinition = "TEXT")
    String clinicalNotes;

    /** Digital signature data */
    @Column(name = "doctor_signature", columnDefinition = "TEXT")
    String doctorSignature;

    /** Session lifecycle status */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    SessionStatus status;

    /** Record creation timestamp — immutable after persist */
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now(); // stamp creation time only
    }
}
