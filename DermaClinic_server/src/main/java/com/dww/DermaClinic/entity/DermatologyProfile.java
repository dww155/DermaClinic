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
@Table(name = "dermatology_profiles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DermatologyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    Long profileId;

    // One-to-one with Patient; cascade DELETE handled at DB level
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", unique = true)
    Patient patient;

    // Fitzpatrick scale 1–6
    @Column(name = "fitzpatrick_skin_type")
    Integer fitzpatrickSkinType;

    @Column(name = "baumann_skin_type", length = 10)
    String baumannSkinType;

    @Column(name = "current_skin_concerns", columnDefinition = "TEXT")
    String currentSkinConcerns;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    String medicalHistory;

    @Column(name = "allergy_history", columnDefinition = "TEXT")
    String allergyHistory;

    @Column(name = "current_medications", columnDefinition = "TEXT")
    String currentMedications;

    @Column(name = "sun_exposure_level", length = 50)
    String sunExposureLevel;

    @Column(name = "skincare_routine_summary", columnDefinition = "TEXT")
    String skincareRoutineSummary;

    @Column(name = "contraindications", columnDefinition = "TEXT")
    String contraindications;

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
