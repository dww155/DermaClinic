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
@Table(
        name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_patients_code", columnNames = "medical_record_code"),
                @UniqueConstraint(name = "uk_patients_user", columnNames = "user_id")
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    Long patientId;

    // Link to User account — single source of truth for personal info
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    // Clinic this patient is registered at
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    Clinic clinic;

    // Unique EMR identifier
    @Column(name = "medical_record_code", nullable = false, length = 50)
    String medicalRecordCode;

    // National ID / Passport — required for medical records
    @Column(name = "id_card_number", length = 30)
    String idCardNumber;

    // Occupation — relevant for dermatology (sun exposure, chemical contact)
    @Column(name = "occupation", length = 100)
    String occupation;

    @Column(name = "emergency_contact_name")
    String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    String emergencyContactPhone;

    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
